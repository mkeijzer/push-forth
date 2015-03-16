package org.arg.test;

import static org.arg.pushforth.program.Programs.list;
import static org.junit.Assert.fail;

import org.arg.pushforth.dictionary.PushBackPolicy;
import org.arg.pushforth.dictionary.SymbolTable;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestInstructionsWithDeletePolicy extends Instructions {

	Program nil = Program.nil;

	public TestInstructionsWithDeletePolicy() {
	}

	@BeforeClass
	public static void setPolicy() {
		PushBackPolicy.INVALID_ARGUMENT_POLICY = PushBackPolicy.Delete;
	}

	@AfterClass
	public static void resetPolicy() {
		PushBackPolicy.INVALID_ARGUMENT_POLICY = PushBackPolicy.defaultPolicy;
	}

	public static void test(Program prog, Program expected) {
		test(prog, expected, false);
	}

	public static void test(Program prog, Program expected, boolean print) {

		if (print)
			System.out.println("Initial" + prog);

		for (int i = 0; i < 100; ++i) {
			Program nw = Instructions.reduce(prog);
			// Program nw = puf.apply(prog);

			if (nw == prog)
				break;
			prog = nw;
			if (print)
				System.out.println("Step " + (i + 1) + " " + prog);
		}

		if (!prog.toString().equals(expected.toString())) {
			fail("Expected " + expected + " got " + prog);
		}
	}

	@Test
	public void testExec() {
		Program prog = list(list(1, 2, 3, 4));
		Program expected = list(nil, 4, 3, 2, 1);
		test(prog, expected);
	}

	@Test
	public void testNil() {
		Program prog = list();
		Program expected = list(nil);
		test(prog, expected);
	}

	@Test
	public void testSteady() {
		Program prog = list(nil, 1, 2);
		test(prog, prog);

		prog = list(nil, 1);
		test(prog, prog);

	}

	Instruction dup = SymbolTable.get("dup");

	@Test
	public void testDup() {
		Program prog = list(list(dup), 1, 2);
		Program expected = list(nil, 1, 1, 2);
		test(prog, expected);

		prog = list(list(dup));
		expected = list(nil);
		test(prog, expected);
	}

	@Test
	public void testSwap() {
		Program prog = list(list(swap), 1, 2);
		Program expected = list(nil, 2, 1);
		test(prog, expected);

		prog = list(list(swap), 1);
		expected = list(nil);
		test(prog, expected);

		prog = list(list(swap, "halt"), 1, list(1));
		expected = list(nil, "halt", list(1), 1);
		test(prog, expected);

	}

	@Test
	public void testSwapList() {
		Program prog = list(list(swap), "a", Programs.list(1, 2));
		Program expected = list(nil, Programs.list(1, 2), "a");
		test(prog, expected);
	}

	@Test
	public void testCons() {

		Program prog = list(list(cons, "halt"), 1, list());
		Program expected = list(nil, "halt", list(1));
		test(prog, expected);

		prog = list(list(cons), 1, 2, list(3, 4));
		expected = list(nil, list(1, 3, 4));
		test(prog, expected);

		prog = list(list(cons), list(1, 2), list(3, 4));
		expected = list(nil, list(list(1, 2), 3, 4));
		test(prog, expected);

	}

	@Test
	public void testI() {
		Instruction i = (Instruction) Programs.parseAtom("i"); 
		Program prog = list(list(i), 1, list(2, 3));
		Program expected = list(nil, 3, 2);
		test(prog, expected);
	}

	Instruction cons = SymbolTable.get("cons");

	@Test
	public void testFactory() {
		Program prog = list(list(cons, 'h'), list(1));
		Program expected = list(nil, 'h', list(1));
		test(prog, expected);
	}

	@Test
	public void testDupCons() {
		Program prog = list(list(list(dup, cons), dup, cons));
		Program expected = Programs.cons(nil, prog);
		test(prog, expected);
	}

}
