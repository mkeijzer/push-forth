package org.arg.test;

import org.arg.test.pushtests.PushForthTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ TestParse.class, TestEvolve.class,
		TestInstructions.class, TestInstructionFactory.class,
		TestIntegers.class, TestInstructionsWithDeletePolicy.class,
		TestDistance.class, TestOverloading.class, TestInterpreter.class, 
		PushForthTests.class
		
})

public class AllTests {
	// nothing
}
