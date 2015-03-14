package org.arg.test;

import org.arg.pushforth.dictionary.SymbolTable;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Numbers;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;


public class TestIntegers {

	static {
		Numbers.load();
	}
	
	
	@Test 
	public void testDivByZero() {
		
		Program prog     = Programs.parse("[[0 2 /]]");
		
		Exception exc = null;
		try {
			int x = 1/0;
		} catch (ArithmeticException e) {
			exc = e;
		}
		
		Program expected = Programs.list(Programs.list(), exc);
		
		TestInstructions.test(prog, expected);
				
	}

	@Test 
	public void testRemByZero() {
		
		Program prog     = Programs.parse("[[0 2 %]]");
		
		Exception exc = null;
		try {
			int x = 1/0;
		} catch (ArithmeticException e) {
			exc = e;
		}
		
		Program expected = Programs.list(Programs.list(), exc);
		
		TestInstructions.test(prog, expected);
				
	}

	@Test 
	public void testDivNotByZero() {
		
		Program prog     = Programs.parse("[[2 0 /]]");
				
		Program expected = Programs.list(Programs.list(), 0);
		
		TestInstructions.test(prog, expected);
				
	}

	@Test 
	public void testOne() {		
		Instruction one = SymbolTable.get("1");
		
		Program prog     = Programs.list(Programs.list(one));				
		Program expected = Programs.list(Programs.list(), 1);
		
		TestInstructions.test(prog, expected);
				
	}

}
