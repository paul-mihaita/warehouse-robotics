package route;

import graph_entities.IEdge;
import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.Vertex;
import utils.Location;
import lejos.geom.Line;

public class Planning {

	public ArrayList<IVertex<Location>> aStar(Graph<Location> graph, Location start,
			Location finish, Location orientation,
			BiFunction<IVertex<Location>, IVertex<Location>, Float> heuristics) {
		PriorityQueue<Edge<Location>> openList = new PriorityQueue<>(
				new Comparator<Edge<Location>>() {

					public int compare(Edge<Location> o1, Edge<Location> o2) {
						return o1.getCost() < o2.getCost() ? -1
								: o1.getCost() > o2.getCost() ? 1 : 0;
					}

				});
		openList.add(new Edge<Location>((IVertex<Location>) graph.getVertex(start), new Float(0)));
		Float cost = 0f;
		ArrayList<Vertex<Location>> closedList = new ArrayList<>();
		IVertex<Location> curr = new Vertex<Location>(new Location(-1, -1));
		IVertex<Location> cameFrom;
		while (!openList.isEmpty()) {
			Edge<Location> currEdge = openList.poll();
			if (!closedList.contains(currEdge.getTgt())) {
				cameFrom = curr;
				curr = currEdge.getTgt();
				curr.getLabel().setParentVertex(cameFrom);
				Location m = (Location) curr.getLabel().getData();
				if (m.equals(finish)) {
					return constructPath(curr);
				}
				closedList.add((Vertex<Location>) curr);
				Collection<IEdge<Location>> children =  curr.getSuccessors();
				ArrayList<Edge<Location>> aux = new ArrayList<>();
				if (!children.isEmpty()) {
					for (IEdge<Location> e : children) {
						IVertex<Location> from =  e.getTgt();
						IVertex<Location> to = (Vertex<Location>) graph.getVertex(finish);
						if (to != null) {
							Float he = cost + heuristics.apply(from, to);
							aux.add(new Edge<Location>(from, he));
						}
					}
				}

				openList.addAll(aux);
			}
		}
		return new ArrayList<IVertex<Location>>();
	}

	private ArrayList<IVertex<Location>> constructPath(IVertex<Location> curr) {
		ArrayList<IVertex<Location>> path = new ArrayList<>();
		Location l = (Location) curr.getLabel().getData();
		Location finish = new Location(-1,-1);
		while(!l.equals(finish)){
			path.add(curr);
			 Optional<IVertex<Location>> s = curr.getLabel().getParentVertex();
			 curr =  s.get();
			l = (Location) curr.getLabel().getData();
		}
		for (int i = 0, j = path.size() - 1; i < j; i++) {
			path.add(i, path.remove(j));
		}
		return path;
	}

	private static Edge<Location> getNeighbour(Graph<Location> graph, GridMap map, int _x, int _y,
			int _dx, int _dy) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2)) {
				return new Edge<Location>(graph.getVertex(p2), new Float(1));
			} else {
				graph.addVertex(new Vertex<Location>(p2));
				return new Edge<Location>(graph.getVertex(p2), new Float(1));

			}
		}
		return new Edge<Location>(new Vertex<Location>(p2), new Float(1));
	}

	public static Graph<Location> createGraph(GridMap map) {

		Graph<Location> graph = new Graph<>();
		Line[] lines = map.getLines();
		Location invalid = new Location(-1, -1);

		for (int x = 0; x < map.getXSize(); x++) {
			for (int y = 0; y < map.getYSize(); y++) {
				if (map.isValidGridPosition(x, y) && !map.isObstructed(x, y)) {
					// Point p = map.getCoordinatesOfGridPosition(x,y);
					Location pPoint = new Location(x, y);
					Collection<IEdge<Location>> children;
					if (graph.contains(pPoint)) {
						children =  graph.get(pPoint);
					} else {
						graph.addVertex(new Vertex<Location>(pPoint));
						children =  graph.get(pPoint);
					}
					ArrayList<Edge<Location>> neighbours = new ArrayList<>();
					neighbours.add(getNeighbour(graph, map, x, y, 1, 0));
					neighbours.add(getNeighbour(graph, map, x, y, -1, 0));
					neighbours.add(getNeighbour(graph, map, x, y, 0, -1));
					neighbours.add(getNeighbour(graph, map, x, y, 0, 1));
					for (Edge<Location> neigh : neighbours) {
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
