package main;

import java.util.ArrayList;
import java.util.List;

import communication.CommConst.command;
import communication.Message;
import communication.thread.Client;
import controller.behaviours.Controller;
import movement.Movement.move;
import robot_gui.GUI;
import utils.Info;
import utils.Location;
import utils.Robot;
public class RobotMain {
	public static void main(String[] args) {
		Robot[] r = Info.getRobots();
		
		Message m = new Message((List<move>) new ArrayList<move>(), command.Wait);
		Client client = new Client(r[0], m);
		client.launch(); //do not use client.start(); use this instead
		Controller controller = new Controller(r[0], m);
		GUI gui = new GUI(r[0], m);
		gui.start();
		//System.out.println(r.getOrientation().getX() + "," + r.getOrientation().getY() + ":" + r.getCurrentLocation().getX() + "," + r.getCurrentLocation().getY());
		controller.run();
	}
}
