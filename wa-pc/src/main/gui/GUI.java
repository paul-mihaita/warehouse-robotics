package main.gui;

import java.util.HashMap;

import graph_entities.IEdge;
import graph_entities.IVertex;
import javafx.animation.AnimationTimer;
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
import main.model.WarehouseFloor;
import student_solution.Graph;
import utils.Location;
import utils.Robot;

public class GUI extends Application {

	// For the moment while there is no map these are not being used
	public static final int ROBOT_WIDTH = 220;
	public static final int MAP_WIDTH = 600;

	public static final int WIDTH = ROBOT_WIDTH + MAP_WIDTH;

	public static final int HEIGHT = 390;

	private static HashMap<Robot, Label> robotLabels;

	private static WarehouseFloor model;

	private static AnimationTimer timer;

	/**
	 * 
	 * Creates a new GUI with the input being the list of jobs in preferential
	 * order
	 * 
	 * @param List
	 *            of jobs in preferential order, stating with most
	 */
	public static void create(WarehouseFloor model) {

		GUI.robotLabels = new HashMap<Robot, Label>();
		GUI.model = model;
		launch();
	}

	/**
	 * Pleeeeeeeeeease don't use this, use create(jobs) instead xxx
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Warehouse Controller");
		primaryStage.setMinHeight(HEIGHT);
		primaryStage.setMinWidth(WIDTH);
		// primaryStage.getIcons().add(new Image("icon.jpg"));

		GridPane robotHolder = GUI.createRobotPane();

		Canvas map = GUI.createMapPane();

		BorderPane guiHolder = new BorderPane();

		guiHolder.setLeft(robotHolder);
		guiHolder.setCenter(map);

		primaryStage.setScene(new Scene(guiHolder));
		primaryStage.show();

	}

	private static Canvas createMapPane() {

		// Floor map is 12 x 8

		Canvas map = new Canvas();

		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {

				GraphicsContext gc = map.getGraphicsContext2D();

				map.setWidth(MAP_WIDTH);
				map.setHeight(HEIGHT);

				Graph<Location> floorMap = model.getFloorGraph();

				for (IVertex<Location> v : floorMap.getVertices()) {
					GUI.drawNode(v, gc);
					GUI.drawEdges(v, gc);
				}

				GUI.drawRobots(gc);
			}
		};

		timer.start();
		return map;
	}

	private static void drawEdges(IVertex<Location> v, GraphicsContext gc) {

		Location parent = v.getLabel().getData();

		for (IEdge<Location> e : v.getSuccessors()) {

			Location child = e.getTgt().getLabel().getData();

			gc.setStroke(Color.LIGHTSLATEGRAY);
			gc.strokeLine(scale(parent.getX()) + 5, scale(parent.getY()) + 5, scale(child.getX()) + 5,
					scale(child.getY()) + 5);
		}

	}

	private static void drawNode(IVertex<Location> v, GraphicsContext gc) {

		Location l = v.getLabel().getData();

		gc.setFill(Color.DARKMAGENTA);
		gc.fillOval(scale(l.getX()), scale(l.getY()), 10, 10);

	}

	private static void drawRobots(GraphicsContext gc) {
		for (Robot r : model.getRobots()) {
			gc.setFill(Color.DARKGREY);
			gc.fillRect(scale(r.getCurrentLocation().getX()) - 5, scale(r.getCurrentLocation().getY()) - 5, 20, 20);
			r.getOrientation();
		}
	}

	private static float scale(float value) {

		// Factors to make the drawing central and pretty

		float scale = (float) (MAP_WIDTH / 11.5);

		return value * scale + 5;
	}

	private static GridPane createRobotPane() {

		GridPane robotHolder = new GridPane();

		robotHolder.setMaxHeight(HEIGHT);
		robotHolder.setPrefWidth(ROBOT_WIDTH);

		Label robotLabel = new Label("Robots");

		robotLabel.setFont(new Font(20));

		robotHolder.add(robotLabel, 0, 0);

		Button startButton = new Button("Start");

		startButton.setTextFill(Color.GREEN);
		startButton.setTextAlignment(TextAlignment.CENTER);
		startButton.setFont(new Font(20));

		startButton.setMinWidth(ROBOT_WIDTH);

		startButton.setOnAction(e -> {
			model.startRobots();
			GUI.updateLabels();
		});

		robotHolder.add(startButton, 0, 1);

		ScrollPane robotDisplay = new ScrollPane();

		robotDisplay.setMaxHeight(HEIGHT);
		robotDisplay.setMinWidth(ROBOT_WIDTH);

		robotDisplay.setHbarPolicy(ScrollBarPolicy.NEVER);
		robotDisplay.setVbarPolicy(ScrollBarPolicy.ALWAYS);

		GridPane robotGrid = new GridPane();

		robotGrid.setAlignment(Pos.CENTER);
		robotGrid.setHgap(ROBOT_WIDTH / 100);
		robotGrid.setVgap(ROBOT_WIDTH / 100);

		int level = 0;

		for (Robot r : model.getRobots()) {

			GridPane robotPane = new GridPane();

			robotPane.setMaxWidth(ROBOT_WIDTH - 20);

			robotPane.setStyle("-fx-border-color: gray");

			Label l = new Label("Name: " + r.getName());

			Button b = new Button("Cancel Job");

			b.setTextFill(Color.CRIMSON);
			b.setMinWidth(75);

			Label s = new Label("Job status:");

			s.setFont(new Font(15));

			String text;

			if (model.getJob(r).isPresent()) {
				text = model.getJob(r).get().getStatus();
			} else {
				text = "UNASSIGNED";
			}

			Label status = new Label(text);

			status.setTextFill(statusColor(text));

			b.setOnAction(e -> {
				model.cancelJob(r);
				GUI.updateLabels();
			});

			Label idText = new Label("Job ID:");

			if (model.getJob(r).isPresent()) {
				text = "" + model.getJob(r).get().getJobID();
			} else {
				text = "UNASSIGNED";
			}

			Label jobId = new Label(text);

			robotPane.setAlignment(Pos.CENTER_LEFT);
			robotPane.setHgap(ROBOT_WIDTH / 10);
			robotPane.setVgap(ROBOT_WIDTH / 10);

			robotPane.add(l, 0, 0);
			robotPane.add(s, 0, 1);

			robotPane.add(b, 1, 0);
			robotPane.add(status, 1, 1);

			robotPane.add(idText, 0, 2);
			robotPane.add(jobId, 1, 2);

			robotLabels.put(r, status);

			robotGrid.add(robotPane, 0, level++);
		}

		robotDisplay.setContent(robotGrid);
		robotHolder.add(robotDisplay, 0, 2);

		return robotHolder;
	}

	public static void updateLabels() {
		// TODO this
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