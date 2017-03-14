package main.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.jfree.util.Log;
import bootstrap.Start;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import main.model.WarehouseFloor;
import rp.util.Rate;
import student_solution.Graph;
import utils.Item;
import utils.Location;
import utils.Robot;
import utils.Task;
import utils.Tuple;

public class GUI extends Application {

	// For the moment while there is no map these are not being used
	public static final int ROBOT_WIDTH = 230;
	public static final int MAP_WIDTH = 600;

	public static final int WIDTH = ROBOT_WIDTH + MAP_WIDTH;

	public static final int HEIGHT = 390;

	private static HashMap<Robot, Tuple<Label, Label>> robotLabels;

	private static WarehouseFloor model;

	private static Thread canvasHandler;
	private static Thread nodeAnimator;

	private static ArrayList<Location> nodesToDraw;

	private static HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> coloredPath;
	private static HashSet<ArrayList<ArrayList<Location>>> paths;

	/**
	 * 
	 * Creates a new GUI with the input being the list of jobs in preferential
	 * order
	 * 
	 * @param List
	 *            of jobs in preferential order, stating with most
	 */
	public static void create(WarehouseFloor model) {

		GUI.paths = new HashSet<ArrayList<ArrayList<Location>>>();
		GUI.coloredPath = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>();
		GUI.robotLabels = new HashMap<Robot, Tuple<Label, Label>>();
		GUI.nodesToDraw = new ArrayList<Location>();
		GUI.model = model;
		launch();
	}

	/**
	 * Pleeeeeeeeeease don't use this, use create(jobs) instead xxx
	 */
	@Override
	public void start(Stage primaryStage) {

		primaryStage.setTitle("Warehouse Controller");
		primaryStage.setMinHeight(HEIGHT);
		primaryStage.setMinWidth(WIDTH);

		File alex = new File("alex.png");
		if (alex.exists()) {
			Start.log.debug(alex.getAbsolutePath());
			primaryStage.getIcons().add(new Image("file:" + alex.getAbsolutePath()));
		}

		TabPane tabPane = GUI.createTabPane();

		Canvas map = GUI.createMapPane();

		BorderPane guiHolder = new BorderPane();

		guiHolder.setLeft(tabPane);
		guiHolder.setCenter(map);

		primaryStage.setScene(new Scene(guiHolder));
		primaryStage.show();

	}

	private static TabPane createTabPane() {
		TabPane pane = new TabPane();
		pane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		GridPane robotHolder = GUI.createRobotPane();
		pane.getTabs().add(new Tab("Robots", robotHolder));
		return pane;
	}

	@Override
	public void stop() {
		canvasHandler.interrupt();
		nodeAnimator.interrupt();
		Start.log.info("GUI was closed");
	}

	private static ArrayList<Location> getNodes() {
		return new ArrayList<Location>(nodesToDraw);
	}

