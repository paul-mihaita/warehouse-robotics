package communication.thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import communication.Message;
import communication.NXTInputStream;
import utils.Robot;

public class PCReciever extends Thread {
	
	private Robot robot;
	private DataInputStream connection;
	private NXTInputStream fromNXT;
	private boolean running = true;
	private Message msg;
	private Logger log;
	public PCReciever(Robot robot, Message msg, DataInputStream connection, Logger log) {
		this.robot = robot;
		this.connection = connection;
		this.msg = msg;
		this.fromNXT = new NXTInputStream(connection);
		this.log = log;
	}
	@Override
	public void run() {
		while (running) {
			try {
				switch(fromNXT.readProtocol()) {
					case Movement:
						msg.setMoves(fromNXT.readMoves());
						break;
					case Robot:
						robot.update(fromNXT.readRobot());
						break;
					case Command:
						msg.setCommand(fromNXT.readCommand());
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
			fromNXT.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
