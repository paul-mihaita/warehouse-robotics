package utils;

public class Location implements Comparable {

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

	@Override
	public int compareTo(Object o) {
		Location a = this;
		Location b = (Location) o;
		return a.getX() < b.getX() ? -1 : a.getX() > b.getX() ? 1 : a.getY() < b.getY() ? -1 : a.getY() > b.getY() ? 1: 0;
	}
	@Override
	public  boolean equals(Object other) {
		
	    if (other instanceof Location) {
	    	Location a = this;
	    	Location b = (Location) other;
			return a.getX() < b.getX() ? false : a.getX() > b.getX() ? false : a.getY() < b.getY() ? false : a.getY() > b.getY() ? false: true;
	    }
	    return false;
	  }
	
}
