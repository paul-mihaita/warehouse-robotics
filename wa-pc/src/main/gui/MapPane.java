package main.gui;

import java.util.ArrayList;
import java.util.Iterator;

import com.sun.java.swing.plaf.motif.MotifOptionPaneUI;

import graph_entities.IEdge;
import graph_entities.IVertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import main.gui.listeners.MouseFlip;
import main.model.WarehouseFloor;
import rp.util.Rate;
import student_solution.Graph;
import utils.Info;
import utils.Item;
import utils.Location;
import utils.Robot;

public class MapPane extends Canvas {

	private static WarehouseFloor model;
	private static Thread pathAnimator;
	private static Thread canvasHandler;
	private static final Image WATER = new Image(
			"http://orig04.deviantart.net/789b/f/2012/102/f/4/bigger_8_bit_squirtle_by_mickiart14-d4vwhge.png", 65, 65,
			false, false);
	private static final Image FIRE = new Image("http://piq.codeus.net/static/media/userpics/piq_366704_400x400.png",
			45, 45, false, false);
	private static final Image GRASS = new Image(
			"http://orig03.deviantart.net/bad3/f/2014/261/0/4/pixel_bulbasaur_by_venasaur12-d7znzxm.png", 55, 45, false,
			false);

	public MapPane(WarehouseFloor model) {
		super();
		MapPane.model = model;
		this.setWidth(GUI.MAP_WIDTH);
		this.setHeight(GUI.HEIGHT);
		this.setOnMouseClicked(new MouseFlip(this));

		GraphicsContext gc = this.getGraphicsContext2D();

		Graph<Location> floorMap = model.getFloorGraph();

		pathAnimator = new PathAnimator(model, gc);

		canvasHandler = new Thread() {
			@Override
			public void run() {
				this.setName("Canvas Handler");

				Rate r = new Rate(10);

				while (!this.isInterrupted()) {
					gc.clearRect(0, 0, GUI.MAP_WIDTH, GUI.HEIGHT);

					for (IVertex<Location> v : floorMap.getVertices()) {
						MapPane.drawEdges(v, gc);
					}

					MapPane.drawPaths(gc);
					MapPane.drawRobots(gc);
					MapPane.drawItems(gc, model.getItems());

					r.sleep();
				}

			}
		};

		canvasHandler.start();
		pathAnimator.start();
	}

