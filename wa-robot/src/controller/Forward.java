package controller;

import constants.RobotConstants;
import rp.config.WheeledRobotConfiguration;

public class Forward extends AbstractBehavior {

	private int numMoves;
	private int count;

	public Forward(WheeledRobotConfiguration config, int numMoves, int count) {
		super(config);
		this.numMoves = numMoves;
		this.count = count;
	}

	@Override
	public boolean takeControl() {
		return (numMoves > count);
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.forward();
	}

}
