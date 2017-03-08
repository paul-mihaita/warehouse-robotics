package main;

import java.util.ArrayList;
import java.util.List;

import communication.Message;
import movement.Movement.move;
import communication.CommConst.command;
import communication.thread.Client;
import robot_gui.GUI;
import utils.Location;
import utils.Robot;

public class RobotMain {
	public static void main(String[] args) {
		Robot r;
		r = new Robot("Cell", "0016531AFA0B", new Location(0, 1), new Location(1, 0));
		//r = new Robot("Keith", "0016530FDDAE", new Location(0,1), new Location(0,0));
		Message m = new Message((List<move>) new ArrayList<move>(), command.Wait);
		Client client = new Client(r, m);
		client.launch(); //do not use client.start(); use this instead
		GUI gui = new GUI(r, m);
		gui.run();
	}
}
