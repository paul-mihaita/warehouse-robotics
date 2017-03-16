package main.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import communication.CommConst.command;
import lejos.util.Delay;
import communication.Message;
import movement.Movement.move;

public class RobotHelper extends Thread {

	private Message m;
	private Queue<ArrayList<move>> r;

	public RobotHelper(Message m, ArrayList<ArrayList<move>> routes) {
		this.m = m;
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
			m.setMoves(r.poll());
			//tells the robot to start
			m.setCommand(command.Start);
			while (m.getCommand() != command.Finish) {
				//wait for the robot to finish
				Delay.msDelay(100);
			}
		}
	}
	
	@Override
	public void interrupt() {
		//job has been cancelled
	}
}
