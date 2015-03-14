package org.arg.test;

import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.dictionary.PushBackPolicy;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;

public class TestInstructionFactory {
	
	public TestInstructionFactory() {
		PushBackPolicy.INVALID_ARGUMENT_POLICY = PushBackPolicy.Push;
	}
	
	public static Double add(Double a, Double b) {
		return a + b;
	}
	
	public static Integer add(Integer a, Integer b) {
		return a + b;
	}
	
	@Test
	public void testAdd() {
		Instruction ins = InstructionFactory.make(TestInstructionFactory.class, "add");
		
		Program prog = Programs.list(1.0, 2.0, ins);
		
		Program expected = Programs.list(Program.nil, 3.0);
		
		TestInstructions.test(Programs.list(prog), expected);		
	}
	
	@Test
	public void testInterspersedAdd() {
		Instruction ins = InstructionFactory.make(TestInstructionFactory.class, "add");
		
		Program prog = Programs.list(3.0, "a", 4.0, ins);
		
		Program expected = Programs.list(Program.nil, "a", 7.0);
		
		TestInstructions.test(Programs.list(prog), expected);
		
	}

	public Double memberAdd(Double x, Integer y) {
		return x + y;
	}
	
	@Test 
	public void testMemberFunction() {
		Instruction add = InstructionFactory.make(TestInstructionFactory.class, "memberAdd");
		
		Program prog = Programs.list(this, 1, 2.0, add);
		Program expected = Programs.list(Programs.list(), 3.0, this);
		
		TestInstructions.test(Programs.list(prog), expected);
	}
	
	@Override
	public String toString() {
		return "testclass";
	}


	@Unpack
	public static Program testMulti(Integer a, Integer b) {
		return Programs.list(a+b,b);
	}
	
	@Test
	public void testMultipleOutputs() {
		Instruction ins = InstructionFactory.make(TestInstructionFactory.class, "testMulti");
		
		Program prog = Programs.list(1, "a", 2, ins);
		
		Program expected = Programs.list(Program.nil, "a", 1, 3);
		
		TestInstructions.test(Programs.list(prog), expected);				
				
	}
	
}
