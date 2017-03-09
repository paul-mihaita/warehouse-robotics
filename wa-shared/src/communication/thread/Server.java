package communication.thread;

import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import communication.CommConst.command;
import communication.Message;
import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Location;
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

	public static void main(String[] args) {
		Logger log = Logger.getLogger(Server.class);
		ConsoleAppender ca = new ConsoleAppender();
		ca.setWriter(new OutputStreamWriter(System.out));
		ca.setLayout(new PatternLayout("%-5p [%t]: %m%n"));
		log.addAppender(ca);
		log.setLevel(Level.ALL);
		Robot r0 = new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0));
		Robot r1 = new Robot("Cell", "0016531AFA0B", new Location(0, 1), new Location(1, 0));
		Robot r2 = new Robot("Charmander", "0016531AF6D6", new Location(0, 1), new Location(2, 0));
		ArrayList<move> moves = new ArrayList<move>();
		moves.add(move.FORWARD);
		moves.add(move.BACKWARD);
		moves.add(move.TURNLEFT);
		moves.add(move.TURNRIGHT);
		Message m0 = new Message(moves, command.Wait);
		Message m1 = new Message(moves, command.Wait);
		Message m2 = new Message(moves, command.Wait);
		Server s = new Server(new Robot[] { r1 }, new Message[] { m1 }, log);
		s.launch();
		Delay.msDelay(1000);
		m0.setCommand(command.Start);
		m1.setCommand(command.Start);
		m2.setCommand(command.Start);
		while (true) {
			if (m1.getCommand() == command.Finish) //&& (m1.getCommand() == command.Finish)) 
				{
				System.out.println("finished");
				m1.setMoves(moves);
				m1.setCommand(command.Start);
			} else {
				System.out.println(Converters.robotToString(r1));
				//System.out.println(Converters.robotToString(r1));
			}
			Delay.msDelay(1000);
		}
	}

}
