package main.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

import bootstrap.Start;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import main.model.WarehouseFloor;
import utils.Location;
import utils.Tuple;

public class GUI extends Application {

	// For the moment while there is no map these are not being used
	public static final int SIDEBAR_WIDTH = 272;
	public static final int MAP_WIDTH = 900;

	public static final int WIDTH = SIDEBAR_WIDTH + MAP_WIDTH;

	public static final int HEIGHT = MAP_WIDTH * 7 / 11;

	private static WarehouseFloor model;

	private static ArrayList<Location> nodesToDraw;

	private static HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> coloredPath;
	private static HashSet<ArrayList<ArrayList<Location>>> paths;

	private static TabMenuPane tabPane;
	private static MapPane map;

	/**
	 * 
	 * Creates a new GUI with the input being the list of jobs in preferential
	 * order
	 * 
	 * @param List
	 *            of jobs in preferential order, stating with most
	 */
	public static void create(WarehouseFloor model) {

		GUI.coloredPath = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>();
		GUI.paths = new HashSet<ArrayList<ArrayList<Location>>>();
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

		File alex = new File("/alex.png");

		if (alex.exists()) {
			Start.log.debug("Icon found at " + alex.getAbsolutePath());
			primaryStage.getIcons().add(new Image(alex.getAbsolutePath()));
		}

		tabPane = new TabMenuPane(model);

		map = new MapPane(model);

		BorderPane guiHolder = new BorderPane();

		guiHolder.setLeft(tabPane);
		guiHolder.setCenter(map);

		primaryStage.setScene(new Scene(guiHolder));
		primaryStage.show();

	}

	@Override
	public void stop() {
		map.interrupt();
		tabPane.interrupt();
		Start.log.info("GUI was closed");
		System.exit(0);
	}
	
	public static void refresh(){
		tabPane.refresh();
	}

	protected static ArrayList<Location> getNodes() {
		return new ArrayList<Location>(nodesToDraw);
	}

	protected static HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> getPaths() {
		return coloredPath;
	}

	protected static ArrayList<Location> getNodesToDraw() {
		return nodesToDraw;
	}

	public static void displayPath(ArrayList<ArrayList<Location>> newPath) {
		paths.add(newPath);
		coloredPath = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>();
		for (ArrayList<ArrayList<Location>> path : paths) {
			coloredPath.add(new Tuple<ArrayList<ArrayList<Location>>, Paint>(path, Color.BLUE));
		}	
	}
}
