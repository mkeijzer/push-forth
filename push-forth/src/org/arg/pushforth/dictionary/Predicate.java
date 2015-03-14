package org.arg.pushforth.dictionary;

import org.arg.pushforth.program.Program;

interface Predicate {
	boolean appliesTo(Object obj, Program args);

	public static final class TypePredicate implements Predicate {

		final Class<?> clazz;
		
		public TypePredicate(Class<?> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public boolean appliesTo(Object obj, Program args) {
			return clazz.isAssignableFrom(obj.getClass());
		}
	}
	
	public static final class ArrayPredicate implements Predicate {
		final TypePredicate[] preds;
		
		public ArrayPredicate(TypePredicate...preds) {
			this.preds = preds;
		}
		
		private boolean isAllowed(Class<?> arg, TypePredicate[] left) {
			for (int i = 0; i < left.length; ++i) {
				if (left[i] != null && left[i].clazz.isAssignableFrom(arg)) {
					left[i] = null;
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean appliesTo(Object obj, Program args) {
			TypePredicate[] left = preds.clone();
			while (!args.isEmpty()) {
				Class<?> first = args.first().getClass();
				isAllowed(first, left);
				args = args.rest();
			}
			
			return isAllowed(obj.getClass(), left);
		}
		
	}
	
}
