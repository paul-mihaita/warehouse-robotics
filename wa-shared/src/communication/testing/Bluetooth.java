package communication.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import communication.Message;
import communication.thread.Server;
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
	
	@Before
	public void setUp() {
		Robot[] robots = Info.getRobotsVector();
		Message[] msgs = new Message[robots.length];
		for (int i = 0; i < msgs.length; i++) {
			BasicJob job = new BasicJob(1, new Task("a", 0));
			msgs[i] = new Message(new ArrayList<move>(), command.Wait, job);
		}
		Server s = new Server(robots, msgs, log);
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
