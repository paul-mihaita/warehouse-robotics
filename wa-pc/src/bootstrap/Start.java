package bootstrap;

import org.apache.log4j.Logger;

import main.job.Input;
import main.pc_gui.GUI;
import utils.Location;
import utils.Robot;

public class Start {

	private static Logger log = Logger.getLogger(Start.class);

	private static Robot r = new Robot("Keith", "0016530FDDAE", new Location(0, 1), new Location(0, 0));
	public static void main(String[] args) {
		log.info("Starting");
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		GUI.create(in.getJobsArray());

	}

}
