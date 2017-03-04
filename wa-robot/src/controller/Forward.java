package controller;

import constants.RobotConstants;
import rp.config.WheeledRobotConfiguration;

public class Forward extends AbstractBehavior {

	private Continue cont;

	public Forward(WheeledRobotConfiguration config, Continue cont) {
		super(config);
		this.cont = cont;
	}

	@Override
	public boolean takeControl() {
		return cont.cont();
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.forward();
	}

}
