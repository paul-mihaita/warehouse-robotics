package communication.thread;

import java.io.IOException;
import java.io.OutputStream;

import communication.NXTOutputStream;
import comunication.CommConst;
import comunication.CommConst.protocol;
import comunication.Message;
import lejos.util.Delay;
import utils.Robot;

public class RobotSender extends Thread {

	private Robot robot;
	private Message msg;
	private OutputStream connection;
	private NXTOutputStream toPC;
	private boolean running;

	public RobotSender(Robot robot, Message msg, OutputStream connection) {
		this.robot = robot;
		this.msg = msg;
		this.connection = connection;
		this.toPC = new NXTOutputStream(connection);
	}

	@Override
	public void run() {
		while (running) {
			if (robot.needsUpdate()) {
				try {
					toPC.sendProtocol(protocol.Robot);
					toPC.sendRobot(robot);
				} catch (IOException e) {
					System.out.println("Couldn't send robot object");
					e.printStackTrace();
				}
				continue;
			}
			if (msg.needsUpdate()) {
				try {
					toPC.sendProtocol(protocol.Movement);
				} catch (IOException e) {
					System.out.println("Couldn't send message object");
					e.printStackTrace();
				}
			}
			Delay.msDelay(CommConst.GRACE);
		}
	}

	@Override
	public void interrupt() {
		running = false;
		try {
			connection.close();
			toPC.close();
		} catch (IOException e) {
			System.out.println("Couldn't close connection");
			e.printStackTrace();
		}
	}
}
