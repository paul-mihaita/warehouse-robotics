package controller;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * @author Lexer747, Alex Lewis
 *
 */
public class TapeSensor {

	private int reading;
	private SensorPort sensor;
	private int TAPE;

	public TapeSensor(SensorPort port) {
		new LightSensor(port);
		this.sensor = port;
	}

	public TapeSensor(SensorPort port, int Tape) {
		this(port);
		TAPE = Tape;
	}

	// public so a user of this object can force an update if they so want
	public void takeReading() {
		reading = sensor.readRawValue();
	}

	public int getOldReading() {
		return reading;
	}

	public int getReading() {
		this.takeReading();
		return reading;
	}

	public boolean isOnTape() {
		this.takeReading();
		return roughlyEqual(reading, TAPE, 20);
	}

	public void calibrate() {
		int sum = 0;
		for (int i = 0; i < 10; i++) {
			this.takeReading();
			sum += reading;
		}
		sum /= 10;
		this.TAPE = sum;
	}

	/**
	 * Checks if x == y, +- the accuracy
	 * 
	 * @param x
	 * @param y
	 * @param accuracy
	 * @return
	 */
	private boolean roughlyEqual(int x, int y, int accuracy) {
		return (Math.abs(x - y) < accuracy);
	}
}
