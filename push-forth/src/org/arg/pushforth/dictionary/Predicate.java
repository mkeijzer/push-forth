package org.arg.pushforth.dictionary;

import java.util.HashMap;
import java.util.Map;

import org.arg.pushforth.program.Program;

public interface Predicate {
	boolean appliesTo(Object obj, Program args);
	
	public static final class TypePredicate implements Predicate {
		
		public final static Map<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
		static {
		    map.put(boolean.class, Boolean.class);
		    map.put(byte.class, Long.class);
		    map.put(short.class, Long.class);
		    map.put(char.class, Long.class);
		    map.put(int.class, Long.class);
		    map.put(long.class, Long.class);
		    map.put(float.class, Double.class);
		    map.put(double.class, Double.class);
		    map.put(void.class, Void.class);
		}

		
		final Class<?> clazz;
		
		public TypePredicate(Class<?> clazz) {
			super();
			this.clazz = clazz;
		}

		@Override
		public boolean appliesTo(Object obj, Program args) {
			Class clazz = obj.getClass();
			if (clazz.isPrimitive()) {
				//clazz = map.get(clazz);
			}
			return this.clazz.isAssignableFrom(clazz);
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
