package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import lejos.util.Delay;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;
import rp.util.Collections;
import utils.Location;
import utils.Robot;

public class Junction extends AbstractBehavior {

	private TapeSensor left;
	private TapeSensor right;
	private Queue<move> moves;
	private Message msg;
	private Robot robot;

	public Junction(WheeledRobotConfiguration desc, SensorPort l, SensorPort r, Robot robot, Message msg) {
		super(desc);
		this.left = new TapeSensor(l);
		this.right = new TapeSensor(r);
		this.moves = new Queue<move>();
		this.msg = msg;
		this.robot = robot;
		for (move m : msg.getMoves()) {
			moves.push(m);
		}
	}

	@Override
	public boolean takeControl() {
		return (left.isOnTape() && right.isOnTape()) && (msg.getCommand() == command.Start);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void action() {
		System.out.println("junction");
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		System.out.println(moves.isEmpty());
		if (moves.isEmpty()) {
			msg.setCommand(command.Finish);
			return;
		}
		move m = (move) moves.pop();
		System.out.println(m);
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		switch (m) {
			case BACKWARD:
				backward();
				break;
			case FORWARD:
				System.out.println(robot.getOrientation().getX() + ","  + robot.getOrientation().getY());
				forward(robot.getOrientation());
				break;
			case TURNLEFT:
				turnleft();
				break;
			case TURNRIGHT:
				turnright();
				break;
			case WAIT:
				waitUntilPress();
				break;
		}
		System.out.println("updating moves");	
		ArrayList<move> list = new ArrayList<move>();
		qToList(moves, list);
		msg.setMoves(list);
	}
	private void qToList(Queue<move> q, List<move> l) {
		while (!q.isEmpty()) {
			move cur = (move) q.pop();
			l.add(cur);
		}
		for (move move : l) {
			q.push(move);
		}
	}

	private void backward() {
		System.out.println("starting rot");
		pilot.rotate(180);
		System.out.println("finished rot");
		System.out.println("stop");
		forward(changeAngle((double) 180, robot.getOrientation()));
	}

	private void forward(Location orientation) {
		pilot.forward();
		Delay.msDelay(50);
		Location l = robot.getCurrentLocation();
		l = addLocation(l, orientation);
		robot.setPosition(l, orientation);
	}

	private void turnleft() {
		pilot.travel(0.05);
		pilot.rotate(-90);
		forward(changeAngle((double) -90, robot.getOrientation()));
	}

	private void turnright() {
		pilot.travel(0.05);
		pilot.rotate(90,false);
		forward(changeAngle((double) 90, robot.getOrientation()));
	}

	private void waitUntilPress() {
		pilot.stop();
		Button.waitForAnyPress();
	}

	private Location changeAngle(Double theata, Location l) {
		int x = l.getX();
		int y = l.getY();
		x = (int) ((x * Math.cos(theata)) - (y * Math.sin(theata)));
		y = (int) ((x * Math.sin(theata)) - (y * Math.cos(theata)));
		l.setX(x);
		l.setY(y);
		return l;
	}

	private Location addLocation(Location x, Location y) {
		return new Location(x.getX() + y.getX(), x.getY() + y.getY());
	}
}
