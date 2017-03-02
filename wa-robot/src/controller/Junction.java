package controller;

import java.util.List;
import java.util.Queue;

import constants.RobotConstants;
import lejos.nxt.Button;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;

public class Junction extends AbstractBehavior {

	private TapeSensor left;
	private TapeSensor right;
	private Queue<move> moves;
	private int numMoves;
	private int count;

	public Junction(WheeledRobotConfiguration config, TapeSensor leftSensor, TapeSensor rightSensor, int numMoves, int count, List<move> moves) {
		super(config);
		this.left = leftSensor;
		this.right = rightSensor;
		this.moves = new Queue<move>();
		for (move move : moves) {
			this.moves.push(move);
		}
		this.numMoves = numMoves;
		this.count = count;
	}

	@Override
	public boolean takeControl() {
		return (left.isOnTape() && right.isOnTape()) && (numMoves > count) ;
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		count++;
		switch ((move) moves.pop()) {
			case BACKWARD:
				turnleft();
				turnleft();
				forward();
				break;
			case FORWARD:
				forward();
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
	}
	private void forward() {
		pilot.travel(RobotConstants.WHEEL_TO_SENSOR);
	}
	private void turnleft() {
		forward();
		pilot.rotate(-90);
	}
	private void turnright() {
		forward();
		pilot.rotate(90);
	}
	private void waitUntilPress() {
		Button.waitForAnyPress();
	}

}
