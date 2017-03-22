package utils;

public class DropLocation {

	private String name;
	private Location location;
	private boolean taken;

	public DropLocation(String name) {
		this.name = name;
		this.location = new Location(0, 0);
		this.taken = false;
	}

	public DropLocation(String name,Location location2) {
		this.name = name;
		this.location = location2;
		this.taken = false;	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(int x, int y) {
		this.location.setX(x);
		this.location.setY(y);
	}

	public void reserve() {
		this.taken = true;
	}
	
	public void unReserve(){
		this.taken = false;
	}

	public boolean isReserved() {
		return taken;
	}

	@Override
	public String toString() {
		return "Name: " + name + " Location: " + location.toString();
	}

}
