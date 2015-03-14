package org.arg.pushforth.instructions;

import org.arg.pushforth.program.Program;

public interface Instruction {
	
	Program apply(Program stack);

}
