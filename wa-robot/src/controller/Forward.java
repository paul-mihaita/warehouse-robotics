package controller;

import communication.Message;
import communication.CommConst.command;
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
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.forward();
	}

}
