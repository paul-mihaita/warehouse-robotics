package utils;

public class Robot {
	private String name = "";
	private String btAddress = "";
	private Location orientation;
	private Location currentLocation;
	private Route path;
	private boolean onJob;
	public static final int WEIGHT_LIMIT = 50;

	public Robot(String name, String btAdresss, Location orientation,
			Location startLocation) {
		this.name = name;
		this.btAddress = btAdresss;
		this.orientation = orientation;
		this.currentLocation = startLocation;
	}
	
	public String getName() {
		return name;
	}

	public Location getOrientation() {
		return orientation;
	}

	public void setOrientation(Location orientation) {
		this.orientation = orientation;
	}

	public void setCurrentLocation(Location loc) {
		this.currentLocation = loc;
	}

	public Location getCurrentLocation(Location loc) {
		return currentLocation;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public Route getPath() {
		return path;
	}

	public void setPath(Route path) {
		this.path = path;
	}

	public boolean isOnJob() {
		return onJob;
	}
}
