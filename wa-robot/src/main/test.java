package main;

import java.util.ArrayList;
import java.util.List;

import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.behaviours.Controller;
import controller.behaviours.Forward;
import controller.logic.Pilot;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.util.Integration;
import movement.Movement.move;
import rp.systems.WheeledRobotSystem;
import utils.Info;
import utils.Task;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();
	static Pilot pilot = new Pilot(new WheeledRobotSystem(RobotConstants.DESC).getPilot());
	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}

			@Override
			public void buttonReleased(Button b) {
				return;
			}
		});
		Button.waitForAnyPress();
		init();
		Message msg = new Message(BASEPATH, command.Start, new BasicJob(0, new Task("a",0)));
		Controller cont = new Controller(Info.getRobotsVector()[0], msg);
		cont.run();
	}
	private static void init() {
		for (int i = 0; i < 6; i++) {
			BASEPATH.add(move.FORWARD);
			BASEPATH.add(move.TURNLEFT);
			BASEPATH.add(move.BACKWARD);
			BASEPATH.add(move.TURNRIGHT);
			BASEPATH.add(move.FORWARD);
		}
	}
}
