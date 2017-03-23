package main;

import java.util.ArrayList;
import java.util.List;

import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import communication.thread.Client;
import controller.behaviours.Controller;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import movement.Movement.move;
import robot_gui.GUI;
import utils.Info;
import utils.Location;
import utils.Robot;
import utils.Task;
public class RobotMain {
	public static void main(String[] args) {
		Robot[] r = Info.getRobotsPaul();
		Location[] vector = Info.RobotVector;
		for (int i = 0; i < r.length; i++) {
			r[i].updated();
		}
		int rob = 0; //0 = s, 1 = b, 2 = c
		printWelcome(r[rob]);
		Message m = new Message((List<move>) new ArrayList<move>(), command.Wait, new BasicJob(1, new Task("a", 1)));
		Client client = new Client(r[rob], m);
		client.launch(); //do not use client.start(); use this instead
		Controller controller = new Controller(r[rob], m, vector[rob]);
		GUI gui = new GUI(r[rob], m);
		gui.start();
		controller.start();
		Button.waitForAnyPress();
	}
	
	private static void printWelcome(Robot r) {
		//Priting welcome message
		LCD.clear();
		LCD.drawString("WELCOME", 5, 1);
		LCD.drawString("I'm "+r.getName(), 1, 3);
		LCD.drawString("Ready to run!", 2, 4);
	}
}
