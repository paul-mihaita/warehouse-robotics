package controller.logic;

import lejos.nxt.Button;
import lejos.util.Delay;
import movement.Maths;
import utils.Location;
import utils.Robot;

public class RobotMovement {
	public static void backward(Location vector, Pilot pilot, Robot robot) {
		pilot.rotate(190);
		forward(Maths.changeAngle((double) 180, vector), pilot, robot);
	}

	public static void forward(Location orientation, Pilot pilot, Robot robot) {
		pilot.forward();
		Location l = robot.getCurrentLocation();
		l = Maths.addLocation(l, orientation);
		robot.setCurrentLocation(l);
		Delay.msDelay(250);
	}

	public static void turnleft(Location vector, Pilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(-95);
		forward(Maths.changeAngle((double) -90, vector), pilot, robot);
	}

	public static void turnright(Location vector, Pilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(95);
		forward(Maths.changeAngle((double) 90, vector), pilot, robot);
	}

	public static void waitUntilPress(Pilot pilot) {
		pilot.stop();
		Button.waitForAnyPress();
	}
}
