package communication.thread;

import java.io.IOException;

import org.apache.log4j.Logger;

import comunication.CommConst;
import comunication.CommConst.protocol;
import comunication.Message;
import comunication.PCOutputStream;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.util.Delay;
import utils.Robot;

public class PCSender extends Thread {

	private Robot robot;
	private Message msg;
	private boolean running;
	private Logger log;
	private NXTComm comm;
	private PCOutputStream toNXT;

	public PCSender(Robot robot, Message msg, Logger log) {
		this.robot = robot;
		this.msg = msg;
		this.log = log;
		try {
			initConnection();
		} catch (NXTCommException e) {
			log.error("couldn't connect", e);
		}
	}

	private void initConnection() throws NXTCommException {
		this.comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		log.info("Connecting");
		comm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, robot.getName(), robot.getBtAddress()));
		this.toNXT = new PCOutputStream(comm.getOutputStream(), log);
	}

	@Override
	public void run() {
		while (running) {
			if (robot.needsUpdate()) {
				try {
					toNXT.sendProtocol(protocol.Robot);
					toNXT.sendRobot(robot);
				} catch (IOException e) {
					log.error("Failed to send robot object", e);
				}
				continue;
			}
			if (msg.needsUpdate()) {
				try {
					toNXT.sendProtocol(protocol.Movement);
					toNXT.sendMoves(msg.getMoves());
					toNXT.sendProtocol(protocol.Command);
					toNXT.sendCommand(msg.getCommand());
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
			comm.close();
			toNXT.close();
		} catch (IOException e) {
			log.error("Failed to close communication on interupt", e);
		}

	}
}
