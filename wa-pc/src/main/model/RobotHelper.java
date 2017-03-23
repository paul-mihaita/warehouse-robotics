package main.model;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.Message;
import communication.thread.Converters;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Job;
import utils.Robot;

public class RobotHelper extends Thread {

	private Message m;
	private ArrayList<ArrayList<move>> r;
	private Robot robot;
	private Logger log;
	private Job job;
	private int index;
	public RobotHelper(Message m, Robot robot, Logger log) {
		this.m = m;
		this.robot = robot;
		this.log = log;
		this.setName(robot.getName());
	}
	
	public void overwriteRoutes(ArrayList<ArrayList<move>> routes, Job j ) {
		this.r = routes;
		this.job = j;
		index = 0;
	}
	@Override
	public void run() {
		while (index < r.size()) {
			//sends the robot the moves
			ArrayList<move> temp = r.get(index);
			m.setMoves(temp);
			
			//check if we need to set the robot to drop off
			if (index == job.getTasks().size()) {
				//robot is on a drop off so don't tell its next job
				robot.setOnPickup(false);
			} else {
				if (!robot.isOnPickup())
					robot.setOnPickup(true);
				//tell the robot what job it is doing
				m.setJob(Converters.toBasicJob(job, index));
			}
			
			
			
			//tells the robot to start
			m.setCommand(command.Start);
			
			index++;
			
			log.info(this.getName() + " - " + m.toString());
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
