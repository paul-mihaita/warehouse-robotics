package controller.behaviours;

import java.util.List;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.logic.Movement;
import controller.logic.QueueTracker;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;
import utils.Location;
import utils.Robot;

public class Junction extends AbstractBehavior {

	private TapeSensor left;
	private TapeSensor right;
	private Message msg;
	private Robot robot;
	private QueueTracker moveQueue;

	public Junction(WheeledRobotConfiguration desc, TapeSensor l, TapeSensor r, Robot robot, Message msg) {
		super(desc);
		this.left = l;
		this.right = r;
		this.msg = msg;
		this.robot = robot;
		this.moveQueue = new QueueTracker(msg);
	}

	@Override
	public boolean takeControl() {
		return (left.isOnTape() && right.isOnTape()) && (msg.getCommand() == command.Start);
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		moveQueue.pull();
		if (moveQueue.finished()) {
			robot.setMoving(false);
			pilot.stop();
			return;
		}
		junction(moveQueue.getNextMove());
	}

	private void junction(move m) {
		//System.out.println(m);
		switch (m) {
			case BACKWARD:
				Movement.backward(pilot, robot);
				break;
			case FORWARD:
				Movement.forward(robot.getOrientation(), pilot, robot);
				break;
			case TURNLEFT:
				Movement.turnleft(pilot, robot);
				break;
			case TURNRIGHT:
				Movement.turnright(pilot, robot);
				break;
			case WAIT:
				Movement.waitUntilPress(pilot);
				break;
		}
	}
}
