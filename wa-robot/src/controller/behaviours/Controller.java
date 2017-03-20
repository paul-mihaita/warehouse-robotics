package controller.behaviours;

import communication.Message;
import constants.RobotConstants;
import controller.logic.Pilot;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import movement.Movement.move;
import rp.systems.WheeledRobotSystem;
import utils.Robot;

public class Controller extends Thread {
	Arbitrator arby;

	public Controller(Robot robot, Message msg) {
		TapeSensor l = new TapeSensor(RobotConstants.LEFTSENSOR);
		TapeSensor r = new TapeSensor(RobotConstants.RIGHTSENSOR);
		Pilot pilot = new Pilot(new WheeledRobotSystem(RobotConstants.DESC).getPilot());
		Behavior forward = new Forward(pilot, msg);
		Behavior left = new Direction(pilot, l, move.TURNLEFT, msg);
		Behavior right = new Direction(pilot, r, move.TURNRIGHT, msg);
		Behavior junction = new Junction(pilot, l, r, robot, msg);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction }, false);
	}

	@Override
	public void run() {
		arby.start();
	}
}
