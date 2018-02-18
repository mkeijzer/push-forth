package org.arg.pushforth.program;

public abstract class AbstractProgram implements Program {
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return Programs.print(this, builder, Integer.MAX_VALUE).toString();
	}

	@Override
	public int hashCode() {
		int large = 2147483647; // largest prime smaller than 1<<31
		int prime1 = 31;
		int prime2 = 889939283;

		int h = 0;
		Program prog = this;
		while (prog != Program.nil) {
			Object first = prog.first();
			h += (first.hashCode() * prime1) % large;
			prog = prog.rest();
			h = h * prime2;
		}
		
		return h;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Program)) {
			return false;
		}

		Program other = (Program) obj;

		if (isEmpty() && other.isEmpty())
			return true;
		if (isEmpty())
			return false;
		if (other.isEmpty())
			return false;

		return first().equals(other.first()) && rest().equals(other.rest());
	}
}
