package utils;

public class Robot {

	private String name = "";
	private String btAddress = "";
	private Location orientation;
	private Location currentLocation;
	private boolean onJob;
	private boolean onPickup;
	public static final int WEIGHT_LIMIT = 50;
	private boolean needsUpdate;


    //if you want an empty robot just set everything to null
	public Robot(String name, String btAdresss, Location orientation, Location startLocation) {
		this.name = name;
		this.btAddress = btAdresss;
		this.orientation = orientation;
		this.currentLocation = startLocation;
		this.onJob = false;
		this.onPickup = false;
		needsUpdate = true;
	}

	public Robot(){
		name = "NO_NAME";
		
	}
	public Robot(String name){
		this.name = name;		
	}
	
	public void setName(String name){
		this.name = name;
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
		needsUpdate = true;
	}

	public void setPosition(Location loc, Location orientation) {
		this.currentLocation = loc;
		this.orientation = orientation;
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
		this.needsUpdate = true;
	}

	public void setOnJob(boolean onJob) {
		this.onJob = onJob;
		this.needsUpdate = true;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public String getBtAddress() {
		return btAddress;
	}

	public void update(Robot r) {
		this.btAddress = r.getBtAddress();
		this.currentLocation = r.getCurrentLocation();
		this.name = r.getName();
		this.onJob = r.isOnJob();
		this.onPickup = r.isOnPickup();
		this.orientation = r.getOrientation();
	}

	public void updated() {
		needsUpdate = false;
	}
}
