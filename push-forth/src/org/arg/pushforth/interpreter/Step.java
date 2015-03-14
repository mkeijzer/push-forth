package org.arg.pushforth.interpreter;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.instructions.Booleans;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.instructions.Numbers;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;


public class Step {

	String generate = "[ [7 [cons] [append] [primrec] [unit] append append primrec cons ] ]";

	static {
		Instructions.load();
		Numbers.load();
		Booleans.load();
	}
	
	static Program fold = Programs.parse("[> [first] + primrec pop]");
	
	@InstructionName(name = "fold")
	@Unpack
	public static Program fold() {
		return fold;
	}
	
	public static void main(String[] args) {
		InstructionFactory.readMembers(Step.class);
		
		String[] a = {"[ [4 [1] nil primrec pop cons pop pop * list? + [*] bool? i + / * primrec << + >> && 1 *]]"};
		Main.run(a, true);
	}

}