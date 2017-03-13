package controller.logic;

import java.util.List;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import controller.behaviours.TapeSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import movement.Movement.move;
import rp.systems.WheeledRobotSystem;
import utils.Robot;

public class Controller extends Thread {
	private boolean run;
	private TapeSensor l;
	private TapeSensor r;
	private Robot robot;
	private Message msg;
	private DifferentialPilot pilot;
	private QueueTracker moveQueue;

	public Controller(Robot robot, Message msg) {
		System.out.println("	" + Runtime.getRuntime().totalMemory());
		run = true;
		this.l = new TapeSensor(RobotConstants.LEFTSENSOR);
		this.r = new TapeSensor(RobotConstants.RIGHTSENSOR);
		this.robot = robot;
		this.msg = msg;
		this.pilot = new WheeledRobotSystem(RobotConstants.DESC).getPilot();
		this.moveQueue = new QueueTracker(msg);
	}

	@Override
	public void run() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		while (run) {
			while (msg.getCommand() == command.Start) {
				System.out.println("	" + Runtime.getRuntime().freeMemory());
				if (l.isOnTape() && r.isOnTape()) {
					moveQueue.pull();
					if (moveQueue.finished()) {
						continue;
					}
					move m = moveQueue.getNextMove();
					junction(m);
					
				} else if (l.isOnTape()) {
					System.out.println("left");
					pilot.steer(RobotConstants.STEER_SPEED);
				} else if (r.isOnTape()) {
					System.out.println("right");
					pilot.steer(-1 * RobotConstants.STEER_SPEED);
				} else {
					System.out.println("for");
					pilot.forward();
				}
				Delay.msDelay(100);
				pilot.stop();
			}
		}
	}
	private void junction(move m) {
		switch (m) {
			case BACKWARD:
				Movement.backward(pilot, robot);
				break;
			case FORWARD:
				Movement.forward(robot.getOrientation(), pilot, robot);
				break;
			case TURNLEFT:
				Movement.turnleft(pilot, robot);
				break;
			case TURNRIGHT:
				Movement.turnright(pilot, robot);
				break;
			case WAIT:
				Movement.waitUntilPress(pilot);
				break;
		}
	}

	@Override
	public void interrupt() {
		this.run = false;
	}
}
