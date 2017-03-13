package controller.behaviours;

import communication.Message;
import constants.RobotConstants;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import movement.Movement.move;
import utils.Robot;

public class Controller extends Thread {
	Arbitrator arby;

	public Controller(Robot robot, Message msg) {
		TapeSensor l = new TapeSensor(RobotConstants.LEFTSENSOR);
		TapeSensor r = new TapeSensor(RobotConstants.LEFTSENSOR);
		Behavior forward = new Forward(RobotConstants.DESC, msg);
		Behavior left = new Direction(RobotConstants.DESC, l, move.TURNLEFT, msg);
		Behavior right = new Direction(RobotConstants.DESC, r, move.TURNRIGHT, msg);
		Behavior junction = new Junction(RobotConstants.DESC, l, r, robot, msg);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction });
	}

	@Override
	public void run() {
		arby.start();
	}
}
