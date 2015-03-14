package org.arg.test;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Random;

import org.arg.pushforth.io.RandomProgramDispenser;
import org.arg.pushforth.program.Program;
import org.junit.Test;

public class TestEvolve {
	
	Random random = new Random();

	@Test
	public void dummy() {}
	
	//@Test
	public void notest() {
		
		RandomProgramDispenser rpd = new RandomProgramDispenser(20);
		
		for (int i = 0; i < 20; ++i) {
			Program prog = rpd.getProgram(random);
			System.out.println(prog);
		}
		
		fail("Not yet implemented");
	}
		
}
