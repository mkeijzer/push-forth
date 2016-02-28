package org.arg.pushforth.instructions;

import static org.arg.pushforth.program.Programs.cons;
import static org.arg.pushforth.program.Programs.list;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Instructions {

	static {
		InstructionFactory.readMembers(Instructions.class);
	}

	public static void load() {
	}

	@InstructionTest(tests = { "[[eval eval eval cdr car 2 =] [[1 1 +]] ]",
			"[[ [swap eval dup car nil? [swap pop] [swap dup i] ?] dup i [[] 2] =] [[1 1 +]] ]"  })
	@InstructionName(name = "eval")
	public static Program reduce(Program prog) {

		if (prog.isEmpty()) {
			// halt
			return list(list());
		}

		// first element is supposedly a program (code)
		Object first = prog.first();

		if (!(first instanceof Program)) {
			// create code as first element and halt
			return Programs.cons(Programs.list(), prog);
		}

		Program code = (Program) first;

		if (code.isEmpty()) {
			// program is halted
			return prog;
		}

		// data stack
		Program data = prog.rest();

		// get first element of the program
		Object obj = code.first();
		code = code.rest();

		if (obj instanceof Instruction) {
			Instruction ins = (Instruction) obj;

			// try {
			// we expect something of the form [ [return values] rest of the
			// stack]
			final Program result = ins.apply(data);

			if (result.isEmpty()) {
				return cons(code, result);
			}

			Object ret = result.first();

			if (!(ret instanceof Program)) { // got something of the form [x y
												// z] , so no return value
				return cons(code, result);
			}

			if (!((Program) ret).isEmpty()) {
				code = append((Program) ret, code);
			}
			data = result.rest();

			// } catch (Throwable t) {
			// throw new RuntimeException(t);
			// code = cons(t, code);
			// }

		} else { // not an instruction, push it on the stack
			data = cons(obj, data);
		}

		// Skip data elements
		while (!code.isEmpty()) {
			obj = code.first();
			if (obj instanceof Instruction) {
				break;
			}
			code = code.rest();
			data = cons(obj, data);
		}

		return cons(code, data);
	}
	
	@InstructionTest(tests = { "[[dup a = swap a = &&] a]" })
	@InstructionName(name = "dup")
	@Unpack
	public static Program dupfunc(Object a) {
		return Programs.list(a, a);
	}

	@InstructionTest(tests = {"[[swap pop a =] a b ]"})
	@InstructionName(name="swap")
	@Unpack
	public static Program swapfunc(Object a, Object b) {
		return Programs.list(a, b);
	}

	public static Instruction swap = InstructionFactory.make(
			Instructions.class, "swapfunc");

	@InstructionTest(tests={"[[[1] 2 cons [2 1] =]]"})
	@InstructionName(name="cons")
	public static Program consFunc(Object obj, Program prog) {
		return Programs.cons(obj, prog);
	}

	@InstructionTest(tests={"[[true i]]]"})
	@InstructionName(name = "i")
	@Unpack
	public static Program ifunc(Program prog) {
		return prog;
	}

	@InstructionTest(tests={"[[true [x] [y] ? x =]]", "[[false [x] [y] ? y =]]"})
	@InstructionName(name = "?")
	@Unpack
	public static Program ifte(Program y, Program x, Boolean b) {
		return b ? x : y;
	}

	@InstructionTest(tests={"[[true false pop]]"})
	@InstructionName(name = "pop")
	public static void pop(Object o) {
	}

	@InstructionTest(tests={"[[nil isnil?]]"})
	@InstructionName(name = "isnil?")
	public static boolean isEmpty(Program o) {
		return o.isEmpty();
	}
	
	@InstructionTest(tests={"[[nil isnil?]]"})
	@InstructionName(name = "nil")
	public static Program nil() {
		return Program.nil;
	}

	@InstructionTest(tests={"[[[1] 2 cons car 2 =]]"})
	@InstructionName(name = "car")
	public static Object first(Program prog) {
		return prog.isEmpty() ? Program.nil : prog.first();
	}

	
	@InstructionTest(tests={"[[[1] 2 cons cdr [1] =]]"})
	@InstructionName(name = "cdr")
	public static Program rest(Program prog) {
		return prog.isEmpty() ? Program.nil : prog.rest();
	}

	@InstructionTest(tests={"[[[1] [2] append car 2 =]]"})
	@InstructionName(name = "append")
	public static Program append(Program x, Program y) {
		return Programs.append(x, y);
	}

}
