package pc_gui;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Job;

public class GUI extends Application {

	// For the moment while there is no map
	public static final int WIDTH = 155;
	public static final int HEIGHT = 600;

	public static final int JOB_WIDTH = 155;
	public static final int JOB_HEIGHT = 600;

	private static ArrayList<Job> jobs;
	private static HashMap<Job, Label> jobLabels;

	/**
	 * 
	 * Creates a new GUI with the input being the list of jobs in preferential
	 * order
	 * 
	 * @param List
	 *            of jobs in preferential order, stating with most
	 */
	public static void create(ArrayList<Job> jobs) {
		GUI.jobs = jobs;
		GUI.jobLabels = new HashMap<Job, Label>();
		launch();
	}

	/**
	 * Pleeeeeeeeeease don't use this, use create(job) instead xxx
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Warehouse Controller");

		GridPane jobHolder = new GridPane();

		jobHolder.setMaxHeight(JOB_HEIGHT);
		jobHolder.setPrefWidth(JOB_WIDTH);

		Button startButton = new Button("Start");

		startButton.setTextFill(Color.GREEN);
		startButton.setTextAlignment(TextAlignment.CENTER);
		startButton.setFont(new Font(20));

		startButton.setMinWidth(JOB_WIDTH);

		startButton.setOnAction(e -> GUI.startJobs());

		jobHolder.add(startButton, 0, 0);

		ScrollPane jobDisplay = new ScrollPane();

		jobDisplay.setMaxHeight(JOB_HEIGHT);
		jobDisplay.setMinWidth(JOB_WIDTH);

		jobDisplay.setHbarPolicy(ScrollBarPolicy.NEVER);
		jobDisplay.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		GridPane jobGrid = new GridPane();

		jobGrid.setAlignment(Pos.CENTER);
		jobGrid.setHgap(JOB_WIDTH / 100);
		jobGrid.setVgap(JOB_WIDTH / 100);

		int level = 1;
		for (Job j : jobs) {

			GridPane jobPane = new GridPane();

			jobPane.setMaxWidth(JOB_WIDTH - 20);

			jobPane.setStyle("-fx-border-color: blue");

			Label l = new Label("Job ID: " + j.getJobID());
			Button b = new Button("Cancel");
			Label s = new Label("Status:");
			Label status = new Label(j.getStatus());

			status.setTextFill(statusColor(j.getStatus()));

			b.setOnAction(e -> GUI.cancelJob(j));

			jobPane.setAlignment(Pos.CENTER_LEFT);
			jobPane.setHgap(JOB_WIDTH / 10);
			jobPane.setVgap(JOB_WIDTH / 10);

			jobPane.add(l, 0, 0);
			jobPane.add(s, 0, 1);

			jobPane.add(b, 1, 0);
			jobPane.add(status, 1, 1);

			jobLabels.put(j, status);

			jobGrid.add(jobPane, 0, level++);
		}

		jobDisplay.setContent(jobGrid);
		jobHolder.add(jobDisplay, 0, 1);

		primaryStage.setScene(new Scene(jobHolder));
		primaryStage.show();

	}

	private static void cancelJob(Job j) {
		j.cancel();
		GUI.update();
	}

	private static void startJobs() {
		for (Job j : jobs) {
			if (j.isInactive()) {
				j.start();
			}
		}
		GUI.update();
	}

	/**
	 * Updates the job status labels. Shouldn't need to use this method as it
	 * should be handled in the GUI
	 */
	public static void update() {
		for (Job j : jobLabels.keySet()) {
			jobLabels.get(j).setText(j.getStatus());
			jobLabels.get(j).setTextFill(statusColor(j.getStatus()));
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