package main.gui;

import java.util.ArrayList;
import java.util.HashSet;

import graph_entities.IEdge;
import graph_entities.IVertex;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.model.WarehouseFloor;
import rp.util.Rate;
import student_solution.Graph;
import utils.Item;
import utils.Location;
import utils.Robot;
import utils.Tuple;

public class MapPane extends Canvas {

	private static WarehouseFloor model;
	private static Thread nodeAnimator;
	private static Thread canvasHandler;

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

							if (this.isInterrupted())
								return;

							new Rate(1.5).sleep();

							if (this.isInterrupted())
								return;
						}

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
			gc.setFill(Color.DARKGREY);
			gc.fillRect(scale(r.getCurrentLocation().getX()) - 5, scale(r.getCurrentLocation().getY()) - 5, 20, 20);

			// TODO r.getOrientation();
		}
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
