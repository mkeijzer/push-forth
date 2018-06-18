package org.arg.pushforth.zzz.attic;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.program.Program;
import org.arg.pushforth.program.Programs;

public class Recursion {

	static {
		Instructions.load();
		InstructionFactory.registerMembers(Recursion.class);
	}
	
	@Unpack
	@InsDef(name="primrec")
	public static Program primrecint(Program action, Program ifzero, Long value) {
		if (value == 0) {
			return ifzero;
		}
		long v = value;
		if (v < 0) v++;
		else v--;
		Program ret = Programs.cons(v, ifzero, action, primrecint, value ,action);
		return ret;
	}
	public static Instruction primrecint = InstructionFactory.make(Instructions.class, "primrecint");

	@Unpack
	public static Program primreclist(Program action, Program ifzero, Program value) {
		if (value.isEmpty()) {
			return Programs.list(ifzero);
		}
		Program ret = Programs.cons(value.rest(), ifzero, action, primreclist, value.first(), action);
		return ret;
	}
	public static Instruction primreclist = InstructionFactory.make(Instructions.class, "primreclist");

	public static Instruction ifte = (Instruction) Programs.parse("[?]").first();
	// TODO
	@Unpack
	public static Program linrec(Program join, Program recurse, Program ifdone, Program test) {
		
		Program res = join;
		Program recursion = Programs.list(test, ifdone, recurse, join, linrec); // recursive call
		
		res = Programs.append(recursion, res);
		res = Programs.append(recurse, res);
		
		res = Programs.list(ifdone, res, ifte);
		res = Programs.append(test, res);

		return res;
	}	
	public static Instruction linrec = InstructionFactory.make(Instructions.class, "linrec");

	@Unpack
	public static Program binrec(Program join, Program recurse1, Program recurse2, Program ifdone, Program test) {
		
		Program res = join;
		Program recursion = Programs.list(test, ifdone, recurse2, recurse1, join, binrec); // recursive call
		
		res = Programs.append(recursion, res);
		res = Programs.append(recurse1, res);
		res = Programs.append(recursion, res);
		res = Programs.append(recurse2, res);
		
		res = Programs.list(ifdone, res, ifte);
		res = Programs.append(test, res);

		return res;
	}	
	public static Instruction binrec = InstructionFactory.make(Instructions.class, "binrec");

	
}
