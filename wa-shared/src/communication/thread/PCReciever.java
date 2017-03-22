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
	private boolean safe; //perform safe disconnects

	public PCReciever(Robot robot, Message msg, InputStream inputStream, Logger log) {
		this.robot = robot;
		this.connection = inputStream;
		this.msg = msg;
		this.fromNXT = new NXTInputStream(inputStream, log);
		this.log = log;
		this.setName("PCReciever - " + robot.getName() + ":");
		safe = false;
	}

	@Override
	public void run() {
		while (running) {
			try {
				switch (fromNXT.readProtocol()) {
					case Robot:
						robot.update(fromNXT.readRobot());
						log.info(this.getName() + " - " + robot.toString());
						robot.updated();
						break;
					case Message:
						msg.update(fromNXT.readMessage());
						log.info(this.getName() + " - " + msg.toString());
						msg.updated();
					case DC:
						log.error(this.getName() +"Disconnected");
						if (safe)
							this.interrupt();
				}
			} catch (IOException e) {
				log.error(this.getName() +"couldn't read data", e);
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
			log.error(this.getName() +"Failed to close", e);
		}
	}

}
