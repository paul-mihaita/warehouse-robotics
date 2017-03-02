package constants;

import lejos.nxt.Motor;
import rp.config.WheeledRobotConfiguration;

public class RobotConstants {
	public static final WheeledRobotConfiguration DESC = new WheeledRobotConfiguration(0.056f, 0.10605f, 0.170f, Motor.A,
			Motor.C);
	public static final int TAPE = 38;
	public static final double FORWARD_SPEED = 0.1;
	public static final double ROT_SPEED = 45;
}
