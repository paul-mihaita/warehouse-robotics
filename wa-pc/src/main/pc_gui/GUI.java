package main.pc_gui;

import java.util.ArrayList;
import java.util.HashMap;

import graph_entities.IEdge;
import graph_entities.IVertex;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.WarehouseFloor;

public class GUI extends Application {

	// For the moment while there is no map these are not being used
	public static final int JOB_WIDTH = 170;

	public static final int MAP_WIDTH = 600;

	public static final int WIDTH = JOB_WIDTH + MAP_WIDTH;
	public static final int HEIGHT = 400;

	private static ArrayList<Job> jobs;
	private static HashMap<Job, Label> jobLabels;

	private static WarehouseFloor model;

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
		GUI.model = new WarehouseFloor(null, Planning.createGraph(MapUtils.createRealWarehouse()));
		launch();
	}

	/**
	 * Pleeeeeeeeeease don't use this, use create(jobs) instead xxx
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Warehouse Controller");

		BorderPane guiHolder = new BorderPane();

		GridPane jobHolder = GUI.createJobPane();

		Canvas map = GUI.createMapPane();

		guiHolder.setRight(jobHolder);
		guiHolder.setCenter(map);

		primaryStage.setScene(new Scene(guiHolder));
		primaryStage.show();

	}

	private static Canvas createMapPane() {

		// Floor map is 12 x 8

		Canvas map = new Canvas();
		GraphicsContext gc = map.getGraphicsContext2D();

		map.setWidth(MAP_WIDTH);
		map.setHeight(HEIGHT);

		Graph<Location> floorMap = model.getFloorGraph();

		for (IVertex<Location> v : floorMap.getVertices()) {
			GUI.drawNode(v, gc);
			GUI.drawEdges(v, gc);
		}

		return map;
	}

	private static void drawEdges(IVertex<Location> v, GraphicsContext gc) {

		Location parent = v.getLabel().getData();

		for (IEdge<Location> e : v.getSuccessors()) {

			Location child = e.getTgt().getLabel().getData();
			
			gc.setStroke(Color.LIGHTSLATEGRAY);
			gc.strokeLine(scale(parent.getX()) + 5, scale(parent.getY()) + 5, scale(child.getX()) + 5, scale(child.getY()) + 5);
		}

	}

	private static void drawNode(IVertex<Location> v, GraphicsContext gc) {

		Location l = v.getLabel().getData();

		gc.setFill(Color.DARKMAGENTA);
		gc.fillOval(scale(l.getX()), scale(l.getY()), 10, 10);

	}

	private static float scale(float value) {

		// Factors to make the drawing central and pretty

		float scale = (float) (MAP_WIDTH / 11.5);

		return value * scale + 5;
	}

	private static GridPane createJobPane() {
		GridPane jobHolder = new GridPane();

		jobHolder.setMaxHeight(HEIGHT);
		jobHolder.setPrefWidth(JOB_WIDTH);

		Button startButton = new Button("Start");

		startButton.setTextFill(Color.GREEN);
		startButton.setTextAlignment(TextAlignment.CENTER);
		startButton.setFont(new Font(20));

		startButton.setMinWidth(JOB_WIDTH);

		startButton.setOnAction(e -> GUI.startJobs());

		jobHolder.add(startButton, 0, 0);

		ScrollPane jobDisplay = new ScrollPane();

		jobDisplay.setMaxHeight(HEIGHT);
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

			jobPane.setStyle("-fx-border-color: gray");

			Label l = new Label("ID: " + j.getJobID());
			Button b = new Button("Cancel");
			b.setTextFill(Color.CRIMSON);
			b.setMinWidth(75);
			Label s = new Label("Status:");
			s.setFont(new Font(15));
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

		return jobHolder;
	}

	private static void cancelJob(Job j) {
		j.cancel();
		GUI.updateJobLabels();
	}

	private static void startJobs() {
		for (Job j : jobs) {
			if (j.isInactive()) {
				j.start();
			}
		}
		GUI.updateJobLabels();
	}

	private static void updateJobLabels() {
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