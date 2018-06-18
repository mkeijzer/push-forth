package org.arg.pushforth.program;

class AppendedProgram extends AbstractProgram implements Program {

	final Program first;
	final Program second;
	
	public AppendedProgram(Program first, Program second) {
		super();
		assert first != null;
		assert second != null;
		assert !first.isEmpty();
		
		this.first = first;
		this.second = second;
	}

	@Override
	public Object first() {
		return first.first();
	}

	@Override
	public Program rest() {
		Program firstrest = first.rest();
		if (firstrest.isEmpty()) {
			return second;
		}
		return new AppendedProgram(firstrest, second);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public int length() {
		return first.length() + second.length();
	}
	
}
