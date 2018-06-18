package org.arg.pushforth.instructions;

import org.arg.pushforth.annotations.InsDef;
import org.arg.pushforth.annotations.InstructionTest;
import org.arg.pushforth.dictionary.InstructionFactory;
import org.arg.pushforth.program.Program;

public class Numbers {

	static {
		InstructionFactory.registerMembers(Numbers.class);
	}

	public static void load() {}

	public static int toIndex(Number value, int sz) {
		int v = (int) Math.abs(value.longValue());
		if (v >= sz) {
			v = sz-1;
		}
		return v;
	}
	
	@InstructionTest(tests = { "[[num?] 1]", "[[num?] 2.3]" })
	@InsDef(name = "num?")
	public static Boolean isnum(Object a) {
		return a instanceof Number;
	}

	@InstructionTest(tests = { "[[1.2 1 + 2 =]]" })
	@InsDef(name = "+")
	public static Long add(Long a, Number b) {
		return a + b.longValue();
	}

	@InstructionTest(tests = { "[[3 - -3 =]]" })
	@InsDef(name = "-")
	public static Long min(Long a) {
		return -a;
	}

	@InstructionTest(tests = { "[[2.1 2 * 4 =]]" })
	@InsDef(name = "*")
	public static Long mul(Long a, Number b) {
		return a * b.longValue();
	}

	@InstructionTest(tests = { "[[2.1 2 / 1 =]]" })
	@InsDef(name = "/")
	public static Object div(Long a, Number b) {
		return a / b.longValue();
	}

	@InstructionTest(tests = { "[[2.1 2 % 0 =]]" })
	@InsDef(name = "%")
	public static Object mod(Long a, Number num) {
		long b = num.longValue();
		try {
			return a % b;
		} catch (ArithmeticException e) {
			return e;
		}
	}

	@InstructionTest(tests={"[[3 1 << 8 =]]"})
	@InsDef(name = "<<")
	public static Long rightShift(Number a, Number b) {
		return a.longValue() << b.longValue();
	}

	@InstructionTest(tests={"[[2 16 >> 4 =]]"})
	@InsDef(name = ">>")
	public static Long leftShift(Number a, Number b) {
		return a.longValue() >> b.longValue();
	}

	@InsDef(name = ">>>")
	public static Number unsignedLeftShift(Number a, Number b) {
		return a.longValue() >>> b.longValue();
	}

	@InsDef(name = "&")
	public static Number bitwiseAnd(Number a, Number b) {
		return a.longValue() & b.longValue();
	}

	@InsDef(name = "|")
	public static Number bitwiseOr(Number a, Number b) {
		return a.longValue() | b.longValue();
	}

	@InsDef(name = "~")
	public static Number bitwiseNot(Number a) {
		return ~a.longValue();
	}

	@InstructionTest(tests={"[[zero?] 0]", "[[zero? !] 1.]"})
	@InsDef(name = "zero?")
	public static Boolean empty(Number prog) {
		return prog.doubleValue() == 0.0;
	}

	
	@InstructionTest(tests={"[[i2f 2.0 =] 2]", "[[i2f 2 = !] 2]"})
	@InsDef(name = "i2f")
	public static Double fromInt(Long v) {
		return v.doubleValue();
	}

	@InstructionTest(tests = { "[[1.0 1.0 + 2.0 =]]", "[[+ 2.1 =] 1.1 1]]" })
	@InsDef(name = "+")
	public static Double add(Double a, Number b) {
		return a + b.doubleValue();
	}

	
	@InstructionTest(tests={"[[1 - -1 =]]", "[[1 - -1.0 = !]]", "[[1.0 - -1.0 =]]"})
	@InsDef(name = "-")
	public static Object min(Number a) {
		if (a instanceof Long) {
			return  Long.valueOf(-a.longValue());
		}
		return -a.doubleValue();
	}

	@InstructionTest(tests={"[[2 0.5 * 1.0 =]]"})
	@InsDef(name = "*")
	public static Double mul(Double a, Number b) {
		return a * b.doubleValue();
	}

	@InstructionTest(tests={"[[2 1.0 / 0.5 =]]"})
	@InsDef(name = "/")
	public static Double div(Double a, Number b) {
		return a / b.doubleValue();
	}

	@InstructionTest(tests={"[[2 3.0 % 1.0 =]]"})
	@InsDef(name = "%")
	public static Double mod(Double a, Number b) {
		return a % b.doubleValue();
	}

	@InstructionTest(tests={"[[4 sqrt 2.0 =]]"})
	@InsDef(name = "sqrt")
	public static Double sqrt(Number a) {
		return Math.sqrt(a.doubleValue());
	}

	@InstructionTest(tests={"[[4 2 pow 16.0 =]]"})
	@InsDef(name = "pow")
	public static Double pow(Number a, Number b) {
		return Math.pow(a.doubleValue(), b.doubleValue());
	}

	@InstructionTest(tests={"[[3 4 > ]]"})
	@InsDef(name = ">")
	public static Boolean gr(Number a, Number b) {
		return a.doubleValue() > b.doubleValue();
	}

	@InstructionTest(tests={"[[3 2 < ]]"})
	@InsDef(name = "<")
	public static Boolean lt(Number a, Number b) {
		return a.doubleValue() < b.doubleValue();
	}
	
	@InstructionTest(tests={"[[3 4 max 4 = ]]"})
	@InsDef(name = "max")
	public static Number max(Number a, Number b) {
		if (a.doubleValue() > b.doubleValue()) {
			return a;
		}
		return b;
	}

	@InstructionTest(tests={"[[3 4 min 3 = ]]"})
	@InsDef(name = "min")
	public static Number min(Number a, Number b) {
		if (a.doubleValue() < b.doubleValue()) {
			return a;
		}
		return b;
	}
}
