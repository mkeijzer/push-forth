package org.arg.pushforth.program;


class ArrayProgram extends AbstractProgram implements Program {

	final Object[] items;
	final int idx;

	public ArrayProgram(Object... items) {
		assert items != null;
		assert noNullItems(items);
		
		this.idx = 0;
		this.items = items.clone();
	}
	
	private boolean noNullItems(Object... items) {
		for (Object item : items) {
			if (item == null) return false;
		}
		
		return true;
	}
	
	ArrayProgram(int idx, Object... items) {
		this.idx = idx;
		this.items = items;
	}

	@Override
	public Object first() {
		if (idx == items.length) {
			return null;
		}
		return items[idx];
	}

	@Override
	public Program rest() {
		if (idx >= items.length-1) {
			return Program.nil;
		}
		return new ArrayProgram(idx+1, items);
	}
	
	@Override
	public boolean isEmpty() {
		return idx == items.length;
	}

	public int getIdx() {
		return idx;
	}

	public Object[] getArray() {
		return items.clone();
	}

	@Override
	public int length() {
		return items.length;
	}
	
}
