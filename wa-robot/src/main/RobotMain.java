package main;

import java.util.ArrayList;
import java.util.List;

import communication.BasicJob;
import communication.CommConst.command;
import communication.Message;
import controller.behaviours.Controller;
import lejos.nxt.Button;
import lejos.util.Delay;
import movement.Movement.move;
import robot_gui.GUI;
import utils.Info;
import utils.Robot;
import utils.Task;
public class RobotMain {
	public static void main(String[] args) {
		Robot[] r = Info.getRobots();
		int rob = 0; //0 = s, 1 = b, 2 = c
		Message m = new Message((List<move>) new ArrayList<move>(), command.Wait, new BasicJob(1, new Task("a", 1)));
		//Client client = new Client(r[rob], m);
		//client.launch(); //do not use client.start(); use this instead
		Controller controller = new Controller(r[rob], m);
		System.out.println("c built");
		Delay.msDelay(1000);
		System.out.println((r[rob] != null) + "---" + (m != null));
		Delay.msDelay(2000);
		GUI gui = new GUI(r[rob], m);
		gui.start();
		controller.start();
		Button.waitForAnyPress();
	}
}
