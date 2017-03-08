package controller;

import java.util.List;

import constants.RobotConstants;
import lejos.nxt.SensorPort;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Robot;

public class Controller {
	Arbitrator arby;

	public Controller(List<move> moves) {
		Continue cont = new Continue(moves.size(), 0);
		SensorPort l = RobotConstants.LEFTSENSOR;
		SensorPort r = RobotConstants.RIGHTSENSOR;
		Behavior forward = new Forward(RobotConstants.DESC, cont);
		Behavior left = new Direction(RobotConstants.DESC, l, move.TURNLEFT);
		Behavior right = new Direction(RobotConstants.DESC, r, move.TURNRIGHT);
		Behavior junction = new Junction(RobotConstants.DESC, l, r, cont, moves);
		arby = new Arbitrator(new Behavior[] { forward, left, right, junction }, true);
	}

	public void run() {
		arby.start();
	}

}
