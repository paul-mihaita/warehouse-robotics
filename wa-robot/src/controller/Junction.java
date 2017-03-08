package controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

import communication.CommConst.command;
import communication.Message;
import constants.RobotConstants;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;
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
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		if (moves.isEmpty()) {
			msg.setCommand(command.Finish);
			return;
		}
		switch ((move) moves.pop()) {
			case BACKWARD:
				backward();
				break;
			case FORWARD:
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
		msg.setMoves(new ArrayList<move>((Collection<move>) moves));
	}

	private void backward() {
		pilot.rotate(180);
		forward(changeAngle((double) 180, robot.getOrientation()));
	}

	private void forward(Location orientation) {
		pilot.travel(RobotConstants.WHEEL_TO_SENSOR);
		Location l = robot.getCurrentLocation();
		l = addLocation(l, orientation);
		robot.setPosition(l, orientation);
	}

	private void turnleft() {

		forward(changeAngle((double) -90, robot.getOrientation()));
		pilot.rotate(-90);
	}

	private void turnright() {
		forward(changeAngle((double) 90, robot.getOrientation()));
		pilot.rotate(90);
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
