package org.arg.pushforth.program;

public abstract class AbstractProgram implements Program {

	int hash = 0;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		return Programs.print(this, builder, 0).toString();
	}

	@Override
	public int hashCode() {
		if (isEmpty())
			return 0;

		if (hash == 0) {
			int h = 0;
			int large = 2147483647; // largest prime smaller than 1<<31
			int prime1 = 31;
			int prime2 = 889939283;

			h = (first().hashCode() * prime1) % large;
			h += (rest().hashCode() * prime2) % large;
			if (h == 0) {
				h = -1;
			}
			hash = h; // atomic
		}

		return hash;
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
