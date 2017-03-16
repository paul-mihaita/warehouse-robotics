package main.gui.listeners;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.gui.JobPane;
import main.gui.RobotPane;
import main.model.WarehouseFloor;
import utils.Job;
import utils.Robot;

public class RobotCancel implements EventHandler<ActionEvent> {

	private WarehouseFloor model;
	private Robot robot;

	public RobotCancel(WarehouseFloor model, Robot robot) {
		super();
		this.model = model;
		this.robot = robot;
	}

	@Override
	public void handle(ActionEvent event) {

		model.getJob(robot).ifPresent(new Consumer<Job>() {
			@Override
			public void accept(Job t) {
				t.cancel();
				RobotPane.updateLabels();
				JobPane.updateLabels();
			}
		});
	}
}