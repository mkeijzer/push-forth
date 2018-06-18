package org.arg.pushforth.program;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class HashedProgram implements Program {

	private static final Map<HashedProgram, HashedProgram> map = Collections
			.synchronizedMap(new WeakHashMap<>());
	private static HashedProgram hashedNil = new HashedProgram();
	static {
		map.put(hashedNil, hashedNil);
	}
	
	final Object first;
	final HashedProgram rest;
	final int hashCode;

	
	private HashedProgram() {
		first = null;
		rest = null;
		hashCode = 0;
	}

	private HashedProgram(Object first, HashedProgram rest, int hashCode) {
		this.first = first;
		this.rest = rest;
		this.hashCode = hashCode;
	}

	public static boolean exists(Program program) {
		if (program instanceof HashedProgram) {
			return true;
		}
		if (program.isEmpty()) {
			return true;
		}
		Object first = program.first();
		if (first instanceof Program ) {
			first = hash((Program) first);
		}
		
		HashedProgram rest = hash(program.rest());
		int hashCode = 37 * first.hashCode() + rest.hashCode;
		HashedProgram h = new HashedProgram(first, rest, hashCode);
		return map.containsKey(h);
	}

	public static HashedProgram hash(Program program) {
		if (program instanceof HashedProgram) {
			return (HashedProgram) program;
		}
		if (program.isEmpty()) {
			return hashedNil; 
		}
		Object first = program.first();
		if (first instanceof Program) {
			first = hash((Program) first);
		} 
		
		HashedProgram rest = hash(program.rest());
		int hashCode = 37 * first.hashCode() + rest.hashCode;
		HashedProgram h = new HashedProgram(first, rest, hashCode);
		HashedProgram exists = map.get(h);
		if (exists == null) {
			exists = h;
			map.put(h, h);
		}
		return exists;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof HashedProgram)) {
			assert !(obj instanceof Program);
			return false;
		}
		HashedProgram prog = (HashedProgram) obj;
		if (first instanceof HashedProgram && prog.first instanceof HashedProgram) {
			return first == prog.first && rest == prog.rest;
		}
		return first.equals(prog.first) && rest == prog.rest;
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
		return first != null;
	}

	@Override
	public String toString() {
		return Programs.print(this, 10);
	}
	
}
