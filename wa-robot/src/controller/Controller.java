package controller;

import communication.Message;
import constants.RobotConstants;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import movement.Movement.move;
import utils.Robot;

public class Controller extends Thread{
	Arbitrator arby;

	public Controller(Robot robot, Message msg) {
		SensorPort l = RobotConstants.LEFTSENSOR;
		SensorPort r = RobotConstants.RIGHTSENSOR;
		Behavior forward = new Forward(RobotConstants.DESC, msg);
		Behavior left = new Direction(RobotConstants.DESC, l, move.TURNLEFT, msg);
		Behavior right = new Direction(RobotConstants.DESC, r, move.TURNRIGHT, msg);
		Behavior junction = new Junction(RobotConstants.DESC, l, r, robot, msg);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction }, true);
	}
	@Override
	public void run() {
		System.out.println("arbirator started");
		arby.start();
	}

}
