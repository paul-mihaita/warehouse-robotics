package controller.behaviours;

import beats.Mixtape;
import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.logic.Pilot;
import controller.logic.QueueTracker;
import controller.logic.RobotMovement;
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
	private Location vector;

	public Junction(Pilot pilot, TapeSensor l, TapeSensor r, Robot robot, Message msg, Location vector) {
		super(pilot);
		this.left = l;
		this.right = r;
		this.msg = msg;
		this.robot = robot;
		this.moveQueue = new QueueTracker(msg);
		this.vector = vector;
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
			Mixtape.play();
			robot.setMoving(false);
			pilot.stop();
			pilot.travel(-3 * RobotConstants.WHEEL_TO_SENSOR);
			pilot.stop();
			return;
		}
		junction(moveQueue.getNextMove());
		pilot.stop();
	}

	private void junction(move m) {
		switch (m) {
			case BACKWARD:
				RobotMovement.backward(vector, pilot, robot);
				break;
			case FORWARD:
				RobotMovement.forward(vector, pilot, robot);
				break;
			case TURNLEFT:
				RobotMovement.turnleft(vector, pilot, robot);
				break;
			case TURNRIGHT:
				RobotMovement.turnright(vector, pilot, robot);
				break;
			case WAIT:
				RobotMovement.waitUntilPress(pilot);
				break;
		}
	}
}
