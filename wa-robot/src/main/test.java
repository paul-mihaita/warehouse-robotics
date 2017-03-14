package main;

import java.util.ArrayList;
import java.util.List;

import communication.CommConst.command;
import communication.Message;
import controller.behaviours.Controller;
import movement.Movement.move;
import utils.Location;
import utils.Robot;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();

	public static void main(String[] args) {
		/*
		 * DifferentialPilot pilot = new
		 * WheeledRobotSystem(RobotConstants.DESC).getPilot();
		 * pilot.setRotateSpeed(RobotConstants.ROT_SPEED); pilot.rotate(180);
		 */
		/*
		 * Button.waitForAnyPress(); TapeSensor x1 = new
		 * TapeSensor(SensorPort.S1); TapeSensor x4 = new
		 * TapeSensor(SensorPort.S4); while (true) {
		 * System.out.println(x1.initial + ";" + x1.getReading() + ":" +
		 * x4.initial + ";" + x4.getReading()); System.out.println(x1.isOnTape()
		 * + ":" + x4.isOnTape()); }
		 */

		BASEPATH.add(move.FORWARD);
		BASEPATH.add(move.BACKWARD);
		BASEPATH.add(move.TURNLEFT);
		BASEPATH.add(move.TURNRIGHT);
		BASEPATH.add(move.WAIT);
		Controller controller = new Controller(
				new Robot("Keith", "0016530FDDAE", new Location(2, 0), new Location(1, 0)),
				new Message(BASEPATH, command.Start));
		controller.run();

	}
}
