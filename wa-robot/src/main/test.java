package main;

import java.util.ArrayList;
import java.util.List;

import controller.TapeSensor;
import lejos.nxt.Button;
import lejos.nxt.SensorPort;
import movement.Movement.move;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();

	public static void main(String[] args) {
		TapeSensor x1 = new TapeSensor(SensorPort.S1);
		TapeSensor x2 = new TapeSensor(SensorPort.S2);
		TapeSensor x3 = new TapeSensor(SensorPort.S3);
		TapeSensor x4 = new TapeSensor(SensorPort.S4);
		Button.waitForAnyPress();
		x1.calibrate();
		x2.calibrate();
		x3.calibrate();
		x4.calibrate();
		while (true) {
			int y1 = x1.isOnTape() ? 1 : 0;
			int y2 = x2.isOnTape() ? 1 : 0;
			int y3 = x3.isOnTape() ? 1 : 0;
			int y4 = x4.isOnTape() ? 1 : 0;
			System.out.println(y1 + "-" + y2 + "-" + y3 + "-" + y4);
			System.out.println(x1.getReading() + "-" + x2.getReading() + "-" + x3.getReading() + "-" + x4.getReading());
		}

		/*
		 * BASEPATH.add(move.FORWARD); BASEPATH.add(move.BACKWARD);
		 * BASEPATH.add(move.TURNLEFT); BASEPATH.add(move.TURNRIGHT);
		 * BASEPATH.add(move.WAIT); Controller controller = new
		 * Controller(BASEPATH); controller.run();
		 */
	}
}
