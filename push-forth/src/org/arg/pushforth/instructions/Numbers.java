package org.arg.pushforth.instructions;

import org.arg.pushforth.annotations.InstructionName;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;

public class Numbers {

	static {
		Instructions.load();
		InstructionFactory.readMembers(Numbers.class);
	}

	public static void load() {}
	
	@InstructionTest(tests = { "[[num?] 1]", "[[num?] 2.3]" })
	@InstructionName(name = "num?")
	public static Boolean isnum(Object a) {
		return a instanceof Number;
	}

	@InstructionTest(tests = { "[[1 1 + 2 =]]", "[[+ 2 =] 1 1.1]]" })
	@InstructionName(name = "+")
	public static Long add(Long a, Number b) {
		return a + b.longValue();
	}

	@InstructionName(name = "-")
	public static Long min(Long a) {
		return -a;
	}

	@InstructionName(name = "*")
	public static Long mul(Long a, Number b) {
		return a * b.longValue();
	}

	@InstructionName(name = "/")
	public static Object div(Long a, Number b) {
		return a / b.longValue();
	}

	@InstructionName(name = "%")
	public static Object mod(Long a, Number num) {
		long b = num.longValue();
		try {
			return a % b;
		} catch (ArithmeticException e) {
			return e;
		}
	}

	@InstructionName(name = "<<")
	public static Long rightShift(Long a, Number b) {
		return a << b.longValue();
	}

	@InstructionName(name = ">>")
	public static Long leftShift(Long a, Number b) {
		return a >> b.longValue();
	}

	@InstructionName(name = ">>>")
	public static Long unsignedLeftShift(Long a, Number b) {
		return a >>> b.longValue();
	}

	@InstructionName(name = "&")
	public static Long bitwiseAnd(Long a, Number b) {
		return a & b.longValue();
	}

	@InstructionName(name = "|")
	public static Long bitwiseOr(Long a, Number b) {
		return a | b.longValue();
	}

	@InstructionName(name = "~")
	public static Long bitwiseNot(Long a) {
		return ~a;
	}

	@InstructionName(name = ">")
	public static Boolean gr(Long a, Number b) {
		return a > b.longValue();
	}

	@InstructionName(name = "1")
	public static Long one() {
		return 1L;
	}

	@InstructionTest(tests={"[[empty?] 0]", "[[empty? !] 1.]"})
	@InstructionName(name = "empty?")
	public static Boolean empty(Number prog) {
		return prog.doubleValue() == 0.0;
	}

	
	@InstructionName(name = "1.0")
	public static Double oneDouble() {
		return 1.0;
	}

	@InstructionName(name = "i2f")
	public static Double fromInt(Long v) {
		return v.doubleValue();
	}

	@InstructionTest(tests = { "[[1.0 1.0 + 2.0 =]]", "[[+ 2.1 =] 1.1 1]]" })
	@InstructionName(name = "+")
	public static Double add(Double a, Number b) {
		return a + b.doubleValue();
	}

	@InstructionName(name = "-")
	public static Double min(Double a) {
		return -a;
	}

	@InstructionName(name = "*")
	public static Double mul(Double a, Number b) {
		return a * b.doubleValue();
	}

	@InstructionName(name = "/")
	public static Double div(Double a, Number b) {
		return a / b.doubleValue();
	}

	@InstructionName(name = "%")
	public static Double mod(Double a, Number b) {
		return a % b.doubleValue();
	}

	@InstructionName(name = "sqrt")
	public static Double sqrt(Double a) {
		return Math.sqrt(a);
	}

	@InstructionName(name = "pow")
	public static Double pow(Double a, Number b) {
		return Math.pow(a, b.doubleValue());
	}

	@InstructionName(name = ">")
	public static Boolean gr(Double a, Number b) {
		return a > b.doubleValue();
	}

	@InstructionName(name = "max")
	public static Double max(Double a, Number b) {
		return Math.max(a, b.doubleValue());
	}

	@InstructionName(name = "min")
	public static Long max(Long a, Number b) {
		return Math.max(a, b.longValue());
	}

}
