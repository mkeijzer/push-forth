package org.arg.pushforth.dictionary;

import java.util.List;

import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

class OverloadedInstruction implements Instruction {
	List<DynamicInstruction> instructions;
	final String name;

	public OverloadedInstruction(String name,
			List<DynamicInstruction> instructions) {
		this.instructions = instructions;
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

	public Program apply2(Program prog) {
		if (prog.isEmpty())
			return prog;

		Object first = prog.first();

		for (DynamicInstruction ins : instructions) {
			Predicate[] args = ins.getArgs();
			if (args == null || args.length == 0) {
				return ins.apply(prog);
			}
			Predicate pred = args[0];

			// first one wins
			if (pred.appliesTo(first, Programs.list())) {
				return ins.apply(prog);
			}
		}

		prog = prog.rest();

		switch (PushBackPolicy.INVALID_ARGUMENT_POLICY) {
		case Swap:
			return Programs.cons(Programs.list(this, first, Instructions.swap),
					prog);

		case Push:
			return Programs.cons(Programs.list(this, first), prog);
		case Delete:
			return Programs.cons(Programs.list(), prog);
		}

		throw new RuntimeException("Invalid return policy specified");
	}

	/**
	 * 
	 * 
	 * @see org.arg.pushforth.instructions.Instruction#apply(org.arg.pushforth.program.Program)
	 */
	@Override
	public Program apply(Program prog) {
		if (prog.isEmpty())
			return prog;

		int maxMatch = 0;

		for (DynamicInstruction ins : instructions) {

			int match = ins.matchCount(prog);
			
			if (match == ins.getArgs().length) {
				return ins.apply(prog);
			}
			maxMatch = Math.max(match, maxMatch);
		}

		// try again
		Program stack = Program.nil;
		Object first = prog.first();
		prog = prog.rest();

		for (;;maxMatch--) {
			if (maxMatch == 0) {

				// unroll stack
				while (!stack.isEmpty()) {
					prog = Programs.cons(stack.first(), prog);
					stack = stack.rest();
				}

				switch (PushBackPolicy.INVALID_ARGUMENT_POLICY) {
				case Swap:
					return Programs
							.cons(Programs.list(this, first, Instructions.swap),
									prog);

				case Push:
					return Programs.cons(Programs.list(this, first), prog);
				case Delete:
					return Programs.cons(Programs.list(), prog);
				}

				throw new RuntimeException("Invalid return policy specified");
			} else {
				stack = Programs.cons(first, stack);
				first = prog.first();
				prog = prog.rest();
			}
		}
	}

}