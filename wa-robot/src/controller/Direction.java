package controller;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import lejos.nxt.SensorPort;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;

public class Direction extends AbstractBehavior {

	private TapeSensor sensor;
	private move dir;
	private Message msg;

	public Direction(WheeledRobotConfiguration config, SensorPort s, move dir, Message msg) {
		super(config);
		this.msg = msg;
		this.sensor = new TapeSensor(s);
		switch (dir) {
			case TURNLEFT:
				this.dir = dir;
				break;
			case TURNRIGHT:
				this.dir = dir;
				break;
			default:
				throw new IllegalArgumentException("Incorrect direction for parameter");
		}
	}

	@Override
	public boolean takeControl() {
		return (sensor.isOnTape() && msg.getCommand() == command.Start);
	}

	@Override
	public void action() {
		System.out.println("direction");
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		while (!suppressed && sensor.isOnTape()) {
			if (dir == move.TURNLEFT) {
				pilot.steer(RobotConstants.STEER_SPEED);
			} else {
				pilot.steer(-1 * RobotConstants.STEER_SPEED);
			}
		}
		suppressed = false;
	}

}
