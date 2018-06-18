package org.arg.pushforth.instructions;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Booleans {
	
	static public void load() {}
	
	static {
		InstructionFactory.registerMembers(Booleans.class);
	}
	
	
	@InstructionTest(tests={"[[true true &&]]", "[[true false && !]]"})
	@InsDef(name = "&&")
	public static Boolean and(Boolean a, Boolean b) {
		return a&&b;
	}
	
	@InstructionTest(tests={"[[false !]]", "[[true ! !]]"})
	@InsDef(name = "!")
	public static Boolean not(Boolean a) {
		return !a;
	}
	
	@InstructionTest(tests={"[[=] a a]", "[[= !] a b]"})
	@InsDef(name="=")
	public static Boolean eq(Object a, Object b) {
		return a.equals(b);
	}
	public static Instruction eq = InstructionFactory.make(Booleans.class, "eq");

	@InstructionTest(tests={"[[typeof 1 typeof = !] a]", "[[typeof a typeof =] b]"})
	public static Class<?> typeof(Object a) {
		return a.getClass();
	}

	public static Instruction typeof = InstructionFactory.make(Booleans.class, "typeof");

	public static Instruction createTypeInstruction(final Class<?> clazz, String name) {
		Program prog = Programs.list(typeof, clazz, eq);
		return InstructionFactory.make(name, prog);
	}

	public static Instruction isbool = Booleans.createTypeInstruction(Number.class, "bool?");
	
}
