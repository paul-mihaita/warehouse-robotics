package main;

import java.util.ArrayList;
import java.util.List;

import constants.RobotConstants;
import controller.logic.RobotMovement;
import movement.Movement.move;
import rp.systems.WheeledRobotSystem;
import utils.Location;
import utils.Robot;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();

	public static void main(String[] args) {
		RobotMovement.forward(new Location(1,0), new WheeledRobotSystem(RobotConstants.DESC).getPilot(), new Robot(null, null, new Location(1,0), new Location(1, 1)));
	}
}
