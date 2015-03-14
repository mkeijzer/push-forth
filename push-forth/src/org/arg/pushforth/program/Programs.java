package org.arg.pushforth.program;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.arg.pushforth.dictionary.SymbolTable;
import org.arg.pushforth.instructions.Instruction;

/**
 * Set of utility functions for working with programs, contains parser, etc.
 * 
 * @author Maarten Keijzer
 * 
 */
public class Programs {

	// parse switches on these things, so the delimiters are hardcoded there
	public static final char[] delim = { '[', ']' };
	private static final char sep = ' ';
	private static final char quote = '"';
	public static final String programDelimiter = ".";

	public static Program cons(Object obj, Program prog) {
		if (prog == null || obj == null) {
			throw new NullPointerException(
					"Lists are not allowed to contain null");
		}
		return new ConsedProgram(obj, prog);
	}

	public static Program cons(Object o1, Object o2, Program prog) {
		return cons(o1, cons(o2, prog));
	}

	public static Program cons(Object o1, Object o2, Object o3, Program prog) {
		return cons(o1, cons(o2, cons(o3, prog)));
	}

	public static Program cons(Object o1, Object o2, Object o3, Object o4,
			Program prog) {
		return cons(o1, cons(o2, cons(o3, cons(o4, prog))));
	}

	public static Program cons(Object o1, Object o2, Object o3, Object o4,
			Object o5, Program prog) {
		return cons(o1, cons(o2, cons(o3, cons(o4, cons(o5, prog)))));
	}

	public static Program list(Object... objs) {
		if (objs.length == 0) {
			return Program.nil;
		}
		if (objs.length == 1) {
			return new SingletonProgram(objs[0]);
		}
		for (int i = 0; i < objs.length; ++i) {
			if (objs[i] == null) {
				throw new NullPointerException(
						"Lists are not allowed to contain null");
			}
		}

		return new ArrayProgram(objs.clone());
	}

	public static Program list(Collection<Object> objs) {
		return list(objs.toArray());
	}

	public static Program append(Program first, Program second) {
		if (first.isEmpty()) {
			return second;
		}

		return new AppendedProgram(first, second);
	}

	public static StringBuilder print(Program prog, StringBuilder builder,
			int depth) {
		return print(prog, builder, depth, false, false);
	}

	public static StringBuilder print(Program prog, StringBuilder builder,
			int depth, boolean pretty, boolean skipBraces) {
		// if (depth > Globals.RECURSION_DEPTH) {
		// return builder;
		// }
		//
		if (!skipBraces)
			builder.append(delim[0]);
		boolean first = true;
		while (!prog.isEmpty()) {
			Object obj = prog.first();

			if (first && !"".equals(programDelimiter)) {
				first = false;

			}

			if (obj instanceof Program) {
				print((Program) obj, builder, depth + 1, pretty, false); // don't
																			// skip
																			// braces
																			// at
																			// the
																			// next
																			// level
			} else if (obj instanceof String) {
				builder.append(quote);
				builder.append(obj);
				builder.append(quote);
			} else {
				builder.append(obj);
			}

			prog = prog.rest();
			if (prog.isEmpty())
				break;

			builder.append(sep);

			if (pretty && builder.length() > 100) {
				builder.append("...");
				break;
			}

		}

		if (!skipBraces)
			builder.append(delim[1]);
		return builder;
	}

	public static Program readNextExpression(InputStream stream)
			throws IOException {
		return readNextExpression(stream, theMap);
	}

	public static Program readNextExpression(InputStream stream,
			InstructionMap theMap) throws IOException {
		// first read in a string, then parse the string

		StringBuilder builder = new StringBuilder();
		int depth = 1;
		boolean inquotes = false;
		boolean firstfound = false;

		while (depth != 0) {
			int ch = stream.read();
			if (ch == -1) {
				return null;
			}
			// check some more..... spaces before first open bracket, etc.
			if (firstfound == false) {
				if (ch == delim[0]) {
					builder.append((char) ch);
					firstfound = true;
					continue;
				} else if (ch == '\n' || ch == '\t' || ch == ' ') {
					continue;
				}
				throw new RuntimeException(
						"Characters found before first bracket");
			}

			if (!inquotes && ch == delim[0]) {
				depth++;
			} else if (!inquotes && ch == delim[1]) {
				depth--;
			}
			if (ch == '"') {
				inquotes = !inquotes;
			}
			builder.append((char) ch);
		}

		return parse(builder.toString(), theMap);
	}

