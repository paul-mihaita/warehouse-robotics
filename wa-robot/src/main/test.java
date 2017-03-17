package main;

import java.util.ArrayList;
import java.util.List;

import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.behaviours.Forward;
import controller.logic.Pilot;
import movement.Movement.move;
import rp.systems.WheeledRobotSystem;
import utils.Task;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();
	static Pilot pilot = new Pilot(new WheeledRobotSystem(RobotConstants.DESC).getPilot());
	public static void main(String[] args) {
		Message msg = new Message(BASEPATH, command.Start, new BasicJob(0, new Task("a",0)));
		Forward forward = new Forward(pilot, msg );
		System.out.println("action");
		forward.action();
	}
}
