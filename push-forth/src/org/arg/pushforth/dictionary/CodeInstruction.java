package org.arg.pushforth.dictionary;

import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

class CodeInstruction implements Instruction {

	final Program program;
	final String name;
	
	public CodeInstruction(String name, Program program) {
		this.program = program;
		this.name = name;
	}
	
	@Override
	public Program apply(Program stack) {
		return Programs.cons(program, stack);
	}

	@Override
	public String toString() {
		return name;
	}
	
}
