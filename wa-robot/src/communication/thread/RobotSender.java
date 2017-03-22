package communication.thread;

import java.io.IOException;
import java.io.OutputStream;

import communication.CommConst;
import communication.CommConst.protocol;
import communication.Message;
import communication.NXTOutputStream;
import lejos.util.Delay;
import utils.Robot;

public class RobotSender extends Thread {

	private Robot robot;
	private Message msg;
	private OutputStream connection;
	private NXTOutputStream toPC;
	private boolean running = true;

	public RobotSender(Robot robot, Message msg, OutputStream connection) {
		this.robot = robot;
		this.msg = msg;
		this.connection = connection;
		this.toPC = new NXTOutputStream(connection);
	}

	@Override
	public void run() {
		while (running) {
			Delay.msDelay(CommConst.GRACE);
			if (robot.needsUpdate()) {
				try {
					toPC.sendRobot(robot);
					robot.updated();
				} catch (IOException e) {
					System.out.println("Couldn't send robot object");
					e.printStackTrace();
				}
				continue;
			}
			if (msg.needsUpdate()) {
				try {
					toPC.sendMessage(msg);
					msg.updated();
				} catch (IOException e) {
					System.out.println("Couldn't send message object");
					e.printStackTrace();
				}
			}
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
