package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import communication.thread.PCSender;
import comunication.Message;
import comunication.CommConst.command;
import movement.Movement.move;
import utils.Location;
import utils.Robot;
import utils.Route;

public class Main {
	// ignore this one liner
	private static final List<move> BASEPATH = new ArrayList<move>(
			Arrays.asList((new move[] { move.FORWARD, move.BACKWARD, move.TURNLEFT, move.TURNRIGHT, move.WAIT })));
	// nothing to see here

	// intialize the the log4j file
	private static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		// get jobs
		// order jobs
		// list of jobs

		// this will be from the pathfinding algorithm
		Route route = new Route();
		route.setPath(BASEPATH);

		//create a robot
		Robot keith = new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0));
		//create a message object which contains the path the robot must execute and the command it should currently follow
		Message startPath = new Message(BASEPATH, command.Start);
		//create the thread to send the robot the info
		PCSender toNXTThread = new PCSender(keith, startPath, log);
		//start the thread which communicates with the robot
		toNXTThread.run();
		
		//if the startPath object is changed or the keith object is changed
		//the corresponding changes will be automatically sent to the robot
		//via the thread.

	}
}
