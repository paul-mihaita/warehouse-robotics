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
			if (robot.needsUpdate()) {
				try {
					toPC.sendProtocol(protocol.Robot);
					toPC.sendRobot(robot);
					robot.updated();
				} catch (IOException e) {
					System.out.println("Couldn't send robot object");
					e.printStackTrace();
				}
				continue;
			}
			if (msg.needsUpdate()) {
				System.out.println("wanted to send message");
				/*
				 * try { toPC.sendProtocol(protocol.Movement);
				 * toPC.sendMoves(msg.getMoves());
				 * toPC.sendProtocol(protocol.Command);
				 * toPC.sendCommand(msg.getCommand()); msg.updated(); } catch
				 * (IOException e) {
				 * System.out.println("Couldn't send message object");
				 * e.printStackTrace(); }
				 */
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
