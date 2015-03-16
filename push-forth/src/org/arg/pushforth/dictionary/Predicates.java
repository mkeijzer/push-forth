package org.arg.pushforth.dictionary;

import org.arg.pushforth.program.Program;

class Predicates {

	public static Predicate and(final Predicate first, final Predicate second) {
		return new Predicate() {

			@Override
			public boolean appliesTo(Object obj, Program args) {
				return first.appliesTo(obj, args) && second.appliesTo(obj, args);
			}
			
		};
	}

	public static Predicate or(final Predicate first, final Predicate second) {
		return new Predicate() {

			@Override
			public boolean appliesTo(Object obj, Program args) {
				return first.appliesTo(obj, args) || second.appliesTo(obj, args);
			}
			
		};
	}

}
