package communication.thread;

import java.io.DataInputStream;
import java.io.IOException;

import communication.CommConst;
import communication.Message;
import communication.PCInputStream;
import lejos.util.Delay;
import utils.Robot;

public class RobotReciever extends Thread {

	private Robot robot;
	private DataInputStream connection;
	private PCInputStream fromPC;
	private boolean running = true;
	private Message msg;

	public RobotReciever(Robot robot, Message msg, DataInputStream connection) {
		this.robot = robot;
		this.msg = msg;
		this.connection = connection;
		this.fromPC = new PCInputStream(connection);
	}

	@Override
	public void run() {
		while (running) {
			try {
				switch (fromPC.readProtocol()) {
					case Robot:
						robot.update(fromPC.readRobot());
						robot.updated();
						break;
					case Message:
						msg.update(fromPC.readMessage());
						msg.updated();
						break;
					case DC:
						//we disconnected
						this.interrupt();
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			Delay.msDelay(CommConst.GRACE);
		}
	}

	@Override
	public void interrupt() {
		running = false;
		try {
			connection.close();
			fromPC.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