	private static Canvas createMapPane() {

		// Floor map is 12 x 8

		Canvas map = new Canvas();

		map.setWidth(MAP_WIDTH);
		map.setHeight(HEIGHT);

		GraphicsContext gc = map.getGraphicsContext2D();

		Graph<Location> floorMap = model.getFloorGraph();

		nodeAnimator = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!nodeAnimator.isInterrupted()) {
					int max = getMaxNodes();
					for (int i = 0; i < max; i++) {
						for (ArrayList<Location> path : makeDrawable(coloredPath)) {
							if (i < path.size()) {
								nodesToDraw.add(path.get(i));
							}

							if (nodeAnimator.isInterrupted())
								return;

							new Rate(1.5).sleep();

							if (nodeAnimator.isInterrupted())
								return;
						}

						for (ArrayList<Location> path : makeDrawable(coloredPath)) {
							if (i < path.size()) {
								nodesToDraw.remove(path.get(i));
							}
						}
					}
				}
			}
		});

		canvasHandler = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!canvasHandler.isInterrupted()) {

					gc.clearRect(0, 0, MAP_WIDTH, HEIGHT);

					for (IVertex<Location> v : floorMap.getVertices()) {
						GUI.drawEdges(v, gc);
					}
					GUI.drawRobots(gc);
					GUI.drawPath(gc);
					GUI.drawNodes(gc);
					GUI.drawItems(gc, model.getItems());

					Log.debug("Updated robot location");

					new Rate(4).sleep();
				}

			}
		});

		nodeAnimator.start();
		canvasHandler.start();

		return map;
	}

	private static void drawItems(GraphicsContext gc, ArrayList<Item> items) {

		for (Item i : items) {

			gc.setFill(Color.BLACK);
			gc.fillOval(scale(i.getLocation().getX()), scale(i.getLocation().getY()), 5, 5);
			gc.setLineWidth(1);
			gc.strokeText(i.getItemName(), scale(i.getLocation().getX()) - 20, scale(i.getLocation().getY()) - 20);
		}

	}

	private static void drawNodes(GraphicsContext gc) {

		ArrayList<Location> nodes = getNodes();

		gc.setFill(Color.CADETBLUE);
		for (Location l : nodes) {
			gc.fillOval(scale(l.getX()), scale(l.getY()), 10, 10);
		}

	}

	private static int cycle;

	private static int getMaxNodes() {

		int maxPath = 0;

		HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> clone = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>(
				coloredPath);

		for (ArrayList<Location> path : makeDrawable(clone)) {
			maxPath = Math.max(maxPath, path.size());
		}

		return maxPath;
	}

	private static ArrayList<ArrayList<Location>> makeDrawable(
			HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> clone) {
		ArrayList<ArrayList<Location>> drawable = new ArrayList<ArrayList<Location>>();

		for (Tuple<ArrayList<ArrayList<Location>>, Paint> partPath : clone) {
			ArrayList<Location> wholePath = new ArrayList<Location>();
			for (ArrayList<Location> singlePath : partPath.getX()) {
				for (Location l : singlePath) {
					wholePath.add(l);
				}
			}
			drawable.add(wholePath);
		}

		return drawable;
	}

	private static void drawPath(GraphicsContext gc) {

		for (Tuple<ArrayList<ArrayList<Location>>, Paint> path : coloredPath) {

			for (ArrayList<Location> part : path.getX()) {

				gc.setFill(path.getY());
				gc.setStroke(path.getY());

				gc.setLineWidth(4);
				gc.setLineDashes(7);

				for (int i = 0; i < part.size() - 1; i++) {
					gc.strokeLine(scale(part.get(i).getX()) + 5, scale(part.get(i).getY()) + 5,
							scale(part.get(i + 1).getX()) + 5, scale(part.get(i + 1).getY()) + 5);
				}

			}
		}
	}

	private static Paint getColor() {

		Paint p;

		if (cycle == 0) {
			p = Color.RED;
		} else if (cycle == 1) {
			p = Color.BLUE;
		} else if (cycle == 2) {
			p = Color.GREEN;
		} else {
			p = Color.YELLOW;
			cycle = 0;
		}

		cycle++;
		return p;
	}

	private static void drawEdges(IVertex<Location> v, GraphicsContext gc) {

		Location parent = v.getLabel().getData();

		for (IEdge<Location> e : v.getSuccessors()) {

			Location child = e.getTgt().getLabel().getData();

			gc.setStroke(Color.LIGHTSLATEGRAY);
			gc.setLineWidth(2);
			gc.setLineDashes(0);
			gc.strokeLine(scale(parent.getX()) + 5, scale(parent.getY()) + 5, scale(child.getX()) + 5,
					scale(child.getY()) + 5);
		}

	}

	private static void drawRobots(GraphicsContext gc) {
		for (Robot r : model.getRobots()) {
			gc.setFill(Color.DARKGREY);
			gc.fillRect(scale(r.getCurrentLocation().getX()) - 5, scale(r.getCurrentLocation().getY()) - 5, 20, 20);

			// TODO r.getOrientation();
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
			robotPane.setHgap(10);
			robotPane.setVgap(0);

			robotPane.add(l, 0, 0);
			robotPane.add(b, 1, 0);

			robotPane.add(idText, 0, 1);
			robotPane.add(jobId, 1, 1);

			robotPane.add(s, 0, 2);
			robotPane.add(status, 1, 2);

			if (model.getJob(r).isPresent()) {

				GridPane taskPane = new GridPane();
				taskPane.setStyle("-fx-border-color: gray");
				taskPane.setAlignment(Pos.BASELINE_CENTER);
				taskPane.setVgap(5);
				taskPane.setHgap(30);

				int i = 0;

				Label itemFlag = new Label("Job Items");
				itemFlag.setFont(new Font(15));

				taskPane.add(itemFlag, 0, i++, 2, 1);

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

		robotDisplay.setContent(robotGrid);
		robotHolder.add(robotDisplay, 0, 2);

		return robotHolder;
	}

	private static void updateLabels() {
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
				text = model.getJob(r).get().getStatus();
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

	public static void displayPath(ArrayList<ArrayList<Location>> arrayList) {

		paths.add(arrayList);
		coloredPath = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>();
		for (ArrayList<ArrayList<Location>> path : paths) {

			coloredPath.add(new Tuple<ArrayList<ArrayList<Location>>, Paint>(path, getColor()));

		}
	}
}