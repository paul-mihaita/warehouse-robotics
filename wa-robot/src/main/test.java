package main;

import java.util.ArrayList;
import java.util.List;

import lejos.nxt.Motor;
import lejos.util.Delay;
import movement.Movement.move;

public class test {
	private static final List<move> BASEPATH = new ArrayList<move>();

	public static void main(String[] args) {

		Motor.C.setSpeed(Motor.C.getMaxSpeed());
		while (true) {
			Motor.C.forward();
			Delay.msDelay(10000000);
		}
	}
}