	private static void drawPaths(GraphicsContext gc) {

		Iterator<ArrayList<Location>> iter = model.getActivePaths().iterator();

		while (iter.hasNext()) {

			ArrayList<Location> path = iter.next();

			for (int i = 0; i < path.size() - 1; i++) {

				Location l = path.get(i);
				Location m = path.get(i + 1);

				gc.setStroke(Color.CADETBLUE);
				gc.setLineDashes(7);
				gc.setLineWidth(6);

				gc.strokeLine(scale(l.getX()) + 2, scale(l.getY()) + 2, scale(m.getX()) + 2, scale(m.getY()) + 2);

			}

		}

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
			/*
			 * gc.setFill(Color.DARKGREY);
			 * gc.fillRect(scale(r.getCurrentLocation().getX()) - 5,
			 * scale(r.getCurrentLocation().getY()) - 5, 20, 20);
			 */

			if (r.getName().equals(Info.RobotNames[0])) {
				// the water one

				gc.drawImage(makeTransparent(WATER), scale(r.getCurrentLocation().getX()) - 25,
						scale(r.getCurrentLocation().getY()) - 25);

				gc.setFill(Color.BLUE);

			} else if (r.getName().equals(Info.RobotNames[1])) {
				// the grass one

				gc.drawImage(makeTransparent(GRASS), scale(r.getCurrentLocation().getX()) - 10,
						scale(r.getCurrentLocation().getY()) - 15);

				gc.setFill(Color.GREEN);

			} else if (r.getName().equals(Info.RobotNames[2])) {
				// the fire one

				gc.drawImage(makeTransparent(FIRE), scale(r.getCurrentLocation().getX()) - 10,
						scale(r.getCurrentLocation().getY()) - 20);

				gc.setFill(Color.RED);

			}

			// drawOrientation(r, gc);

		}
	}

	@SuppressWarnings("unused")
	private static void drawOrientation(Robot r, GraphicsContext gc) {

		float x = r.getCurrentLocation().getX();
		float y = r.getCurrentLocation().getY();

		float rx = r.getRelativeOrientation().getX();
		float ry = r.getRelativeOrientation().getY();

		double[] xPoints = new double[3];
		double[] yPoints = new double[3];

		if (rx == 1.0f) {
			// right

			xPoints[0] = scale(x + 0.5f);
			yPoints[0] = scale(y);

			xPoints[1] = scale(x + 0.3f);
			xPoints[1] = scale(y - 0.2f);

			xPoints[2] = scale(x + 0.3f);
			xPoints[2] = scale(y + 0.2f);

		} else if (rx == -1.0f) {
			// left

			xPoints[0] = scale(x - 0.5f);
			yPoints[0] = scale(y);

			xPoints[1] = scale(x - 0.3f);
			xPoints[1] = scale(y - 0.2f);

			xPoints[2] = scale(x - 0.3f);
			xPoints[2] = scale(y + 0.2f);

		} else if (ry == 1.0d) {
			// down

			xPoints[0] = scale(x);
			yPoints[0] = scale(y + 0.5f);

			xPoints[1] = scale(x - 0.2f);
			xPoints[1] = scale(y + 0.3f);

			xPoints[2] = scale(x + 0.2f);
			xPoints[2] = scale(y + 0.3f);

		} else {
			// up

			xPoints[0] = scale(x);
			yPoints[0] = scale(y - 0.5f);

			xPoints[1] = scale(x - 0.2f);
			xPoints[1] = scale(y - 0.3f);

			xPoints[2] = scale(x + 0.2f);
			xPoints[2] = scale(y - 0.3f);

		}

		// Start.log.debug("Direction drawn at: " +
		// r.getOrientation().toString());

		// gc.fillPolygon(xPoints, yPoints, 3);

	}

	private static final int TOLERANCE_THRESHOLD = 0XFF;

	private static Image makeTransparent(Image inputImage) {
		int W = (int) inputImage.getWidth();
		int H = (int) inputImage.getHeight();
		WritableImage outputImage = new WritableImage(W, H);
		PixelReader reader = inputImage.getPixelReader();
		PixelWriter writer = outputImage.getPixelWriter();
		for (int y = 0; y < H; y++) {
			for (int x = 0; x < W; x++) {
				int argb = reader.getArgb(x, y);

				int r = (argb >> 16) & 0xFF;
				int g = (argb >> 8) & 0xFF;
				int b = argb & 0xFF;

				if (r >= TOLERANCE_THRESHOLD && g >= TOLERANCE_THRESHOLD && b >= TOLERANCE_THRESHOLD) {

					argb &= 0x00FFFFFF;
				}

				writer.setArgb(x, y, argb);
			}
		}

		return outputImage;
	}

	private static void drawItems(GraphicsContext gc, ArrayList<Item> items) {

		for (Item i : items) {

			gc.setFill(Color.BLACK);
			gc.setLineWidth(1);
			gc.setLineDashOffset(0);
			gc.setLineDashes(0);
			gc.setStroke(Color.BLACK);
			gc.strokeText(i.getItemName(), scale(i.getLocation().getX()), scale(i.getLocation().getY()) + 5);
		}

	}

	private static float scale(float value) {

		// Factors to make the drawing central and pretty

		float scale = (float) (GUI.MAP_WIDTH / 12);

		return value * scale + 10;
	}

	public void interrupt() {
		canvasHandler.interrupt();
		pathAnimator.interrupt();
	}
}
