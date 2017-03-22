package main.gui;

import java.util.HashMap;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import main.gui.listeners.JobCancel;
import main.model.WarehouseFloor;
import utils.Job;
import utils.Job.Status;
import utils.Task;

public class JobPane extends GridPane {

	private static HashMap<Label, Job> jobStatus;

	public JobPane(WarehouseFloor model) {
		super();

		this.setHgap(30);

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

		ScrollPane jobData = new ScrollPane();
		jobData.setMaxHeight(GUI.HEIGHT);
		jobData.setHbarPolicy(ScrollBarPolicy.NEVER);
		jobData.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		GridPane dataHolder = new GridPane();
		dataHolder.setStyle("-fx-border-color: gray");

		int level = 0;
		for (Job j : model.getJobs().values()) {

			Label id = new Label("" + j.getJobID());

			Label status = new Label(j.getStatus().toString());

			status.setTextFill(colorPicker(j.getStatus()));

			ComboBox<String> task = new ComboBox<String>();
			task.setEditable(false);
			task.setPrefWidth(15);

			for (Task t : j.getTasks()) {
				task.getItems().add(t.toString());
			}

			Button cancel = new Button("Cancel");
			cancel.setTextFill(Color.RED);
			cancel.setOnAction(new JobCancel(model, j));

			jobStatus.put(status, j);

			dataHolder.add(id, 0, level);
			dataHolder.add(status, 1, level);
			dataHolder.add(task, 2, level);
			dataHolder.add(cancel, 3, level++);
		}

		jobData.setContent(dataHolder);
		this.add(jobData, 0, 1, 4, 1);
	}

	public static void updateLabels() {

		for (Label l : jobStatus.keySet()) {
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
