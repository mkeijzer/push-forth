package org.arg.test;

import org.arg.pushforth.annotations.AdditionalArgumentChecks;
import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.dictionary.Predicate;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;

/*
 * Tests the AdditionalArgumentChecks functionality 
 */
public class TestDynamicPredicate {

	public static final Predicate positive = new Predicate() {
		@Override
		public boolean appliesTo(Object obj, Program args) {
			double dblval = ((Number)obj).doubleValue();
			return dblval >= 0;
		}
	};
	
	// only add positive numbers
	@AdditionalArgumentChecks(predicates={"positive", "positive"})
	@InstructionName(name="pos+")
	public static double addPos(Number a, Number b) {
		return a.doubleValue() + b.doubleValue();
	}
	
	Instruction addPos = InstructionFactory.make(TestDynamicPredicate.class, "addPos");
	
	
	@Test
	public void testPositives() {
	Program prog     = Programs.parse("[[pos+] a 1.0 -1.0 2.0]");
				
		Program expected = Programs.list(Programs.list(), "a", -1.0, 3.0);
		
		TestInstructions.test(prog, expected);
	
	}

}
