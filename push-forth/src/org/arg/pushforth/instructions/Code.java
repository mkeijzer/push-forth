package org.arg.pushforth.instructions;

import java.util.List;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Code {

	static public void load() {}
	
	static {
		InstructionFactory.readMembers(Code.class);
	}
	
	@InstructionName(name = "?")
	public static Program ifte(Boolean a, Program b, Program c) {
		return a?b:c;
	}
		
	@InstructionTest(tests={"[[nil?] []]", "[[nil? !] [0]]"})
	@InstructionName(name = "nil?")
	public static Boolean isnil(Program prog) {
		return prog.isEmpty();
	}

	public static Instruction islist = Booleans.createTypeInstruction(
			Program.class, "list?");
	

	@InstructionName(name="hash") 
	public static Long hashCode(Object o) {
		return (long) o.hashCode();
	}
	
}

