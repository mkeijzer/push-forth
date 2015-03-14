package org.arg.pushforth.dictionary;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.arg.pushforth.annotations.InstructionName;
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
		TypePredicate[] predicates = new TypePredicate[types.length];
		for (int i = 0; i < predicates.length; ++i) {
			predicates[i] = new TypePredicate(types[i]);
		}
		return predicates;
	}

	private static Predicate[] createMemberPredicates(Method method) {
		methodList.add(method);
		Class<?>[] types = method.getParameterTypes();
		TypePredicate[] predicates = new TypePredicate[1 + types.length];

		predicates[types.length] = new TypePredicate(method.getDeclaringClass());
		for (int i = 0; i < types.length; ++i) {
			predicates[i] = new TypePredicate(types[i]);
		}
		
		return predicates;
	}

	public static Instruction make(Class<?> clazz, String name) {
		return make(clazz, name, name);
	}
	public static Instruction make(Class<?> clazz, String name, String alias) {
				
		Instruction ins = null;
		for (Method m : clazz.getDeclaredMethods()) {
			if (m.getName().equals(name)) {
				ins = handleMember(m, alias);
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
	public static List<Instruction> readMembers(Class<?> clazz) {

		Method[] method = clazz.getDeclaredMethods();

		List<Instruction> ret = new ArrayList<Instruction>();

		for (Method m : method) {

			InstructionName ann = m.getAnnotation(InstructionName.class);
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
		DynamicInstruction ins = null;
		Predicate[] preds;
		if (Modifier.isStatic(mods)) {
			preds = createStaticPredicates(m);
		} else {
			preds = createMemberPredicates(m);
		}
		ins = new DynamicInstruction(m, alias, preds);
		
		return addInstruction(alias, ins);
	}
	
	public static Instruction addInstruction(String name, DynamicInstruction ins) {
		List<DynamicInstruction> instructions = overloadedMap.get(name);

		if (instructions == null) {
			instructions = new ArrayList<DynamicInstruction>();
			instructions.add(ins);
			overloadedMap.put(name, instructions);
			
			if (SymbolTable.get(name) != null) {
				throw new RuntimeException("Non-dynamic instruction found");
			}
			SymbolTable.put(name, ins);
			return ins;
		} 
		
		// check if there are issues
		int i = 0;
		for(i = 0; i < instructions.size(); ++i) {
			Method m1 = ins.getMethod();
			Method m2 = instructions.get(i).getMethod();
			
			Class<?> firstType = m1.getParameterTypes()[0];
			Class<?> secondType = m2.getParameterTypes()[0];
			
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
