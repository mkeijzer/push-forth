package org.arg.test;

import static org.arg.pushforth.program.Programs.list;
import static org.junit.Assert.fail;

import org.arg.pushforth.dictionary.SymbolTable;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;

public class TestParse extends Instructions {

	@Test
	public void test1() {
		Program prog = list(1);
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}
	}

	@Test
	public void test2() {
		Program prog = list(1, 2, 4);
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}	
	}

	@Test
	public void test3() {
		Program prog = list(1, list(2, 3), 4);
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}	
	}

	@Test
	public void test4() {
		Program prog = list(1, list(2, "alabama"), 4);
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}	
	}

	@Test
	public void test5() {
		Program prog = list("I have spaces and also braces ( ");
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}	
	}

	Instruction cons = SymbolTable.get("cons");
	@Test
	public void test6() {
		Program prog = list(cons, SymbolTable.get("nil"));
		
		String s = prog.toString();
		Program next = Programs.parse(s);
		
		String r = next.toString();
		
		if (!s.equals(r)) {
			fail("Expected " + s + " but received " + r);
		}	
	}
	
	@Test
	public void testIntegerParse() {		
		Program prog = Programs.parse("[1]");
		if (! (prog.first() instanceof Long) ) {
			fail("Expected Long, received " + prog.first().getClass().getCanonicalName());
		}		
		
		prog = Programs.parse("[1L]");
		if ( (prog.first() instanceof Long) ) {
			fail("Expected a string, received " + prog.first().getClass().getCanonicalName());
		}		
	}
	
	@Test
	public void testDouble() {		
		Program prog = Programs.parse("[1.0]");
		if (! (prog.first() instanceof Double) ) {
			fail("Expected Double, received " + prog.first().getClass().getCanonicalName());
		}		
		
		prog = Programs.parse("[1.4e+223]");
		if (! (prog.first() instanceof Double) ) {
			fail("Expected Double, received " + prog.first().getClass().getCanonicalName());
		}		
	}		

	@Test
	public void testNoBrackets() {		
		try {
			Program prog = Programs.parse("1 1 2");
			fail("Should not be able to parse non-balanced programs");
		} catch (Exception e) { // all is well
		}
		
	}		
	
	@Test
	public void testComment() {		
		Program prog = Programs.parse("[1 1 # comment \n 2]");
	
		if (!prog.toString().equals("[1 1 2]")) {
			fail("Expected [1 1 2], got " + prog);
		}
		
	}		
	
	@Test
	public void testNumbers() {		
		Program prog = Programs.parse("[1.0]");
	
		if (!prog.toString().equals("[1.0]")) {
			fail("Expected [1.0], got " + prog);
		}
		
	}		
	
}

