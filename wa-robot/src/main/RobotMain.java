package main;

import robot_gui.GUI;
import utils.Location;
import utils.Robot;

public class RobotMain {
	public static void main(String[] args) {
		Robot r = new Robot("Cell", "0016531AFA0B", new Location(0, 0), new Location(1, 0));
		GUI gui = new GUI(r);
		gui.run();
	}
}
