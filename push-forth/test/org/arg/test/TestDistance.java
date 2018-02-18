package org.arg.test;

import static org.junit.Assert.fail;

import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;


public class TestDistance {

	@Test
	public void testDistance() {
		Program a = Programs.list(1, 2, 3, 4);
		Program b = Programs.list(1, 2, 3, 4);
		
		if (Programs.distance(a,b, 210000) != 0) {
			fail("Expected zero distance, got " + Programs.distance(a,b, 2100000));
		}
	}

	@Test
	public void testDistance2() {
		Program a = Programs.list(1, 2, 3, 4);
		Program b = Programs.list(1, 2, 3);
		
		if (Programs.distance(a,b, 2100000) == 0) {
			fail("Expected non-zero distance, got " + Programs.distance(a,b, 2100000));
		}
		
		if (Programs.distance(b,a, 2100000) != Programs.distance(a,b, 210000)) {
			fail("Expected symmetric distance, got " + Programs.distance(a,b, 2100000));
		}		
	}

	@Test
	public void testDistance3() {
		Program a = Programs.list(1, 2, Programs.list(3));
		Program b = Programs.list(1, 2, 3);
		
		if (Programs.distance(a,b, 2100000) == 0) {
			fail("Expected non-zero distance, got " + Programs.distance(a,b, 2100000));
		}

		if (Programs.flatDistance(a,b, 2100000) != 0) {
			fail("Expected zero distance, got " + Programs.flatDistance(a,b, 2100000));
		}

	}

	@Test
	public void testDistance4() {
		Program a = Programs.list(1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 1, 1, 1, 1, 1, 1);
		Program b = Programs.list(1);
		
		System.out.println(Programs.distance(a,b,10000));
		
	}
}


