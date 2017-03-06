package communication.thread;

import java.io.DataInputStream;
import java.io.OutputStream;

import communication.Message;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import utils.Robot;

public class Client extends Thread {
	private DataInputStream in;
	private OutputStream out;
	private Robot robot;
	private Message msg;
	private RobotReciever inThread;
	private RobotSender outThread;

	public Client(Robot robot, Message msg) {
		BTConnection comm = Bluetooth.waitForConnection();
		//wait for PC
		
		//open streams
		this.in = comm.openDataInputStream();
		this.out = comm.openOutputStream();
		this.robot = robot;
		this.msg = msg;
	}

	@Override
	public void run() {
		//construct threads to handle each stream
		this.inThread = new RobotReciever(robot, msg, in);
		this.outThread = new RobotSender(robot, msg, out);
		
		//run each thread
		inThread.run();
		outThread.run();
	}

	@Override
	public void interrupt() {
		System.out.println("Client interupted");
		//attempt to close the streams gracefully
		inThread.interrupt();
		outThread.interrupt();
	}

}
