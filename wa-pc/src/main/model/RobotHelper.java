package main.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import communication.CommConst.command;
import lejos.util.Delay;
import communication.Message;
import movement.Movement.move;
import utils.Robot;

public class RobotHelper extends Thread {

	private Message m;
	private Queue<ArrayList<move>> r;
	private Robot robot;

	public RobotHelper(Message m, Robot robot) {
		this.m = m;
		this.robot = robot;
	}
	
	public void overwriteRoutes(ArrayList<ArrayList<move>> routes) {
		Queue<ArrayList<move>> route = new LinkedList<ArrayList<move>>();
		for (ArrayList<move> arrayList : routes) {
			route.offer(arrayList);
		}
		this.r = route;
	}
	@Override
	public void run() {
		while (!r.isEmpty()) {																																																						
			//sends the robot the moves
			ArrayList<move> temp = r.poll();
			m.setMoves(temp);
			if (r.isEmpty()) {
				robot.setOnPickup(false);
			} else {
				if (!robot.isOnPickup())
					robot.setOnPickup(true);
			}
			//tells the robot to start
			m.setCommand(command.Start);
			while (m.getCommand() != command.Finish) {
				//wait for the robot to finish it segement
				Delay.msDelay(100);
			}
		}
	}
	
	@Override
	public void interrupt() {
		//job has been cancelled
		System.err.println("interrupted");
		m.setCommand(command.Wait);
	}
}
