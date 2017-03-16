package main.gui;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import main.model.WarehouseFloor;
import utils.Job;
import utils.Job.Status;
import utils.Task;

public class JobPane extends GridPane {
	
	private static HashMap<Label, Job> jobStatus;

	public JobPane(WarehouseFloor model) {
		super();
		
		JobPane.jobStatus = new HashMap<Label, Job>();

		Label idLabel = new Label("ID");
		idLabel.setFont(new Font(17));

		this.add(idLabel, 0, 0);
		
		Label statusLabel = new Label("Status");
		statusLabel.setFont(new Font(17));
		
		this.add(statusLabel, 1, 0);
		
		Label taskLabel = new Label("Tasks");
		taskLabel.setFont(new Font(17));
		
		this.add(taskLabel, 2, 0);
		
		Label cancelButtons = new Label("Cancel");
		cancelButtons.setFont(new Font(17));
		
		this.add(cancelButtons, 3, 0);


		int level = 1;
		for (Job j : model.getJobs().values()) {

			Label id = new Label("" + j.getJobID());

			Label status = new Label(j.getStatus().toString());

			String text = "";
			for (Task t : j.getTasks()) {
				text += t.toString();
			}

			Label task = new Label(text);

			Button cancel = new Button("Cancel");
			cancel.setTextFill(Color.RED);
			cancel.setOnAction(e -> {
				model.cancelJob(j);
				JobPane.updateLabels();
				RobotPane.updateLabels();
			});

			jobStatus.put(status, j);

			this.add(id, 0, level);
			this.add(status, 1, level);
			this.add(task, 2, level);
			this.add(cancel, 3, level++);
		}
	}
	
	public static void updateLabels(){
		
		for (Label l : jobStatus.keySet()){
			l.setText(jobStatus.get(l).getStatus().toString());
			l.setTextFill(colorPicker(jobStatus.get(l).getStatus()));
		}
		
	}

	private static Paint colorPicker(Status status) {
		switch (status) {
		case COMPLETED:
			return Color.GREEN;
		case SELECTED:
			return Color.YELLOW;
		case NOT_SELECTED:
			return Color.BLACK;
		case ACTIVE:
			return Color.YELLOWGREEN;
		case CANCELED:
			return Color.RED;
		}
		return Color.PURPLE;
	}
}
