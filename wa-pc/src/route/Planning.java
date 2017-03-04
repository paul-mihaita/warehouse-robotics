package route;

import graph_entities.IVertex;

import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.Vertex;
import utils.Location;
import lejos.geom.Line;
import movement.Movement;

public class Planning {
	private List<Location> points;
	private Map<Location, List<Location>> graph;
	private GridMap map;

	public ArrayList<Vertex> aStar(Graph graph, Location start,
			Location finish, Location orientation,
			BiFunction<Vertex<Location>, Vertex<Location>, Float> heuristics) {
		PriorityQueue<Edge> openList = new PriorityQueue<>(
				new Comparator<Edge>() {

					public int compare(Edge o1, Edge o2) {
						return o1.getCost() < o2.getCost() ? -1
								: o1.getCost() > o2.getCost() ? 1 : 0;
					}

				});
		openList.add(new Edge((IVertex) graph.getVertex(start), new Float(0)));
		Float cost = 0f;
		ArrayList<Vertex> closedList = new ArrayList<>();
		Vertex curr = new Vertex(new Location(-1, -1));
		Vertex cameFrom;
		while (!openList.isEmpty()) {
			Edge currEdge = openList.poll();
			if (!closedList.contains(currEdge.getTgt())) {
				cameFrom = curr;
				curr = (Vertex) currEdge.getTgt();
				curr.getLabel().setParentVertex(cameFrom);
				Location m = (Location) curr.getLabel().getData();
				if (m.equals(finish)) {
					return constructPath(curr);
				}
				closedList.add(curr);
				ArrayList<Edge> children = (ArrayList<Edge>) curr
						.getSuccessors();
				ArrayList<Edge> aux = new ArrayList<>();
				if (!children.isEmpty()) {
					for (Edge e : children) {
						Vertex from = (Vertex) e.getTgt();
						Vertex to = (Vertex<Location>) graph.getVertex(finish);
						if (to != null) {
							Float he = cost + heuristics.apply(from, to);
							aux.add(new Edge(from, he));
						}
					}
				}

				openList.addAll(aux);
			}
		}
		return new ArrayList<Vertex>();
	}

	private ArrayList<Vertex> constructPath(Vertex curr) {
		ArrayList<Vertex> path = new ArrayList<>();
		Location l = (Location) curr.getLabel().getData();
		Location finish = new Location(-1,-1);
		while(!l.equals(finish)){
			path.add(curr);
			 Optional s = curr.getLabel().getParentVertex();
			 curr = (Vertex) s.get();
			l = (Location) curr.getLabel().getData();
		}
		for (int i = 0, j = path.size() - 1; i < j; i++) {
			path.add(i, path.remove(j));
		}
		return path;
	}

	private static Edge getNeighbour(Graph graph, GridMap map, int _x, int _y,
			int _dx, int _dy) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2)) {
				return new Edge(graph.getVertex(p2), new Float(1));
			} else {
				graph.addVertex(new Vertex<Location>(p2));
				return new Edge(graph.getVertex(p2), new Float(1));

			}
		}
		return new Edge(new Vertex<Location>(p2), new Float(1));
	}

	public static Graph createGraph(GridMap map) {

		Graph graph = new Graph();
		Line[] lines = map.getLines();
		Location invalid = new Location(-1, -1);

		for (int x = 0; x < map.getXSize(); x++) {
			for (int y = 0; y < map.getYSize(); y++) {
				if (map.isValidGridPosition(x, y) && !map.isObstructed(x, y)) {
					// Point p = map.getCoordinatesOfGridPosition(x,y);
					Location pPoint = new Location(x, y);
					ArrayList<Edge> children;
					if (graph.contains(pPoint)) {
						children = (ArrayList<Edge>) graph.get(pPoint);
					} else {
						graph.addVertex(new Vertex<Location>(pPoint));
						children = (ArrayList<Edge>) graph.get(pPoint);
					}
					ArrayList<Edge> neighbours = new ArrayList<>();
					neighbours.add(getNeighbour(graph, map, x, y, 1, 0));
					neighbours.add(getNeighbour(graph, map, x, y, -1, 0));
					neighbours.add(getNeighbour(graph, map, x, y, 0, -1));
					neighbours.add(getNeighbour(graph, map, x, y, 0, 1));
					for (Edge neigh : neighbours) {
						Location poz = (Location) neigh.getTgt().getLabel()
								.getData();
						if (!poz.equals(invalid) && !children.contains(neigh)) {
							children.add(neigh);

						}
					}

				}
			}
		}
		return graph;
	}
}
