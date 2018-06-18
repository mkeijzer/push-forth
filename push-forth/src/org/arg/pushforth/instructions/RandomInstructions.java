package org.arg.pushforth.instructions;

import java.util.Random;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.Unpack;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Programs;

public class RandomInstructions {

	static {
		InstructionFactory.readMembers(RandomInstructions.class);
	}

	@InsDef(name = "rand")
	public static Random newRandom() {
		return new Random();
	}
	
	@InsDef(name="r.instruction")
	@Unpack
	public static Object ins(Random random, Instruction[] ins) {
		
		Object res = ins[random.nextInt(ins.length)];
		if (random.nextBoolean()) {
			res = Programs.list(res);
		}
		
		return Programs.list(random, ins, res);
	}	

	
}
