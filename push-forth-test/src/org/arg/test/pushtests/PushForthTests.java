package org.arg.test.pushtests;

import java.lang.reflect.Method;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Assert;
import org.junit.Test;

/*
 * Runs the tests in the annotations of individual instructions
 */
public class PushForthTests {

	static {
		InstructionFactory.readMembers(PushForthTests.class);
	}

	public static Program runTest(Program prog, boolean step) {
		Program old = prog;
		int i = 0;

		if (step) {
			System.out.println(i + ": " + prog);
		}

		while (!(prog = Instructions.reduce(prog)).isEmpty()) {
			if (old == prog)
				break;
			old = prog;
			i++;
			if (step) {
				System.out.println(i + ": " + prog);
			}
		}

		return prog;
	}

	@Test
	public void runTests() {

		for (Method m : InstructionFactory.getMethodList()) {

			InstructionTest ann = m.getAnnotation(InstructionTest.class);

			if (ann != null) {
				for (String t : ann.tests()) {
					System.out.println("Running test " + t + " for method "
							+ m.getName() + " defined in "
							+ m.getDeclaringClass().getName());
					Program prog = Programs.parse(t);
					Program res = runTest(prog, false);

					Object result = res.rest().first();
					if (result == null || !(result instanceof Boolean)
							|| ((Boolean) result) != true
							|| res.rest().rest() != Program.nil) {
						runTest(prog, true);
						Assert.fail();
					}

				}
			} else {
				System.err.println("Method " + m.getName() + " defined on " + m.getDeclaringClass().getName() + " has no associated tests.");
			}
		}
	}

	// do some tests on data members
	long val = 0;

	@InstructionName(name = "pft.nw")
	public static PushForthTests nw() {
		return new PushForthTests();
	}

	@InstructionName(name = "pft.add")
	public Long testAdd1(Long value) {
		val += value;
		return val;
	}

	@InstructionTest(tests = {
			"[[pft.nw 3 pft.add pop 2 pft.add pop pft.get swap pop 5 =]]",
			"[[+ pop 3 =] 2 a 1]",
			"[[+ 2 =] 2]"}) // tests if we skip 'a', and not 2 (for overloaded instructions)
	@InstructionName(name = "pft.get")
	public Long get() {
		return val;
	}

	@Override
	public String toString() {
		return "pft(" + val + ")";
	}

}
