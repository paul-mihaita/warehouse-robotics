package utils;

public class Location {
	int x;
	int y;

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// GET
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	// SET
	public void setX(int x) {
		this.x=x;
	}

	public void setY(int y) {
		this.y=y;
	}
}
