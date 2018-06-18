package org.arg.pushforth.interpreter;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.instructions.Booleans;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.instructions.Numbers;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;


public class Step {

	String generate = "[ [7 [cons] [append] [primrec] [unit] append append primrec cons ] ]";

	static String g = "[[i false false false instructions cdr] [[] [] false]]";  
	//"3857: [[] [false false instructions gen] [false] [[] false] [[]]]"
	
	
	static {
		Instructions.load();
		Numbers.load();
		Booleans.load();
	}
	
	static Program fold = Programs.parse("[> [first] + primrec pop]");
	
	@InsDef(name = "fold")
	@Unpack
	public static Program fold() {
		return fold;
	}
	
	public static void main(String[] args) {
		InstructionFactory.readMembers(Step.class);
		
		//String[] a = {"[ [4 [1] nil primrec pop cons pop pop * list? + [*] bool? i + / * primrec << + >> && 1 *]]"};
		String a = "[[dup i] [dup i] [[dup i] dup i]]";
		a = "[[' car =] car]";
		a = g;
		Main.run(a, true);
	}

}
