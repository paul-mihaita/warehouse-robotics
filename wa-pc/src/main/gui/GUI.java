package main.gui;

import java.io.File;

import org.apache.log4j.Logger;

import bootstrap.Start;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.model.WarehouseFloor;

public class GUI extends Application {

	public static final Logger log = Logger.getLogger(GUI.class);

	// For the moment while there is no map these are not being used
	public static final int SIDEBAR_WIDTH = 272;
	public static final int MAP_WIDTH = 900;

	public static final int WIDTH = SIDEBAR_WIDTH + MAP_WIDTH;

	public static final int HEIGHT = MAP_WIDTH * 7 / 11;

	private static WarehouseFloor model;

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
			log.debug("Icon found at " + alex.getAbsolutePath());
			primaryStage.getIcons().add(new Image(alex.getAbsolutePath()));
		}

		tabPane = new TabMenuPane(model);

		map = new MapPane(model);

		BorderPane guiHolder = new BorderPane();

		guiHolder.setLeft(tabPane);
		guiHolder.setCenter(map);

		final LongProperty lastUpdate = new SimpleLongProperty();

		final long minUpdateInterval = 0;

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (now - lastUpdate.get() > minUpdateInterval) {
					GUI.refresh();
				}
			}
		};
		timer.start();

		primaryStage.setScene(new Scene(guiHolder));
		primaryStage.show();

	}

	@Override
	public void stop() {
		map.interrupt();
		tabPane.interrupt();
		log.info("GUI was closed");
		System.exit(0);
	}

	public static void refresh() {
		tabPane.refresh();
	}
}
