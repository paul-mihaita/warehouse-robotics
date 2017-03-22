package controller.logic;

import lejos.nxt.Button;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import utils.Location;
import utils.Robot;

public class RobotMovement {
	public static void backward(Pilot pilot, Robot robot) {
		pilot.rotate(180);
		forward(changeAngle((double) 180, robot.getOrientation()), pilot, robot);
	}

	public static void forward(Location orientation, Pilot pilot, Robot robot) {
		pilot.forward();
		Location l = robot.getCurrentLocation();
		l = addLocation(l, orientation);
		robot.setPosition(l, orientation);
		Delay.msDelay(250);
	}

	public static void turnleft(Pilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(-90);
		forward(changeAngle((double) -90, robot.getOrientation()), pilot, robot);
	}

	public static void turnright(Pilot pilot, Robot robot) {
		pilot.travel(0.05);
		pilot.rotate(90);
		forward(changeAngle((double) 90, robot.getOrientation()), pilot, robot);
	}

	public static void waitUntilPress(Pilot pilot) {
		pilot.stop();
		Button.waitForAnyPress();
	}

	private static Location changeAngle(Double theata, Location l) {
		int x = l.getX();
		int y = l.getY();
		theata = theata * (Math.PI / 180);
		int newx = round((x * Math.cos(theata)) + (y * Math.sin(theata)));
		int newy = round((x * (-Math.sin(theata))) + (y * Math.cos(theata)));
		l.setX(newx);
		l.setY(newy);
		return l;
	}
	private static int round(double d) {
		if (d > 0) {
			return (int) (d + 0.5);
		}
		return (int) (d - 0.5);
	}

	private static Location addLocation(Location a, Location b) {
		return new Location(a.getX() + b.getX(), a.getY() + b.getY());
	}
}
