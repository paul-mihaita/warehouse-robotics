package main.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

	private static HashMap<Robot, ArrayList<Label>> robotLabels;
	private static HashMap<Robot, GridPane> robotItems;
	private static WarehouseFloor model;

	public RobotPane(WarehouseFloor model) {
		super();

		RobotPane.model = model;
		RobotPane.robotLabels = new HashMap<Robot, ArrayList<Label>>();
		RobotPane.robotItems = new HashMap<Robot, GridPane>();

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
				robotItems.put(r, taskPane);
			}

			ArrayList<Label> labels = new ArrayList<Label>();
			labels.add(jobId);
			labels.add(status);
			robotLabels.put(r, labels);

			robotGrid.add(robotPane, 0, level++);
		}

		this.add(robotGrid, 0, 2);
	}

	public static void updateLabels() {
		for (Robot r : robotLabels.keySet()) {
			ArrayList<Label> t = robotLabels.get(r);
			String text;

			if (model.getJob(r).isPresent()) {
				text = "" + model.getJob(r).get().getJobID();
			} else {
				text = "UNASSIGNED";
			}

			t.get(0).setText(text);

			if (model.getJob(r).isPresent()) {
				text = model.getJob(r).get().getStatus().toString();
			} else {
				text = "UNASSIGNED";
			}

			t.get(1).setText(text);

		}
	}

	public static void updateItems() {
		for (Robot r : model.getRobots()) {
			GridPane pane = robotItems.get(r);
			if (model.getJob(r).isPresent() && !model.getJob(r).get().getTasks().isEmpty()) {

				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						pane.getChildren().removeIf(new Predicate<Node>() {
							@Override
							public boolean test(Node arg0) {
								GUI.log.debug("Items nodes removed");
								return true;
							}

						});

						int i = 0;

						for (Task t1 : model.getJob(r).get().getTasks()) {

							Label itemName = new Label(t1.getItem().getItemName());
							Label itemQuantity = new Label("" + t1.getQuantity());

							pane.add(itemName, 0, i);
							pane.add(itemQuantity, 1, i++);

							GUI.log.debug("Items nodes populated");

						}
					}
				});

			}
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