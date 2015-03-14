package org.arg.test;

import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;


public class TestOverloading {
	
	@Test
	public void testOverloading() {
		Instruction ins = InstructionFactory.make(TestInstructionFactory.class, "add");
		
		Program prog = Programs.list(1.0, 2.0, ins);
		
		Program expected = Programs.list(Program.nil, 3.0);
		
		TestInstructions.test(Programs.list(prog), expected);		
		
		prog = Programs.list(1, 2, ins);
		
		expected = Programs.list(Program.nil, 3);
		
		TestInstructions.test(Programs.list(prog), expected);		
	}

	public static Double addx(Number a, Double b) {
		return a.doubleValue() + b;
	}
	
	public static Integer addx(Integer a, Integer b) {
		return a + b;
	}

	@Test
	public void testOverloadingWithJoinedFirstArgument() {
		Instruction ins = InstructionFactory.make(TestOverloading.class, "addx");
		
		Program prog = Programs.list(1.0, 2, ins);
		
		Program expected = Programs.list(Program.nil, 3.0);
		
		TestInstructions.test(Programs.list(prog), expected);		
		
		prog = Programs.list(1.0, 2, ins);
		
		expected = Programs.list(Program.nil, 3.0);
		
		TestInstructions.test(Programs.list(prog), expected);		
	}
		
}
