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
		/*Button.waitForAnyPress();
		TapeSensor x1 = new TapeSensor(SensorPort.S1);
		TapeSensor x4 = new TapeSensor(SensorPort.S4);
		while (true) {
			System.out.println(x1.initial + ";" +  x1.getReading() + ":" + x4.initial + ";" + x4.getReading());
			System.out.println(x1.isOnTape() + ":" + x4.isOnTape());
		}*/

		/*
		 * BASEPATH.add(move.FORWARD); BASEPATH.add(move.BACKWARD);
		 * BASEPATH.add(move.TURNLEFT); BASEPATH.add(move.TURNRIGHT);
		 * BASEPATH.add(move.WAIT); Controller controller = new
		 * Controller(BASEPATH); controller.run();
		 */
	}
}
