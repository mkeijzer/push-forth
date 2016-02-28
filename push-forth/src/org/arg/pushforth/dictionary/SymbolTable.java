package org.arg.pushforth.dictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.arg.pushforth.instructions.Arrays;
import org.arg.pushforth.instructions.Booleans;
import org.arg.pushforth.instructions.Code;
import org.arg.pushforth.instructions.Dictionary;
import org.arg.pushforth.instructions.Instruction;
import org.arg.pushforth.instructions.Instructions;
import org.arg.pushforth.instructions.Numbers;

/**
 * The Symbol Table contains all instructions that are globally accessible.
 * 
 * @author Maarten Keijzer
 *
 */

public class SymbolTable {
	
	// The global map of instructions
	private static Map<String, Instruction> instructionMap = new HashMap<String, Instruction>();

	// static class
	private SymbolTable() {}

	// Load the global symbol table
	static {
		Instructions.load();
		Booleans.load();
		Numbers.load();
		Code.load();
		Dictionary.load();
		Arrays.load();
	}
	
	public static Instruction getInstruction(String name) {
		return instructionMap.get(name);
	}

	public static Instruction[] getAllInstructions() {
		Collection<Instruction> ins = instructionMap.values();
		return ins.toArray(new Instruction[ins.size()]);
	}

	public static List<Instruction> getAllInstructionsButOne(String toExclude) {
		List<Instruction> ins = new ArrayList<Instruction>();
		
		for (Map.Entry<String, Instruction> entry : instructionMap.entrySet()) {
			if (!entry.getKey().equals(toExclude)) {
				ins.add(entry.getValue());
			}
		}
		
		return ins;
	}


	public static void put(String name, Instruction result) {
		instructionMap.put(name, result);		
	}

	public static Instruction get(String name) {
		return instructionMap.get(name);
	}
	
	public static Instruction safeget(String name) {
		Instruction res =  instructionMap.get(name);
		if (res == null) {
			throw new RuntimeException("Instruction with name " + name + " not found");
		}
		return res;
	}

	public static void remove(String name) {
		instructionMap.remove(name);
	}
	
	public static Instruction randomInstruction(Random random) {
		int choice = random.nextInt(instructionMap.size());
		for (Instruction ins : instructionMap.values()) {
			if (choice-- == 0) {
				return ins;
			}
		}
		throw new RuntimeException("Dictionary: should not happen");
	}

}


