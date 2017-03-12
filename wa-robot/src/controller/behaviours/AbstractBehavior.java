package controller.behaviours;

import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;
import rp.config.WheeledRobotConfiguration;
import rp.systems.WheeledRobotSystem;

/**
 * @author Alex Lewis
 * @author Maria
 * @author Louis
 *
 * 
 *         This class is the superclass for most behaviours of a robot, it has a
 *         pilot so that the robot can control its movement from any behaviour,
 *         and it has the default suppress implementation.
 */
public abstract class AbstractBehavior implements Behavior {

	protected WheeledRobotConfiguration config;
	protected DifferentialPilot pilot;
	protected boolean suppressed;

	public AbstractBehavior(WheeledRobotConfiguration config) {
		this.config = config;
		pilot = new WheeledRobotSystem(config).getPilot();
	}

	/**
	 * @see lejos.robotics.subsumption.Behavior#suppress()
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}

}
