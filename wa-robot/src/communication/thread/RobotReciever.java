package communication.thread;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import communication.PCInputStream;
import comunication.CommConst.command;
import movement.Movement.move;
import utils.Robot;

public class RobotReciever extends Thread {

	private Robot robot;
	private DataInputStream connection;
	private PCInputStream fromPC;
	private boolean running;
	private List<move> path;
	private command nextCmd;

	public RobotReciever(Robot robot, DataInputStream connection, List<move> path, command nextCmd) {
		this.robot = robot;
		this.path = path;
		this.nextCmd = nextCmd;
		this.connection = connection;
		this.fromPC = new PCInputStream(connection);
	}

	@Override
	public void run() {
		while (running) {
			try {
				switch (fromPC.readProtocol()) {
					case Movement:
						path = fromPC.readMoves();
						break;
					case Robot:
						robot = fromPC.readRobot();
						break;
					case Command:
						nextCmd = fromPC.readCommand();
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void interrupt() {
		running = false;
		try {
			connection.close();
			fromPC.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
