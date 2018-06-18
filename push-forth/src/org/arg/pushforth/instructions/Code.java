package org.arg.pushforth.instructions;

import java.util.List;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Code {

	static public void load() {}
	
	static {
		InstructionFactory.registerMembers(Code.class);
	}
	
	@InsDef(name = "?")
	public static Program ifte(Boolean a, Program b, Program c) {
		return a?b:c;
	}
		
	@InstructionTest(tests={"[[nil?] []]", "[[nil? !] [0]]"})
	@InsDef(name = "nil?")
	public static Boolean isnil(Program prog) {
		return prog.isEmpty();
	}

	public static Instruction islist = Booleans.createTypeInstruction(
			Program.class, "list?");
	

	@InsDef(name = "nth") 
	public static Object nth(Program prog, Integer n) {
		int v = n;
		while (!prog.isEmpty() && v-- > 0) {
			prog = prog.rest();
		}
		if (prog.isEmpty()) {
			return prog;
		}
		return prog.first();
	}
	
	@InsDef(name = "nthProgram") 
	public static Program nthProgram(Program prog, Integer n) {
		int v = n;
		while (!prog.isEmpty() && v-- > 0) {
			prog = prog.rest();
		}
		if (prog.isEmpty()) {
			return prog;
		}
		return prog;
	}
	
	@InsDef(name="hash") 
	public static Long hashCode(Object o) {
		return (long) o.hashCode();
	}
	
}

