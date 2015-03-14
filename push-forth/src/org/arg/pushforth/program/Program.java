package org.arg.pushforth.program;

/**
 * The Program interface, containst nil and accessors
 * 
 * @author Maarten Keijzer
 *
 */
public interface Program {

	public static Program nil = new ArrayProgram();
	
	Object first();
	Program rest();

	boolean isEmpty();	
	
}
