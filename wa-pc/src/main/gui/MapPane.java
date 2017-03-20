package main.gui;

import java.util.ArrayList;
import java.util.HashSet;

import graph_entities.IEdge;
import graph_entities.IVertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.model.WarehouseFloor;
import rp.util.Rate;
import student_solution.Graph;
import utils.Info;
import utils.Item;
import utils.Location;
import utils.Robot;
import utils.Tuple;

public class MapPane extends Canvas {

	private static WarehouseFloor model;
	private static Thread nodeAnimator;
	private static Thread canvasHandler;

	private static final Image WATER = new Image(
			"http://orig04.deviantart.net/789b/f/2012/102/f/4/bigger_8_bit_squirtle_by_mickiart14-d4vwhge.png", 45, 45,
			false, false);
	private static final Image FIRE = new Image("http://piq.codeus.net/static/media/userpics/piq_366704_400x400.png",
			35, 35, false, false);
	private static final Image GRASS = new Image(
			"http://orig03.deviantart.net/bad3/f/2014/261/0/4/pixel_bulbasaur_by_venasaur12-d7znzxm.png", 40, 30, false,
			false);

	public MapPane(WarehouseFloor model) {
		super();
		MapPane.model = model;
		this.setWidth(GUI.MAP_WIDTH);
		this.setHeight(GUI.HEIGHT);

		GraphicsContext gc = this.getGraphicsContext2D();

		Graph<Location> floorMap = model.getFloorGraph();

		nodeAnimator = new Thread() {

			@Override
			public void run() {
				while (!this.isInterrupted()) {
					int max = getMaxNodes();
					for (int i = 0; i < max; i++) {
						for (ArrayList<Location> path : makeDrawable(GUI.getPaths())) {

							if (i < path.size()) {
								GUI.getNodesToDraw().add(path.get(i));
							}
						}

						if (this.isInterrupted())
							return;

						new Rate(1.5).sleep();

						if (this.isInterrupted())
							return;

						for (ArrayList<Location> path : makeDrawable(GUI.getPaths())) {
							if (i < path.size()) {
								GUI.getNodesToDraw().remove(path.get(i));
							}
						}
					}
				}
			}
		};

		canvasHandler = new Thread() {
			@Override
			public void run() {
				while (!this.isInterrupted()) {

					gc.clearRect(0, 0, GUI.MAP_WIDTH, GUI.HEIGHT);

					for (IVertex<Location> v : floorMap.getVertices()) {
						MapPane.drawEdges(v, gc);
					}
					MapPane.drawRobots(gc);
					MapPane.drawPath(gc);
					MapPane.drawNodes(gc);
					MapPane.drawItems(gc, model.getItems());
					new Rate(5).sleep();
				}

			}
		};

		canvasHandler.start();
		nodeAnimator.start();
	}

	private static void drawPath(GraphicsContext gc) {

		for (Tuple<ArrayList<ArrayList<Location>>, Paint> path : GUI.getPaths()) {

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
				gc.drawImage(makeTransparent(WATER), scale(r.getCurrentLocation().getX()) - 15,
						scale(r.getCurrentLocation().getY()) - 20);

			} else if (r.getName().equals(Info.RobotNames[1])) {
				// the grass one

				gc.drawImage(makeTransparent(GRASS), scale(r.getCurrentLocation().getX()) - 10,
						scale(r.getCurrentLocation().getY()) - 15);

			} else if (r.getName().equals(Info.RobotNames[2])) {
				// the fire one
				gc.drawImage(makeTransparent(FIRE), scale(r.getCurrentLocation().getX()) - 15,
						scale(r.getCurrentLocation().getY()) - 20);

			}

			// TODO r.getOrientation();
		}
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

	private static void drawNodes(GraphicsContext gc) {

		ArrayList<Location> nodes = GUI.getNodes();

		gc.setFill(Color.CADETBLUE);
		for (Location l : nodes) {
			gc.fillOval(scale(l.getX()), scale(l.getY()), 10, 10);
		}

	}

	private static float scale(float value) {

		// Factors to make the drawing central and pretty

		float scale = (float) (GUI.MAP_WIDTH / 11.5);

		return value * scale + 5;
	}

	public void interrupt() {
		canvasHandler.interrupt();
		nodeAnimator.interrupt();
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

	private static int getMaxNodes() {

		int maxPath = 0;

		HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>> clone = new HashSet<Tuple<ArrayList<ArrayList<Location>>, Paint>>(
				GUI.getPaths());

		for (ArrayList<Location> path : makeDrawable(clone)) {
			maxPath = Math.max(maxPath, path.size());
		}

		return maxPath;
	}
}
