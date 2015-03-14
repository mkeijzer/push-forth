package org.arg.test;

import org.arg.pushforth.interpreter.Main;
import org.junit.Test;

public class TestInterpreter {

	@Test
	public void testA() {
		String[] prog = {"[[Numbers import 1 1 +]]"};
		Main.main(prog);
	}
	
}
