package controller;

import java.util.List;

import constants.RobotConstants;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import movement.Movement.move;

public class Controller {
	Arbitrator arby;

	public Controller(List<move> moves) {
		int numMoves = moves.size();
		int count = 0;
		TapeSensor leftSensor = new TapeSensor(RobotConstants.LEFTSENSOR);
		TapeSensor rightSensor = new TapeSensor(RobotConstants.RIGHTSENSOR);
		Behavior forward = new Forward(RobotConstants.DESC, numMoves, count);
		Behavior left = new Direction(RobotConstants.DESC, leftSensor, move.TURNLEFT);
		Behavior right = new Direction(RobotConstants.DESC, rightSensor, move.TURNRIGHT);
		Behavior junction = new Junction(RobotConstants.DESC, leftSensor, rightSensor, numMoves, count, moves);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction }, true);
	}

	public void run() {
		arby.start();
	}

}
