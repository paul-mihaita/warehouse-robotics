package main;

import java.util.ArrayList;
import java.util.List;

import controller.TapeSensor;
import lejos.nxt.SensorPort;
import movement.Movement.move;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();

	public static void main(String[] args) {
		TapeSensor lx = new TapeSensor(SensorPort.S4);
		TapeSensor rx = new TapeSensor(SensorPort.S1);
		while (true) {
			System.out.println(lx.isOnTape() + "-" + rx.isOnTape());
			System.out.println(lx.getReading() + "-" + rx.getReading());
		}

		/*
		 * BASEPATH.add(move.FORWARD); BASEPATH.add(move.BACKWARD);
		 * BASEPATH.add(move.TURNLEFT); BASEPATH.add(move.TURNRIGHT);
		 * BASEPATH.add(move.WAIT); Controller controller = new
		 * Controller(BASEPATH); controller.run();
		 */
	}
}
