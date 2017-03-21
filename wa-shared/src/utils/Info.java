package utils;

public class Info {
	public static final String[] RobotNames = new String[] {"Squirtle", "Bulbasaur", "Charmander" };
	public static final String[] RobotAddresses = new String[] {"0016530FDDAE", "0016531AFA0B" , "00165308DA58"};
	public static final Location[] RobotStarting = new Location[] {new Location (2,0), new Location (3,0), new Location (4,0)};
	public static final Location[] RobotVector = new Location[] {new Location (1,0), new Location (1,0), new Location (1,0)};
	/**
	 * Gets all 3 robots
	 * @return an array conatining all the robots
	 */
	public static Robot[] getRobots() {
		Robot[] r = new Robot[RobotNames.length];
		for (int i = 0; i < RobotAddresses.length; i++) {
			r[i] = new Robot(RobotNames[i], RobotAddresses[i], RobotStarting[i], addLocation(RobotStarting[i], RobotVector[i]));
		}
		return r;
	}
	private static Location addLocation(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
}
