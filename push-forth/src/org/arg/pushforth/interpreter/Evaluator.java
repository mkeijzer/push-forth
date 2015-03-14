package org.arg.pushforth.interpreter;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Evaluator {

	Deque<Program> programs = new ArrayDeque<Program>();
	Deque<Program> done = new ArrayDeque<Program>();
		
	public void add(Program prog) {
		programs.addFirst(prog);
	}
	
	public int size() {
		return programs.size();
	}
	
	public void cull() {
		programs.pop();
	}
	
	Collection<Program> getDoneList() {
		return done;
	}
	
	public void run(int n) {
		Program prog = programs.pollFirst();
		
		if (prog == null) {
			return;
		}
		
		if (Math.random() < 0.001) {
			return; // cull
		}
		
		for (int i = 0; i < n; ++i) {
			prog = Instructions.reduce(prog);
			if (Programs.isHalted(prog)) {
				done.add(prog);
				return;
			}
		}
		
		programs.addLast(prog);
	}
	
}
