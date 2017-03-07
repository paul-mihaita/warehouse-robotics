package bootstrap;

import org.apache.log4j.Logger;

import main.job.Input;
import main.pc_gui.GUI;

public class Start {

	private static Logger log = Logger.getLogger(Start.class);


	public static void main(String[] args) {

		log.info("Starting");
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		GUI.create(in.getJobsArray());
	}

}
