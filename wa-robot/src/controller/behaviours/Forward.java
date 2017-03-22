package controller.behaviours;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.logic.Pilot;
import rp.config.WheeledRobotConfiguration;

public class Forward extends AbstractBehavior {

	private Message msg;

	public Forward(Pilot pilot, Message msg) {
		super(pilot);
		this.msg = msg;
	}

	@Override
	public boolean takeControl() {
		return msg.getCommand() == command.Start;
	}

	@Override
	public void action() {
		System.out.println("for");
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.forward();
		while (!suppressed);
		suppressed = false;
		pilot.stop();
	}

}
