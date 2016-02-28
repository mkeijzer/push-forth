package org.arg.pushforth.instructions;

import java.util.HashMap;
import java.util.Map;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;

/**
 * Implements a 'Dictionary' type -- an associative array
 * @author MaartenKeijzer
 *
 */
public class Dictionary {

	static {
		InstructionFactory.readMembers(Dictionary.class);
	}
	public static void load() {}
	
	private final Map<Object, Object> map = new HashMap<Object, Object>();
	
	public Dictionary() {
	}
	
	private Dictionary(Map<Object, Object> map) {
		this.map.putAll(map);
	}
	
	@InstructionName(name="{}")
	public static Dictionary nw() {
		return new Dictionary();
	}
	
	@InstructionName(name="{}.put")
	public Object put(Object key, Object value) {
		map.put(key, value);
		return key;
	}
	
	@InstructionTest(tests={"[[{} hi 2 {}.put {}.get swap pop hi =]]"})
	@InstructionName(name="{}.get") 
	public Object get(Object key) {
		Object res = map.get(key);
		if (res == null) {
			res = Program.nil;
		}
		return res;
	}

	@InstructionTest(tests={"[[{} 1 2 {}.put pop {}.clone 3 2 {}.put {}.get 3 = swap pop swap pop]]"})
	@InstructionName(name="{}.clone") 
	public Object clone() {
		return new Dictionary(map);
	}

	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			builder.append(entry.getKey());
			builder.append(":");
			builder.append(entry.getValue());
			builder.append(",");
		}
		
		builder.append("}");
		return builder.toString();
	}
	
}
