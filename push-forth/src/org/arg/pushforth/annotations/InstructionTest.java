package org.arg.pushforth.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies an array of tests for a particular instruction. These tests are picked up elsewhere.
 * 
 * The tests are valid push programs, and the test only passes if, after execution, the value 'true'
 * is the only value found on the data stack.
 * 
 * @author Maarten Keijzer
 *
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InstructionTest {
	public String[] tests();
}
