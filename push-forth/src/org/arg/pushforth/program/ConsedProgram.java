package org.arg.pushforth.program;

/**
 * The Lisp-Style program
 * 
 * @author Maarten Keijzer
 *
 */
class ConsedProgram extends AbstractProgram implements Program {

	final Object first;
	final Program rest;
	
	public ConsedProgram(Object first, Program rest) {
		super();
		assert(rest != null);
		assert(first != null);
		
		this.first = first;
		this.rest = rest;
	}

	@Override
	public Object first() {
		return first;
	}

	@Override
	public Program rest() {
		return rest;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
}