	public interface InstructionMap {
		Object get(String atom);
	}

	private static InstructionMap theMap = new InstructionMap() {
		public Object get(String atom) {
			return SymbolTable.get(atom);
		}
	};

	public static Program parse(CharSequence seq) {

		return parse(seq, theMap);
	}

	public static Program parse(CharSequence charSeq, InstructionMap map) {
		assert delim[0] == '[';
		assert delim[1] == ']';

		String seq = removeComments(charSeq);

		Program prog = list();
		int[] idxPtr = { 0 };

		while (true) {

			char ch = seq.charAt(idxPtr[0]++);

			switch (ch) {
			case '[': {
				prog = cons(list(), prog);
				break;
			}
			case ']': {
				// done with this program, push
				Program first = (Program) prog.first();
				first = Programs.reverse(first);

				prog = prog.rest();
				if (prog.isEmpty()) {
					return first;
				}
				Program second = (Program) prog.first();
				prog = cons(cons(first, second), prog.rest());
			}
			case '\n':
			case '\t':
			case ' ':
				break; // pass
			default: {
				idxPtr[0]--;
				Object obj = parseAtom(seq, idxPtr, map);
				if (obj == programDelimiter) {
					prog = reverse(prog);
					prog = Programs.list(prog);
				} else {
					Program first = (Program) prog.first();
					first = cons(obj, first);
					prog = cons(first, prog.rest());
				}
			}

			}

		}
	}

	private static String removeComments(CharSequence s) {
		StringBuilder builder = new StringBuilder();
		boolean inComment = false;
		for (int i = 0; i < s.length(); ++i) {
			char c = s.charAt(i);
			if (inComment) {
				if (c == '\n') {
					inComment = false;
				}
			} else {
				if (c == '#') {
					inComment = true;
				} else {
					builder.append(c);
				}
			}
		}

		return builder.toString();
	}

	private static Program reverse(Program prog) {
		Program res = list();
		while (!prog.isEmpty()) {
			res = cons(prog.first(), res);
			prog = prog.rest();
		}
		return res;
	}

	public static Object parseAtom(CharSequence seq) {
		return parseAtom(seq, new int[]{0}, theMap);
	}

	
	private static Object parseAtom(CharSequence seq, int[] idxPtr,
			InstructionMap map) {

		boolean inquotes = false;
		StringBuilder atomBuilder = new StringBuilder();

		MAIN: while (idxPtr[0] < seq.length()) {
			char ch = seq.charAt(idxPtr[0]++);
			switch (ch) {
			case '\n':
			case '\t':
			case ' ': {
				if (inquotes) {
					atomBuilder.append(ch);
				} else {
					break MAIN;
				}
				break;
			}

			case '[':
			case ']': {
				idxPtr[0]--;
				break MAIN;
			}
			case '"': {
				if (inquotes) {
					break MAIN;
				} else {
					inquotes = true;
				}
				break;
			}
			default:
				atomBuilder.append(ch);
				break;
			}
		}

		// now we have an atom, let's see if we can make some sense out of it
		String atom = atomBuilder.toString().intern();

		return getAtomValue(atom, map);
	}

	public static Object getAtomValue(String atom, InstructionMap map) {

		if ("true".equalsIgnoreCase(atom)) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(atom)) {
			return Boolean.FALSE;
		}

		try {
			return Long.parseLong(atom);
		} catch (NumberFormatException e) {
		}

		try {
			return Double.parseDouble(atom);
		} catch (NumberFormatException e) {
		}

		Object ins = map.get(atom);
		if (ins != null) {
			return ins;
		}

		if (atom == "null") {
			return null;
		}

