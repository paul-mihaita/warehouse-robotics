package controller.behaviours;

import controller.logic.Pilot;
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
	protected Pilot pilot;
	protected boolean suppressed;

	public AbstractBehavior(Pilot pilot) {
		this.pilot = pilot;
	}

	/**
	 * @see lejos.robotics.subsumption.Behavior#suppress()
	 */
	@Override
	public void suppress() {
		suppressed = true;
	}

}
