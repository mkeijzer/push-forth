package org.arg.pushforth.dictionary;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arg.pushforth.annotations.AdditionalArgumentChecks;
import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.dictionary.Predicate.TypePredicate;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;

/**
 * InstructionFactory creates instructions from java member functions 
 * and adds them to the language symbol table
 * 
 * @author Maarten Keijzer
 *
 */
public class InstructionFactory {

	// contains all methods wrapped in instructions
	private static List<Method> methodList = new ArrayList<Method>();
	
	// contains an ordered list of all instructions
	private static Map<String, List<DynamicInstruction>> overloadedMap = new HashMap<String, List<DynamicInstruction>>();

	public static List<Method> getMethodList() {
		return methodList;
	}
	
	private static Predicate[] createStaticPredicates(Method method) {
		methodList.add(method);
		Class<?>[] types = method.getParameterTypes();
		Predicate[] predicates = new Predicate[types.length];
		for (int i = 0; i < predicates.length; ++i) {
			predicates[i] = new TypePredicate(types[i]);
		}
		return predicates;
	}

	private static Predicate[] createMemberPredicates(Method method) {
		methodList.add(method);
		Class<?>[] types = method.getParameterTypes();
		Predicate[] predicates = new Predicate[1 + types.length];

		predicates[types.length] = new TypePredicate(method.getDeclaringClass());
		for (int i = 0; i < types.length; ++i) {
			predicates[i] = new TypePredicate(types[i]);
		}
		
		return predicates;
	}

	public static Instruction make(Class<?> clazz, String name) {
		return make(clazz, name, "");
	}
	
	public static Instruction make(Class<?> clazz, String name, Predicate[] preds) {
		return make(clazz, name, "", preds);
	}

	private static Instruction make(Class<?> clazz, String name, String alias) {
				
		Instruction ins = null;
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(name)) {
				ins = handleMember(m, alias);
			}
		}

		return ins;
	}

	public static Instruction make(Class<?> clazz, String name, String alias, Predicate[] preds) {
		
		Instruction ins = null;
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(name)) {
				ins = handleMember(m, alias, preds);
			}
		}

		return ins;
	}

	
	private InstructionFactory() {
	}

	/**
	 * Reads all annotated members of the class and creates instructions
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<Instruction> registerMembers(Class<?> clazz) {

		Method[] method = clazz.getDeclaredMethods();

		List<Instruction> ret = new ArrayList<Instruction>();

		for (Method m : method) {

			InsDef ann = m.getAnnotation(InsDef.class);
			if (ann == null) {
				// skip
				continue;
			}

			String alias = m.getName();
			if (ann != null) {
				alias = ann.name();
			}

			ret.add(handleMember(m, alias));
		}

		return ret;
	}


	private static Instruction handleMember(Method m, 
			String alias) {

		int mods = m.getModifiers();
		Predicate[] preds;
		if (Modifier.isStatic(mods)) {
			preds = createStaticPredicates(m);
		} else {
			preds = createMemberPredicates(m);
		}
		return handleMember(m, alias, preds);
	}
	
	private static Instruction handleMember(Method m, String alias, Predicate[] preds) {
		
		
		if (alias.isEmpty() || alias == null) {
			InsDef insName = m.getAnnotation(InsDef.class);
			if (insName == null) {
				alias = m.getName();
			} else {
				alias = insName.name();
			}
		}
				
		// Check if there are additional parameters to be loaded
		AdditionalArgumentChecks additional = m.getAnnotation(AdditionalArgumentChecks.class);
		if (additional != null) {
			String[] names = additional.predicates();
			Class<?> clazz = m.getDeclaringClass();
			
			if (names.length != preds.length) {
				throw new RuntimeException("Initialize Instruction: wrong number of predicates wrt number of function arguments");
			}

			for (int i = 0; i < preds.length; ++i) {
				try {
					Field predicateField = clazz.getField(names[i]);
					Predicate pred = (Predicate) predicateField.get(null); // has to be static
					preds[i] = Predicates.and(preds[i], pred);
				} catch (SecurityException e) {
					throw new RuntimeException("Security excpetion when loading predicate");
				} catch (NoSuchFieldException e) {
					throw new RuntimeException("No such (public static) field: " + names[i] + " on class " + clazz);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		DynamicInstruction ins = new DynamicInstruction(m, alias, preds);		
		return addInstruction(alias, ins);
	}
	
	public static Instruction addInstruction(String name, DynamicInstruction ins) {
		List<DynamicInstruction> instructions = overloadedMap.get(name);

		if (instructions == null) {
			instructions = new ArrayList<DynamicInstruction>();
			instructions.add(ins);
			overloadedMap.put(name, instructions);
			
			if (SymbolTable.get(name) != null) {
				throw new RuntimeException("Non-dynamic instruction found: " + name + " " + SymbolTable.get(name));
			}
			SymbolTable.put(name, ins);
			return ins;
		} 
				
		// check if there are issues
		int i = 0;
		for(i = 0; i < instructions.size(); ++i) {
			Method m1 = ins.getMethod();
			Method m2 = instructions.get(i).getMethod();

			Class<?>[] param1 = m1.getParameterTypes();
			Class<?>[] param2 = m2.getParameterTypes();
			
			if (param1.length == 0 || param2.length == 0) {
				throw new RuntimeException("Overloading an instruction with no arguments: " + m1 + " " + m2);
			}
			
			Class<?> firstType = param1[0];
			Class<?> secondType = param2[0];
			
			if (firstType.isAssignableFrom(secondType)) {
				break;
			}
			
		}
		
		instructions.add(i, ins);
		
		Instruction result = new OverloadedInstruction(name, instructions);

		SymbolTable.put(name, result);

		return result;
	}

	public static Instruction make(String name, Program prog) {
		if (SymbolTable.get(name) != null) {
			throw new RuntimeException("Instruction already defined");
		}
		
		Instruction ins = new CodeInstruction(name, prog);
		
		SymbolTable.put(name, ins);
		return ins;
	}

}
