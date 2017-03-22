package communication.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import communication.Message;
import communication.thread.Server;
import lejos.util.Delay;
import movement.Movement.move;
import communication.BasicJob;
import communication.CommConst.command;
import utils.Info;
import utils.Robot;
import utils.Task;

/**
 * @author Lexer747, Alex Lewis
 *
 */
public class Bluetooth {

	public static final Logger log = Logger.getLogger(Bluetooth.class);
	Robot[] robots = Info.getRobotsVector();
	Message[] msgs = new Message[robots.length];

	@Before
	public void setUp() {
		for (int i = 0; i < msgs.length; i++) {
			BasicJob job = new BasicJob(1, new Task("a", 0));
			msgs[i] = new Message(new ArrayList<move>(), command.Wait, job);
		}
		Server s = new Server(robots, msgs, log);
		s.launch(); // wait for the robots to connect
	}

	@Test
	public void test() {
		Robot[] previous = new Robot[robots.length];
		for (int i = 0; i < previous.length; i++) {
			previous[i] = robots[i].cloneRobot();
		}
		for (int i = 0; i < msgs.length; i++) {
			msgs[i].setCommand(command.Finish); 
			//send a bluetooth command
			//the robots array will be changed by robots
		}
		//wait for the response
		Delay.msDelay(5000);
		for (int i = 0; i < previous.length; i++) {
			assertTrue(!compareRobot(previous[i], robots[i]));
		}
	}

	private boolean compareRobot(Robot x, Robot y) {
		return (x.getOrientation().equals(y.getOrientation()))
				&& (x.getCurrentLocation().equals(y.getCurrentLocation())) && (x.getName().equals(y.getName()));
	}

}
