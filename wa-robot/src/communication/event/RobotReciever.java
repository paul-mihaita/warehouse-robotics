package communication.event;

import java.io.IOException;

import communication.PCInputStream;
import comunication.Message;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import utils.Robot;

public class RobotReciever extends Thread{
	
	private Robot robot;
	private Message msg;
	private BTConnection connection;
	private PCInputStream fromPC;
	private boolean running;
	public RobotReciever(Robot robot, Message msg) {
		this.robot = robot;
		this.msg = msg;
		initConnection();
	}
	private void initConnection() {
		this.connection = Bluetooth.waitForConnection();
		this.fromPC = new PCInputStream(connection.openDataInputStream());
	}
	@Override
	public void run() {
		while (running) {
			
		}
	}
	@Override
	public void interrupt() {
		running = false;
		connection.close();
		try {
			fromPC.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
