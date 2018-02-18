package org.arg.pushforth.interpreter;

import org.arg.pushforth.instructions.Code;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.instructions.Numbers;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Main {

	// Causes the dictionary to fill up
	
	static {
		Numbers.load();
		Instructions.load();
		Code.load();
	}
	
	public static void main(String[] args) {
		String concat = "[[";
		for (int i = 0; i < args.length; ++i) {
			concat += " " + args[i];
		}
		concat += "]]";

		
		System.out.println("Concat: " + concat);

		run(concat, true);
	}
	
	public static void run(String concat, boolean step) {
		
	
		Program prog = Programs.parse(concat);
		System.out.println("Prog = " + prog);
		Program old = prog;
		int i = 0;
		
		if (step) {
			System.out.println(i + ": " + prog);
		}
		
		while ( !(prog = Instructions.reduce(prog)).isEmpty()) {
			if (old == prog) break;
			old = prog;
			if (step) {
				i++;
				System.out.println(i +": " + prog);
			}
		}
		System.out.println(prog);
		
	}
	
}
