package communication.thread;

import java.io.DataInputStream;
import java.io.IOException;

import communication.Message;
import communication.PCInputStream;
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
					case Movement:
						msg.setMoves(fromPC.readMoves());
						break;
					case Robot:
						robot = fromPC.readRobot();
						break;
					case Command:
						msg.setCommand(fromPC.readCommand());
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
