package communication.thread;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import communication.CommConst;
import communication.Message;
import communication.NXTInputStream;
import lejos.util.Delay;
import utils.Robot;

public class PCReciever extends Thread {

	private Robot robot;
	private InputStream connection;
	private NXTInputStream fromNXT;
	private boolean running = true;
	private Message msg;
	private Logger log;

	public PCReciever(Robot robot, Message msg, InputStream inputStream, Logger log) {
		this.robot = robot;
		this.connection = inputStream;
		this.msg = msg;
		this.fromNXT = new NXTInputStream(inputStream);
		this.log = log;
	}

	@Override
	public void run() {
		while (running) {
			try {
				log.debug("reading protocol");
				switch (fromNXT.readProtocol()) {
					case Movement:
						msg.setMoves(fromNXT.readMoves());
						msg.updated();
						break;
					case Robot:
						robot.update(fromNXT.readRobot());
						robot.updated();
						break;
					case Command:
						msg.setCommand(fromNXT.readCommand());
						msg.updated();
						break;
				}
			} catch (IOException e) {
				log.error("couldn't read data", e);
			}
			Delay.msDelay(CommConst.GRACE);
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
