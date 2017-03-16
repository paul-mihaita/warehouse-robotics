package utils;

public class DropLocation {

	private String name;
	private Location location;
	
	public DropLocation(String name){
		this.name=name;
	}
	
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
	
	
}
