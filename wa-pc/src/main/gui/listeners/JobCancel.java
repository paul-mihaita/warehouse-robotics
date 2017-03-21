package main.gui.listeners;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.gui.JobPane;
import main.gui.RobotPane;
import main.model.WarehouseFloor;
import utils.Job;
import utils.Robot;

public class JobCancel implements EventHandler<ActionEvent> {

	private WarehouseFloor model;
	private Job j;

	public JobCancel(WarehouseFloor model, Job j) {
		this.model = model;
		this.j = j;
	}

	@Override
	public void handle(ActionEvent event) {

		model.cancelJob(j);
		model.reassignJobs();
		JobPane.updateLabels();
		RobotPane.updateLabels();

		for (Robot r : model.getRobots()) {
			if (model.getJob(r).equals(j)) {

			}
		}

	}

}
