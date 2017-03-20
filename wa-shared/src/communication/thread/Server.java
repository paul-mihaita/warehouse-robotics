package communication.thread;

import org.apache.log4j.Logger;

import communication.Message;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.util.Delay;
import utils.Robot;

//new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0))
public class Server extends Thread {

	private Logger log;
	private NXTInfo[] nxts;
	private Robot[] robots;
	private Message[] msgs;
	private PCSender[] outThread;
	private PCReciever[] inThread;
	private boolean connected = false;

	public Server(Robot[] robots, Message[] msgs, Logger log) {
		this.log = log;
		this.robots = robots;
		this.msgs = msgs;
		this.outThread = new PCSender[robots.length];
		this.inThread = new PCReciever[robots.length];
		if (robots.length != msgs.length) {
			IllegalArgumentException e = new IllegalArgumentException(
					"List of robots and messages where different sizes");
			log.error(e);
			throw e;
		}
		log.info("Constructing Server");
		this.nxts = new NXTInfo[msgs.length];
		for (int i = 0; i < msgs.length; i++) {
			log.debug("creating nxt object: " + robots[i].getName() + " -- " + robots[i].getBtAddress());
			nxts[i] = new NXTInfo(NXTCommFactory.BLUETOOTH, robots[i].getName(), robots[i].getBtAddress());
		}
		log.info("Server setup finished");
	}

	@Override
	public void run() {
		NXTComm[] comms = new NXTComm[nxts.length];
		log.debug("creating NXTComm objects");
		for (int i = 0; i < comms.length; i++) {
			try {
				comms[i] = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			} catch (NXTCommException e) {
				log.error(e);
				return;
			}
		}
		log.info("Connecting to the Robots");
		for (int i = 0; i < comms.length; i++) {
			try {
				log.debug("Attempting to connect to: " + nxts[i].name);
				comms[i].open(nxts[i]);
				log.debug("\tConnected, creating threads");
				outThread[i] = new PCSender(robots[i], msgs[i], comms[i].getOutputStream(), log);
				inThread[i] = new PCReciever(robots[i], msgs[i], comms[i].getInputStream(), log);
				log.debug("thread created");
			} catch (NXTCommException e) {
				log.error("Couldn't connect", e);
				return;
			}
		}
		log.info("Connected, starting communication");
		for (int i = 0; i < comms.length; i++) {
			log.debug("starting: " + nxts[i].name);
			outThread[i].start();
			inThread[i].start();
		}
		connected = true;
	}

	@Override
	public void interrupt() {
		System.out.println("Server interupted");
		for (int i = 0; i < inThread.length; i++) {
			outThread[i].interrupt();
			inThread[i].interrupt();
		}
	}

	// the method to start the server
	public void launch() {
		this.start();
		while (!connected) {
			Delay.msDelay(1000);
		}
	}

}
