package org.arg.test.interpreter;

import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.instructions.Numbers;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Main {

	// Causes the dictionary to fill up
	
	static {
		Numbers.load();
		Instructions.load();
	}
	
	public static void main(String[] args) {
		run(args, false);
	}
	
	public static void run(String[] args, boolean step) {
		
		if (args.length == 0) {
			System.out.println("[[]]");
			System.exit(1);
		}
		
		String concat = "";
		for (int i = 0; i < args.length; ++i) {
			concat += " " + args[i];
		}
		
		System.out.println("Concat: " + concat);
		
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
			i++;
			if (step) {
				System.out.println(i +": " + prog);
			}
		}
		System.out.println(prog);
		
	}
	
}
