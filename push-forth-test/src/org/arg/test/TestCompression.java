package org.arg.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;
import org.junit.Test;


public class TestCompression {

	@Test
	public void testCompression() {
		
		Program a = Programs.parse("[a b c d]");
		Program b = Programs.parse("[a b c d]");
		
		if (a == b) {
			Assert.fail();
		}
		
		Map<Program, Program> map = new HashMap<Program, Program>();
		a = Programs.compress(map, a);
		b = Programs.compress(map, b);
		
		if (a != b) {
			Assert.fail();
		}
		
	}
	
}
