package org.arg.pushforth.program;

import org.arg.pushforth.instructions.Instructions;

/**
 * The Program interface, contains nil and accessors
 * 
 * @author Maarten Keijzer
 *
 */
public interface Program {

	public static Program nil = new ArrayProgram();
	public static Program halted = new SingletonProgram(nil);
	
	Object first();
	Program rest();

	boolean isEmpty();	
	
	/* Default helper functions */
	
	default public boolean isHalted() {
		Object first = first();
		if (!(first instanceof Program)) {
			return true;
		}
		
		return ((Program)first).isEmpty();
	}
	
	default public Program reduce() {
		return Instructions.reduce(this);
	}
	
	default public Program cons(Object obj) {
		if (obj == null) {
			throw new NullPointerException("Lists are not allowed to contain null");
		}
		return new ConsedProgram(obj, this);
	}
	
	default public Program append(Program toAppend) {
		if (toAppend.isEmpty()) {
			return this;
		}
		
		return append(toAppend.rest()).cons(toAppend.first()); 
	}
	
	default public int length() {
		int sz = 0;
		Program p = this;
		while (!p.isEmpty()) {
			sz++;
			p = p.rest();
		}
		return sz;
	}

	default public boolean equals(Program oth) {
		Program one = this;
		while (!one.isEmpty() && !oth.isEmpty()) {
			Object a = one.first();
			Object b = oth.first();
			if (!a.equals(b)) {
				return false;
			}
			one = one.rest();
			oth = oth.rest();
		}
		
		return one.isEmpty() && oth.isEmpty();
	}
	
}
