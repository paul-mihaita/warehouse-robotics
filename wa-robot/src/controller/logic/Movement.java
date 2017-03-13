package controller.logic;

import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import utils.Location;
import utils.Robot;

public class Movement {
	public static void backward(DifferentialPilot pilot, Robot robot) {
		pilot.rotate(180);
		forward(changeAngle((double) 180, robot.getOrientation()), pilot, robot);
	}

	public static void forward(Location orientation, DifferentialPilot pilot, Robot robot) {
		pilot.forward();
		Location l = robot.getCurrentLocation();
		l = addLocation(l, orientation);
		robot.setPosition(l, orientation);
		Delay.msDelay(150);
	}

	public static void turnleft(DifferentialPilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(-90);
		forward(changeAngle((double) -90, robot.getOrientation()), pilot, robot);
	}

	public static void turnright(DifferentialPilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(90, false);
		forward(changeAngle((double) 90, robot.getOrientation()), pilot, robot);
	}

	public static void waitUntilPress(DifferentialPilot pilot) {
		pilot.stop();
		Button.waitForAnyPress();
	}

	private static Location changeAngle(Double theata, Location l) {
		int x = l.getX();
		int y = l.getY();
		x = (int) ((x * Math.cos(theata)) - (y * Math.sin(theata)));
		y = (int) ((x * Math.sin(theata)) + (y * Math.cos(theata)));
		l.setX(x);
		l.setY(y);
		return l;
	}

	private static Location addLocation(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
}
