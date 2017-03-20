package utils;

public class Info {
	public static final String[] RobotNames = new String[] {"Squirtle", "Bulbasaur", "Charmander" };
	public static final String[] RobotAddresses = new String[] {"0016530FDDAE", "0016531AFA0B" , "00165308DA58"};
	
	/**
	 * Gets all 3 robots
	 * @return an array conatining all the robots
	 */
	public static Robot[] getRobots() {
		Robot[] r = new Robot[RobotNames.length];
		for (int i = 0; i < RobotAddresses.length; i++) {
			r[i] = new Robot(RobotNames[i], RobotAddresses[i], new Location(0,0),new Location(0,0));
		}
		return r;
	}
}
