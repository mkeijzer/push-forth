package org.arg.pushforth.dictionary;

/* 
 * Pushback policy specifies what to do when an instruction encounters an argument it cannot handle
 * Options are: delete it, push it behind you, or push it behind you followed by a swap operation.
 * 
 * The latter method allows you to do an operation deep in the stack, leaving the result there, and
 * pick up execution at a higher part of the stack
 * 
*/
public enum PushBackPolicy {

	Delete, Push, Swap;

	public static final PushBackPolicy defaultPolicy = Push;
	public static PushBackPolicy INVALID_ARGUMENT_POLICY = defaultPolicy;
	
}
