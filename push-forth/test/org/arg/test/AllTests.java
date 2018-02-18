package org.arg.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestParse.class,
		TestInstructions.class, TestInstructionFactory.class,
		TestIntegers.class, TestInstructionsWithDeletePolicy.class,
		TestDistance.class, TestOverloading.class, TestInterpreter.class
		
})

public class AllTests {
	// nothing
}
