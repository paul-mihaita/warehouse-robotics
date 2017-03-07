package utils;

public class Robot {
	private String name = "";
	private String btAddress = "";
	private Location orientation;
	private Location currentLocation;
	private boolean onJob;
	public static final int WEIGHT_LIMIT = 50;
	private boolean needsUpdate;

    //if you want an empty robot just set everything to null
	public Robot(String name, String btAdresss, Location orientation, Location startLocation) {
		this.name = name;
		this.btAddress = btAdresss;
		this.orientation = orientation;
		this.currentLocation = startLocation;
		needsUpdate = true;
	}


	public String getName() {
		return name;
	}

	public Location getOrientation() {
		return orientation;
	}

	public void setOrientation(Location orientation) {
		this.orientation = orientation;
		needsUpdate = true;
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

	public boolean isOnJob() {
		return onJob;
	}
	
	public boolean needsUpdate() {
		return needsUpdate;
	}

	public String getBtAddress() {
		return btAddress;
	}
}
