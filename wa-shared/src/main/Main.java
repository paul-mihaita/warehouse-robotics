package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import comunication.BTConnect;
import lejos.pc.comm.NXTCommException;
import movement.Movement.move;
import utils.Route;

public class Main {
	// ignore this one liner
	private static final List<move> BASEPATH = new ArrayList<move>(Arrays.asList((new move[] { move.FORWARD, move.BACKWARD, move.TURNLEFT, move.TURNRIGHT, move.WAIT })));
	// nothing to see here

	// intialize the the log4j file
	private static Logger log = Logger.getLogger(Main.class);

	public static void main(String[] args) {
		// get jobs
		// order jobs
		// find route for first job

		// this will be from the pathfinding algorithm
		Route route = new Route();
		route.setPath(BASEPATH);

		// attempt to send it to the robot
		try {
			// open the bluetooth connection
			BTConnect connection = new BTConnect(log);
			connection.sendMoves(route.getPath()); // send the path
			connection.start(); // tell the robot to start
			connection.close(); // finished our communication close the
								// connection
		} catch (NXTCommException e) {
			// occurs if we can't find the NXT
			log.error(e);
		} catch (IOException e) {
			// occurs if it goes really wrong
			log.error(e);
		}

	}
}
