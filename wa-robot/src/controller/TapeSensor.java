package controller;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

/**
 * @author Lexer747, Alex Lewis
 *
 */
public class TapeSensor {

	private int reading;
	private SensorPort sensor;
	private int initial;

	public TapeSensor(SensorPort port) {
		new LightSensor(port);
		this.sensor = port;
		Delay.msDelay(100);
		this.initial = this.getReading();
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
		return checkDelta(initial, reading, 40);
	}
	
	private boolean checkDelta(int initial,int newValue,int diff) {
		return (Math.abs(initial - newValue) >= diff);
	}

	/**
	 * Checks if x == y, +- the accuracy
	 * 
	 * @param x
	 * @param y
	 * @param accuracy
	 * @return
	 *//*
	private boolean roughlyEqual(int x, int y, int accuracy) {
		return (Math.abs(x - y) < accuracy);
	}*/
}