		return atom; // treat as a string
	}

	private Programs() {
	} // no objects should be created

	public static boolean isHalted(Program prog) {
		Object frst = prog.first();
		if (frst instanceof Program) {
			return ((Program) prog.first()).isEmpty();
		}
		return true;
	}

	public static int length(Program p) {
		if (p.isEmpty())
			return 0;
		return 1 + length(p.rest());
	}

	public static int size(Program p) {
		if (p.isEmpty())
			return 0;
		int sz = 1;
		if (p.first() instanceof Program) {
			sz += size((Program) p.first());
		}
		return sz + size(p.rest());
	}

	public static Iterable<Object> iterate(final Program program) {
		return new Iterable<Object>() {

			@Override
			public Iterator<Object> iterator() {
				return new Iterator<Object>() {

					Program p = program;

					@Override
					public boolean hasNext() {
						return !p.isEmpty();
					}

					@Override
					public Object next() {
						Object first = p.first();
						p = p.rest();
						return first;
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

	public static boolean equals(Program a, Program b) {
		return a.toString().equals(b.toString());
	}

	public static Object[] toArray(Program p) {
		if (p instanceof ArrayProgram) {
			ArrayProgram a = (ArrayProgram) p;
			if (a.getIdx() == 0) {
				return a.getArray();
			}
		}
		// find length
		List<Object> res = new ArrayList<Object>();
		while (!p.isEmpty()) {
			res.add(p.first());
			p = p.rest();
		}
		return res.toArray();
	}

	private static final int SUM = 0;
	private static final int COUNT = 1;

	private static void prob(Program a, Program b, double[] stats, int maxlen) {
		if (--maxlen <= 0) {
			return;
		}
		if (a.isEmpty() && b.isEmpty()) {
			stats[SUM]++;
			stats[COUNT]++;
			return;
		}

		// add to size
		stats[COUNT]++;

		if (a.isEmpty()) {
			prob(a, b.rest(), stats, maxlen);
			return;
		}

		if (b.isEmpty()) {
			prob(a.rest(), b, stats, maxlen);
			return;
		}

		stats[SUM]++; // one point for having the structure the same

		Object a1 = a.first();
		Object b1 = b.first();

		if (a1 instanceof Program && b1 instanceof Program) {
			prob((Program) a1, (Program) b1, stats, maxlen);
		} else { // terminal
			objectDistance(a1, b1, stats);
		}

		prob(a.rest(), b.rest(), stats, maxlen);
	}

	private static void objectDistance(Object a, Object b, double[] stats) {
		if (a instanceof Number && b instanceof Number) {
			double aval = ((Number) a).doubleValue();
			double bval = ((Number) b).doubleValue();
			double d = aval - bval;
			d = Math.abs(d);
			stats[COUNT]++;
			stats[SUM] += 1 / (1 + d);
			return;
		}

		stats[COUNT]++;
		if (a.equals(b)) {
			stats[SUM]++;
		}
	}

	public static double distance(Program a, Program b, int maxlen) {
		double[] res = new double[2];
		prob(a, b, res, maxlen);
		return 1 - res[SUM] / res[COUNT];
	}

	public static List<Object> flatten(Program prog, int maxlen) {
		List<Object> lst = new ArrayList<Object>();
		flatten(prog, lst, maxlen);
		return lst;
	}

	private static void flatten(Program prog, List<Object> lst, int maxlen) {
		if (prog.isEmpty() || lst.size() > maxlen) {
			return;
		}

		Object first = prog.first();
		if (first instanceof Program) {
			flatten((Program) first, lst, maxlen);
		} else {
			lst.add(first);
		}

		flatten(prog.rest(), lst, maxlen);
	}

	public static double flatDistance(Program a, Program b, int max) {
		return distance(Programs.list(flatten(a, max)),
				Programs.list(flatten(b, max)), max);
	}

	public static String printJoy(Program prog) {
		StringBuilder builder = new StringBuilder();

		Program code = (Program) (prog instanceof Program ? prog.first()
				: Program.nil);
		Program data = prog.rest();
		data = reverse(data);

		while (!data.isEmpty()) {
			Object obj = data.first();
			if (obj instanceof Program) {
				print((Program) obj, builder, 1);
			} else {
				builder.append(obj.toString());
			}
			data = data.rest();
			builder.append(" ");
		}

		builder.append("<=> ");
		
		while (!code.isEmpty()) {
			Object obj = code.first();
			if (obj instanceof Program) {
				print((Program) obj, builder, 1);
			} else {
				builder.append(obj.toString());
			}
			code = code.rest();
			builder.append(" ");
		}
		return builder.toString();
	}

	public static Program compress(Map<Program, Program> map, Program program) {
		if (program.isEmpty()) {
			return Program.nil;
		}

		Program res = map.get(program);

		if (res == null) {
			Object first = program.first();
			if (first instanceof Program) {
				first = compress(map, (Program) first);
			}
			res = cons(first, compress(map, program.rest()));

			map.put(res, res);
		}

		return res;
	}


}
