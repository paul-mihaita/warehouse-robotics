package bootstrap;

import main.job.Input;
import main.pc_gui.GUI;

public class Start {

	public static void main(String[] args) {
		
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		GUI.create(in.getJobsArray());

	}

}
