package controller;

public class Continue {
	private int num;
	private int count;

	public Continue(int num, int count) {
		this.num = num;
		this.count = count;
	}

	public boolean cont() {
		return num > count;
	}

	public void inc() {
		count++;
	}

}
