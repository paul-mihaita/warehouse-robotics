package controller;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import rp.config.WheeledRobotConfiguration;

public class Forward extends AbstractBehavior {

	private Message msg;

	public Forward(WheeledRobotConfiguration config, Message msg) {
		super(config);
		this.msg = msg;
	}

	@Override
	public boolean takeControl() {
		return msg.getCommand() == command.Start;
	}

	@Override
	public void action() {
		System.out.println("forward");
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.forward();
		while (!suppressed);
		suppressed = false;
		pilot.stop();
	}

}
