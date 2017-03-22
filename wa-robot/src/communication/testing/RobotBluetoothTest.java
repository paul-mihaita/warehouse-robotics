package communication.testing;

import communication.Message;

import java.util.ArrayList;

import communication.BasicJob;
import communication.CommConst.command;
import communication.thread.Client;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Info;
import utils.Location;
import utils.Robot;
import utils.Task;

public class RobotBluetoothTest {
	public static void main(String[] args) {
		Robot[] robots = Info.getRobotsVector();
		int rob = 0; //0 = s, 1 = b, 2 = c;
		BasicJob job = new BasicJob(0, new Task("a", 0));
		Message m = new Message(new ArrayList<move>(), command.Wait, job);
		Client c = new Client(robots[rob], m);
		c.launch(); //wait for pc to connect
		while (m.getCommand() != command.Finish) {
			Delay.msDelay(100); //wait for pc to tell us what to do
		}
		robots[rob].setCurrentLocation(new Location(15, 15));
		//send a robot update
	}
}
