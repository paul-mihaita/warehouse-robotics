package utils;

public class Robot {

	private String name = "";
	private String btAddress = "";
	private Location orientation;
	private Location vectorOrient;
	private Location currentLocation;
	private boolean onJob;
	private boolean onPickup;
	public static final int WEIGHT_LIMIT = 50;
	private boolean needsUpdate;
	private boolean isMoving;
	private int movesCompleted;
	private Integer globalMax;

	// if you want an empty robot just set everything to null
	public Robot(String name, String btAdresss, Location orientation, Location startLocation) {
		this.name = name;
		this.btAddress = btAdresss;
		this.orientation = orientation;
		this.currentLocation = startLocation;
		this.onJob = false;
		this.onPickup = false;
		movesCompleted = 0;
		needsUpdate = true;
		this.setMoving(false);
	}

	public String getName() {
		return name;
	}

	public Location getOrientation() {
		return orientation;
	}

	public Location getRelativeOrientation() {
		return new Location(orientation.getX() - currentLocation.getX(), orientation.getY() - currentLocation.getY());
	}

	public void setOrientation(Location orientation) {
		this.orientation = orientation;
		needsUpdate = true;
	}

	public void setCurrentLocation(Location loc) {
		this.currentLocation = loc;
		needsUpdate = true;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public boolean isOnJob() {
		return onJob;
	}

	public boolean isOnPickup() {
		return onPickup;
	}

	public void setOnPickup(boolean onPickup) {
		this.onPickup = onPickup;
		needsUpdate = true;
	}

	public void setOnJob(boolean onJob) {
		this.onJob = onJob;
		needsUpdate = true;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public String getBtAddress() {
		return btAddress;
	}

	public void update(Robot r) {
		this.currentLocation = r.getCurrentLocation();
		this.name = r.getName();
		this.onJob = r.isOnJob();
		this.onPickup = r.isOnPickup();
	}

	public void updated() {
		needsUpdate = false;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
		needsUpdate = true;
	}

	public void setMoves(int moves) {
		movesCompleted = moves;
		needsUpdate = true;
	}

	public void incrementMoves() {
		movesCompleted++;
		synchronized (globalMax) {
			if (movesCompleted > globalMax)
				globalMax = movesCompleted;
		}
		needsUpdate = true;
	}

	public int getMovesCompleted() {
		return movesCompleted;
	}
	@Override
	public String toString() {
		String returnStr = "";
		returnStr += name;
		returnStr += currentLocation.toString();
		return returnStr;
	}
	
	public Robot cloneRobot() {
		Robot clone = new Robot(this.name, this.btAddress, this.orientation, this.currentLocation);
		clone.setMoves(this.movesCompleted);
		clone.setOnJob(this.onJob);
		clone.setOnPickup(this.onPickup);
		clone.setMoving(this.isMoving);
		clone.updated();
		return clone;
	}
}
