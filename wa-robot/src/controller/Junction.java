package controller;

import java.util.List;
import java.util.Queue;

import constants.RobotConstants;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import movement.Movement.move;
import rp.config.WheeledRobotConfiguration;
import utils.Robot;

public class Junction extends AbstractBehavior {

	private TapeSensor left;
	private TapeSensor right;
	private Queue<move> moves;
	private Continue cont;

	public Junction(WheeledRobotConfiguration config, SensorPort l, SensorPort r, Continue cont, List<move> moves) {
		super(config);
		this.left = new TapeSensor(l);
		this.right = new TapeSensor(r);
		this.moves = new Queue<move>();
		for (move move : moves) {
			this.moves.push(move);
		}
		this.cont = cont;
	}

	@Override
	public boolean takeControl() {
		return (left.isOnTape() && right.isOnTape()) && (cont.cont()) ;
	}

	@Override
	public void action() {
		pilot.setTravelSpeed(RobotConstants.FORWARD_SPEED);
		pilot.setRotateSpeed(RobotConstants.ROT_SPEED);
		cont.inc();
		System.out.println(cont.cont());
		switch ((move) moves.pop()) {
			case BACKWARD:
				pilot.rotate(180);
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
		pilot.stop();
		Button.waitForAnyPress();
	}

}
