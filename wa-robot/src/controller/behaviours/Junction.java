package controller.behaviours;

import java.util.List;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.logic.RobotMovement;
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
				RobotMovement.backward(pilot, robot);
				break;
			case FORWARD:
				RobotMovement.forward(robot.getOrientation(), pilot, robot);
				break;
			case TURNLEFT:
				RobotMovement.turnleft(pilot, robot);
				break;
			case TURNRIGHT:
				RobotMovement.turnright(pilot, robot);
				break;
			case WAIT:
				RobotMovement.waitUntilPress(pilot);
				break;
		}
	}
}
