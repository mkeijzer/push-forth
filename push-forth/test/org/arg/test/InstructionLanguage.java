package org.arg.test;

import static org.arg.pushforth.program.Programs.list;

import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;

public class InstructionLanguage {

	public static void main(String[] args) {
		
		// cons: [[cons] X [Y] ] -> [[] [X Y] ]
		// cons: [[cons] X Y ] -> [[cons swap Y] X ]
		
		// [[i] [Z]] -> [ [Z] ]
		// [[i] Z] -> [ [i Z sw] ]
		
		// in code:
		String X = "x";
		String Y = "y";
		
		Instruction ins = createInstruction("cons", new Program[] { 
				rule( list(X, list(Y)), list( list(X, Y) )), 
		} ) ;
		
	}

	private static Program rule(Program antecedent, Program consequent) {
		return null;
	}
	
	private static Instruction createInstruction(String string, Program[] programs) {
		
		return null;
		
	}
	
}
