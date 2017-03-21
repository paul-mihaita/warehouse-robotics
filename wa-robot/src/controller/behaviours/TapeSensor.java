package controller.behaviours;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

/**
 * @author Lexer747, Alex Lewis
 *
 */
public class TapeSensor {

	private SensorPort sensor;
	private int initial;

	public TapeSensor(SensorPort port) {
		new LightSensor(port);
		this.sensor = port;
		Delay.msDelay(500);
		this.initial = this.takeReading();
	}

	// public so a user of this object can force an update if they so want
	public synchronized int takeReading() {
		return sensor.readRawValue();
	}

	public synchronized boolean isOnTape() {
		return checkDelta(initial, takeReading(), 40);
	}

	private synchronized boolean checkDelta(int initial, int newValue, int diff) {
		return (Math.abs(initial - newValue) >= diff);
	}
}
