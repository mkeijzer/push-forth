package org.arg.pushforth.program;

public class SingletonProgram extends AbstractProgram implements Program {

	private final Object payload;
	
	SingletonProgram(Object payload) {
		this.payload = payload;
	}
	
	@Override
	public Object first() {
		return payload;
	}

	@Override
	public Program rest() {
		return Program.nil;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
