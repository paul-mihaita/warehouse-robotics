package communication.event;

import java.io.IOException;
import java.util.List;

import communication.PCInputStream;
import comunication.CommConst.command;
import comunication.Message;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import movement.Movement.move;
import utils.Robot;

public class RobotReciever extends Thread{
	
	private Robot robot;
	private BTConnection connection;
	private PCInputStream fromPC;
	private boolean running;
	private List<move> path;
	private command nextCmd;
	public RobotReciever(Robot robot, List<move> path, command nextCmd) {
		this.robot = robot;
		this.path = path;
		this.nextCmd = nextCmd;
		initConnection();
	}
	private void initConnection() {
		this.connection = Bluetooth.waitForConnection();
		this.fromPC = new PCInputStream(connection.openDataInputStream());
	}
	@Override
	public void run() {
		while (running) {
			try {
				switch(fromPC.readProtocol()) {
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
		connection.close();
		try {
			fromPC.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
