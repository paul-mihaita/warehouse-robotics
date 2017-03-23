package utils;

import java.util.ArrayList;

public class Info {
	public static final String[] RobotNames = new String[] {"Squirtle", "Bulbasaur", "Charmander" };
	public static final String[] RobotAddresses = new String[] {"0016530FDDAE", "0016531AFA0B" , "00165308DA58"};
	public static final Location[] RobotStarting = new Location[] {new Location (10,0), new Location (9,0), new Location (11,0)};
	public static final Location[] RobotVector = new Location[] {new Location (-1,0), new Location (-1,0), new Location (-1,0)};
	/**
	 * Gets all 3 robots
	 * @return an array containing all the robots
	 */
	public static Robot[] getRobotsPaul() {
		Robot[] r = new Robot[RobotNames.length];
		for (int i = 0; i < RobotAddresses.length; i++) {
			r[i] = new Robot(RobotNames[i], RobotAddresses[i],addLocation(RobotStarting[i], RobotVector[i]), RobotStarting[i] );
		}
		return r;
	}
	public static ArrayList<Robot> getRobotsRavinder() {
		ArrayList<Robot> r = new ArrayList<Robot>();
		for (int i = 0; i < RobotAddresses.length; i++) {
			Robot temp= new Robot(RobotNames[i], RobotAddresses[i],addLocation(RobotStarting[i], RobotVector[i]), RobotStarting[i] );
			r.add(temp);
		}
		return r;
	}
	public static Robot[] getRobotsVector() {
		Robot[] r = new Robot[RobotNames.length];
		for (int i = 0; i < RobotAddresses.length; i++) {
			r[i] = new Robot(RobotNames[i], RobotAddresses[i], RobotVector[i], RobotStarting[i]);
		}
		return r;
	}
	private static Location addLocation(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
}
