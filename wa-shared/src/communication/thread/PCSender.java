package communication.thread;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.log4j.Logger;

import communication.CommConst;
import communication.CommConst.protocol;
import communication.Message;
import communication.PCOutputStream;
import lejos.util.Delay;
import utils.Robot;

public class PCSender extends Thread {

	private Robot robot;
	private Message msg;
	private boolean running = true;
	private Logger log;
	private PCOutputStream toNXT;
	private OutputStream connection;

	public PCSender(Robot robot, Message msg, OutputStream connection, Logger log) {
		this.robot = robot;
		this.msg = msg;
		this.log = log;
		this.connection = connection;
		this.toNXT = new PCOutputStream(connection, log);
	}

	@Override
	public void run() {
		while (running) {
			if (robot.needsUpdate()) {
				log.debug("robot needs an update");
				try {
					toNXT.sendProtocol(protocol.Robot);
					toNXT.sendRobot(robot);
					robot.updated();
					log.debug("robot updates sent");
				} catch (IOException e) {
					log.error("Failed to send robot object", e);
				}
				continue;
			}
			if (msg.needsUpdate()) {
				log.debug("Message needs update");
				try {
					toNXT.sendProtocol(protocol.Movement);
					toNXT.sendMoves(msg.getMoves());
					toNXT.sendProtocol(protocol.Command);
					toNXT.sendCommand(msg.getCommand());
					msg.updated();
					log.debug("Message updates sent");
				} catch (IOException e) {
					log.error("Failed to send message", e);
				}
				continue;
			}
			Delay.msDelay(CommConst.GRACE);
		}
	}

	@Override
	public void interrupt() {
		running = false;
		try {
			connection.close();
			toNXT.close();
		} catch (IOException e) {
			log.error("Failed to close communication on interupt", e);
		}

	}
}
