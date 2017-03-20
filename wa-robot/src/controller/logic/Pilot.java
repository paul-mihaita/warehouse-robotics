package controller.logic;

import lejos.robotics.navigation.DifferentialPilot;

public class Pilot {
	private DifferentialPilot pilot;

	public Pilot(DifferentialPilot pilot) {
		this.pilot = pilot;
	}
	
	public synchronized void forward() {
		pilot.forward();
	}
	
	public synchronized void travel(double distance) {
		pilot.travel(distance);
	}
	
	public synchronized void rotate(double angle) {
		pilot.rotate(angle);
	}
	
	public synchronized void stop() {
		pilot.stop();
	}
	
	public synchronized void steer(double turnRate){
		pilot.steer(turnRate);
	}
	
	public synchronized void setTravelSpeed(double speed) {
		pilot.setTravelSpeed(speed);
	}
	
	public synchronized void setRotateSpeed(double speed) {
		pilot.setRotateSpeed(speed);
	}
}
  