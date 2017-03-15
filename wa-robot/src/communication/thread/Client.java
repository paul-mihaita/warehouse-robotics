package communication.thread;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import communication.CommConst.command;
import communication.Message;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Location;
import utils.Robot;

public class Client extends Thread {
	private DataInputStream in;
	private OutputStream out;
	private Robot robot;
	private Message msg;
	private RobotReciever inThread;
	private RobotSender outThread;
	private boolean connected = false;

	// new Robot("Keith", "0016530FDDAE", new Location(0, 0), new Location(0, 0))
	// new Robot("Cell", "0016531AFA0B", new Location(0,0), new Location(1, 0))
	public Client(Robot robot, Message msg) {
		BTConnection comm = Bluetooth.waitForConnection();
		// wait for PC

		// open streams
		this.in = comm.openDataInputStream();
		this.out = comm.openOutputStream();
		this.robot = robot;
		this.msg = msg;
		robot.updated();
		msg.updated();
	}

	@Override
	public void run() {
		// construct threads to handle each stream
		this.inThread = new RobotReciever(robot, msg, in);
		this.outThread = new RobotSender(robot, msg, out);

		// run each thread
		inThread.start();
		outThread.start();
		this.connected = true;
	}

	@Override
	public void interrupt() {
		System.out.println("Client interupted");
		// attempt to close the streams gracefully
		inThread.interrupt();
		outThread.interrupt();
	}
	/**
	 * This will start communication with the server, 
	 * it will hog the thread until a connection has been established
	 */
	public void launch() {
		this.start();
		while (!connected) {
			Delay.msDelay(1000);
		}
	}
	
	public static void main(String[] args) {
		Robot r;
		// r = new Robot("Keith", "0016530FDDAE", new Location(0, 0), new Location(0, 0));
		r = new Robot("Cell", "0016531AFA0B", new Location(0, 0), new Location(1, 0));
		Message m = new Message(new ArrayList<move>(), command.Wait);
		System.out.println("constructing client");
		Client c = new Client(r, m);
		System.out.println("running");
		c.launch();
		while (true) {
			System.out.println(r.getCurrentLocation().getX());
			Delay.msDelay(5000);
		}
	}
}
