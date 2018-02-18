package org.arg.pushforth.dictionary;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

class DynamicInstruction implements Instruction {

	final String name;
	final Method method;
	final Predicate[] args;
	final boolean unpack;
	final boolean isMemberFunc;

	// TODO, use method handles
//	final static java.lang.invoke.MethodHandles.Lookup lookup = MethodHandles.lookup();
//	final MethodHandle mh;

	public DynamicInstruction(Method method, String alias, Predicate[] args) {
		this.method = method;
		this.name = alias;
		this.args = args;
		this.isMemberFunc = Modifier.isStatic(method.getModifiers()) ? false : true;

		unpack = method.getAnnotation(Unpack.class) != null;

//		try {
//			mh = lookup.unreflect(method);
//		} catch (IllegalAccessException e) {
//			throw new RuntimeException(e);
//		}
	}

	public Predicate[] getArgs() {
		return args;
	}

	private int matchCount(Program data, int arg) {
		if (arg == args.length) {
			return arg;
		}
		if (data.isEmpty()) {
			return 0; // too few arguments, there is no way that this
						// instruction can be applied
		}

		Object first = data.first();
		data = data.rest();
		if (!args[arg].appliesTo(first, data)) {
			return arg;
		}

		return matchCount(data, arg + 1);
	}

	public int matchCount(Program data) {
		return matchCount(data, 0);
	}

	public Program apply(Program data) {
		Program objs = Programs.list();

		Program tmp = data;
		for (Predicate pred : args) {

			if (tmp.isEmpty()) {

				// Not enough arguments -- put the arguments back on the stack
				while (!objs.isEmpty()) {
					tmp = Programs.cons(objs.first(), tmp);
					objs = objs.rest();
				}
				return Programs.list(tmp);
			}

			Object obj = tmp.first();
			tmp = tmp.rest();

			boolean canUse = pred.appliesTo(obj, objs);

			if (!canUse) {
				// Wrong argument type -- put the arguments back on the stack
				// and swallow the offending argument
				Program ret = null;

				switch (PushBackPolicy.INVALID_ARGUMENT_POLICY) {
				case Swap: {
					ret = Programs.list(this, obj, Instructions.swap);
					break;
				}
				case Push: {
					ret = Programs.list(this, obj);
					break;
				}
				case Delete: {
					ret = Programs.list(this);
					break;
				}
				}
				// push the valid arguments back on the stack
				int argsUsed = args.length;
				while (!objs.isEmpty() && argsUsed-- >= 0) {
					tmp = Programs.cons(objs.first(), tmp);
					objs = objs.rest();
				}
				return Programs.cons(ret, tmp);
			}

			objs = Programs.cons(obj, objs);
		}

		Object obj = null;
		Object member = null;
		try {

			if (isMemberFunc) {
				member = objs.first();
				objs = objs.rest();
			}

			int len = args.length + (isMemberFunc ? -1 : 0);
			Object[] fargs = new Object[len];
			for (int i = len - 1; i >= 0; --i) {
				fargs[i] = objs.first();
				objs = objs.rest();
			}

			obj = method.invoke(member, fargs);
			
//			if (member == null) {
//				try {
//					System.out.println("Before " + obj);
//					obj = mh.invoke(fargs);
//					System.out.println("After " + obj);
//
//				} catch (Throwable e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}

		} catch (IllegalArgumentException e) {
			obj = e.getCause();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			obj = e.getCause();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			obj = e.getCause();
			// let exception be pushed on the stack
			// throw new RuntimeException(e);
		}

		// pushes 'this' back on the stack
		if (isMemberFunc) {
			tmp = Programs.cons(member, tmp);
		}

		if (unpack && obj instanceof Program) {
			return Programs.cons(obj, tmp);
		}

		if (obj != null) {
			return Programs.cons(Programs.list(obj), tmp);
		} else {
			return Programs.cons(Program.nil, tmp);
		}
	}

	public Object run(Program objs) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// replace this by a 'asm function pointer'
		int len = args.length + (isMemberFunc ? -1 : 0);
		Object[] fargs = new Object[len];
		for (int i = len - 1; i >= 0; --i) {
			fargs[i] = objs.first();
			objs = objs.rest();
		}

		Object member = (isMemberFunc ? objs.first() : null);

		return method.invoke(member, fargs);
	}

	@Override
	public String toString() {
		return name;
	}

	public Method getMethod() {
		return method;
	}

}