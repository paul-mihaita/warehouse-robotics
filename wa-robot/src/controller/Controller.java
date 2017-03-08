package controller;

import communication.Message;
import constants.RobotConstants;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import movement.Movement.move;
import utils.Robot;

public class Controller {
	Arbitrator arby;

	public Controller(Robot robot, Message msg) {
		SensorPort l = RobotConstants.LEFTSENSOR;
		SensorPort r = RobotConstants.RIGHTSENSOR;
		Behavior forward = new Forward(RobotConstants.DESC, msg);
		Behavior left = new Direction(RobotConstants.DESC, l, move.TURNLEFT);
		Behavior right = new Direction(RobotConstants.DESC, r, move.TURNRIGHT);
		Behavior junction = new Junction(RobotConstants.DESC, l, r, robot, msg);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction }, true);
	}

	public void run() {
		arby.start();
	}

}
