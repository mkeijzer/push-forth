package org.arg.test;

import static org.junit.Assert.*;

import org.arg.pushforth.program.Programs;
import org.junit.Test;

public class TestArrays {

	@Test
	public void testNth() {
		
		Integer[] ints = new Integer[]{1,2,3};
		
		Programs.list(ints, Programs.parse("nth"));
		
		
		fail("Not yet implemented");
	}

}
