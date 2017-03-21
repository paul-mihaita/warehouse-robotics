package main.gui;

import java.util.HashMap;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import main.gui.listeners.RobotCancel;
import main.gui.listeners.StartListener;
import main.model.WarehouseFloor;
import utils.Robot;
import utils.Task;
import utils.Tuple;

public class RobotPane extends GridPane {

	private static HashMap<Robot, Tuple<Label, Label>> robotLabels;
	private static WarehouseFloor model;

	public RobotPane(WarehouseFloor model) {
		super();

		RobotPane.model = model;
		RobotPane.robotLabels = new HashMap<Robot, Tuple<Label, Label>>();

		this.setMaxHeight(GUI.HEIGHT);
		this.setPrefWidth(GUI.SIDEBAR_WIDTH);
		this.setHgap(20);
		Button startButton = new Button("Start");

		startButton.setTextFill(Color.GREEN);
		startButton.setTextAlignment(TextAlignment.CENTER);
		startButton.setFont(new Font(20));

		startButton.setPrefWidth(GUI.SIDEBAR_WIDTH);
		
		startButton.setOnAction(new StartListener(model));

		this.add(startButton, 0, 1);

		GridPane robotGrid = new GridPane();

		robotGrid.setAlignment(Pos.CENTER);
		robotGrid.setHgap(GUI.SIDEBAR_WIDTH / 70);
		robotGrid.setVgap(GUI.SIDEBAR_WIDTH / 70);

		int level = 0;

		for (Robot r : model.getRobots()) {

			GridPane robotPane = new GridPane();
			
			robotPane.setAlignment(Pos.BASELINE_RIGHT);

			robotPane.setMaxWidth(GUI.SIDEBAR_WIDTH - 20);

			robotPane.setStyle("-fx-border-color: gray");

			Label l = new Label("Name: " + r.getName());

			Button cancelb = new Button("Cancel Job");

			cancelb.setTextFill(Color.CRIMSON);
			cancelb.setMinWidth(75);
			cancelb.setOnAction(new RobotCancel(model, r));
			
			Label s = new Label("Job status:");

			String text;

			if (model.getJob(r).isPresent()) {
				text = model.getJob(r).get().getStatus().toString();
			} else {
				text = "UNASSIGNED";
			}

			Label status = new Label(text);

			status.setTextFill(statusColor(text));

			Label idText = new Label("Job ID:");

			if (model.getJob(r).isPresent()) {
				text = "" + model.getJob(r).get().getJobID();
			} else {
				text = "UNASSIGNED";
			}

			Label jobId = new Label(text);

			robotPane.setAlignment(Pos.CENTER_LEFT);
			robotPane.setHgap(10);
			robotPane.setVgap(10);

			robotPane.add(l, 0, 0);
			robotPane.add(cancelb, 1, 0);

			robotPane.add(idText, 0, 1);
			robotPane.add(jobId, 1, 1);

			robotPane.add(s, 0, 2);
			robotPane.add(status, 1, 2);

			if (model.getJob(r).isPresent()) {

				GridPane taskPane = new GridPane();
				taskPane.setStyle("-fx-border-color: gray");
				taskPane.setAlignment(Pos.BASELINE_CENTER);
				taskPane.setVgap(10);
				taskPane.setHgap(30);

				int i = 0;

				for (Task t : model.getJob(r).get().getTasks()) {

					Label itemName = new Label(t.getItem().getItemName());
					Label itemQuantity = new Label("" + t.getQuantity());

					taskPane.add(itemName, 0, i);
					taskPane.add(itemQuantity, 1, i++);

				}

				robotPane.add(taskPane, 0, 3, 3, 3);
			}

			robotLabels.put(r, new Tuple<Label, Label>(jobId, status));

			robotGrid.add(robotPane, 0, level++);
		}

		this.add(robotGrid, 0, 2);
	}

	public static void updateLabels() {
		for (Robot r : robotLabels.keySet()) {
			Tuple<Label, Label> t = robotLabels.get(r);
			String text;

			if (model.getJob(r).isPresent()) {
				text = "" + model.getJob(r).get().getJobID();
			} else {
				text = "UNASSIGNED";
			}

			t.getX().setText(text);

			if (model.getJob(r).isPresent()) {
				text = model.getJob(r).get().getStatus().toString();
			} else {
				text = "UNASSIGNED";
			}

			t.getY().setText(text);

		}
	}

	private static Paint statusColor(String status) {
		/*
		 * aCtive, iNactive, cOmpleted, cAnceled
		 */
		switch (status.charAt(1)) {
		case 'C':
			return Color.GREEN;
		case 'N':
			return Color.BLACK;
		case 'O':
			return Color.BLUE;
		case 'A':
			return Color.RED;
		default:
			return Color.GRAY;
		}
	}

}