package utils;

public class Node {
	private Location location;
	private boolean reserved;
	
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location x){
		location = x;
	}
	public boolean isReserved(){
		return reserved;
	}
	public void setReserve(boolean x){
		reserved = x;
	}
}
