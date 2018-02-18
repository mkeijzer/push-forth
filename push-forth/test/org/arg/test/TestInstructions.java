package org.arg.test;
import static org.arg.pushforth.program.Programs.list;
import static org.junit.Assert.fail;

import org.arg.pushforth.dictionary.PushBackPolicy;
import org.arg.pushforth.dictionary.SymbolTable;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;

public class TestInstructions extends Instructions {
	
	Program nil = Program.nil;
	
	public TestInstructions() {
		PushBackPolicy.INVALID_ARGUMENT_POLICY = PushBackPolicy.Push;
	}
	
	public static void test(Program prog, Program expected) {
		test(prog, expected, false);
	}

	public static void test(Program prog, Program expected, boolean print) {
		test(prog, expected, print, 150);
	}
	public static void test(Program prog, Program expected, boolean print, int max) {
		
		if (print) System.out.println("Initial: " + prog);
		
		for (int i = 0; i < max; ++i) {
			Program nw = reduce(prog);
			//Program nw = puf.apply(prog);
			
			if (nw == prog) break;
			prog = nw;
			if (print) System.out.println("" + (i+1) + ": " + Programs.printJoy(prog)); // + " -- " + expected);
		}

		
		if (!prog.toString().equals(expected.toString())) {
			fail("Expected " + expected + " got " + prog);
		}
	}
	
	@Test
	public void testExec() {
		Program prog     = list(list(1,2,3,4));
		Program expected = list(nil, 4,3,2,1); 
		test(prog, expected);
	}
	
	@Test
	public void testNil() {
		Program prog     = list();
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
		Program prog = list( list(dup), 1, 2 );
		Program expected = list(nil, 1, 1, 2);
		test(prog, expected);

		prog = list( list(dup) );
		expected = list(nil);
		test(prog, expected);
	}

	
	@Test
	public void testSwap() {
		Program prog = list( list(swap), 1, 2 );
		Program expected = list(nil, 2, 1);
		test(prog, expected);
		
		prog = list( list(swap), 1 );
		expected = list(nil, 1);
		test(prog, expected);
		
		prog = list( list(swap, "halt"), 1, list(1));
		expected = list(nil, "halt", list(1), 1);
		test(prog, expected);
		
		
	}

	@Test
	public void testSwapList() {
		Program prog = list( list(swap), "a", Programs.list(1, 2) );
		Program expected = list(nil, Programs.list(1,2), "a");
		test(prog, expected);
	}

	Instruction cons = SymbolTable.get("cons");
	
	@Test
	public void testCons() {
		Program prog = list( list(cons, "halt"), 1, list() );
		Program expected = list(nil, "halt", list(1) );
		test(prog, expected);
		
		prog = list( list(cons), 1, 2, list(3, 4));
		expected = list(nil, 2, list(1,3,4));
		test(prog, expected);	

		
		prog = list( list(cons), list(1, 2), list(3, 4));
		expected = list(nil, list(list(1,2),3,4));
		test(prog, expected);	

	}
	
	@Test
	public void testI() {
		Program prog = list( list(SymbolTable.get("i")), 1, list(2, 3) );
		Program expected = list(nil, 1, 3, 2 );
		test(prog, expected);		
	}
		
	@Test
	public void testFactory() {
		Program prog = list( list(cons, 'h'), list(1) );
		Program expected = list(nil, 'h', list(1));
		test(prog, expected);		
	}


	Instruction i = SymbolTable.get("i");
	@Test
	public void testSingleInstruction() {
		Program prog = list( list(i), 1, list(2) );
		Program expected = list(nil, 1, 2);
		test(prog, expected);
	}
		
//	@Test
//	public void testDip() {
//		//Program cat = Programs.parse("[[i] dip i] cons cons");
//		
//		Program prog = list( list(dip), list('a', dup), list('b'));
//		Program expected = list(list(), list('b'), 'a', 'a');
//		
//		test(prog, expected);
//	}
//
		
	@Test
	public void testPop() {
		Program pop = Programs.parse("[[pop] A [dup B]]");
		Program exp = Programs.parse("[ [] [dup B] ]");
		test(pop, exp);
	}
			
	@Test
	public void testIFTE() {
		Program p = Programs.parse("[ [true [x dup] [y dup] ?] ]");
		Program e = Programs.parse("[ [] x x ]");
		test(p, e);
		p = Programs.parse("[ [false [x dup] [y dup] ?] ]");
		e = Programs.parse("[ [] y y ]");
		test(p, e);
	}
	
		
//	public static Double func(Double x, Long y) {
//		return x + y;
//	}
//	@Test
//	public void testOrder() {
//		InstructionFactory.make(TestInstructions.class, "func");
//		Program p = Programs.parse("[ [func] 1.0 2 ]");
//		Program e = Programs.parse("[ [] 3.0 ]");
//		test(p, e);
//		InstructionFactory.make(TestInstructions.class, "func");
//		
//		p = Programs.parse("[ [func] 1 2.0 ]");
//		e = Programs.parse("[ [] 3.0 ]");
//		test(p, e);
//		
//	}
	
}
