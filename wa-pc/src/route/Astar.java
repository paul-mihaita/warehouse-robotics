package route;

import graph_entities.IEdge;
import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import student_solution.Edge;
import student_solution.Graph;
import student_solution.Vertex;
import utils.Location;

public class Astar {

	public static Float simpleAc = 0f;

	public static ArrayList<IVertex<Location>> aStar(Graph<Location> graph,
			Location start, Location finish,
			BiFunction<Location, Location, Integer> heuristics) {
		PriorityQueue<Edge<Location>> openList = new PriorityQueue<>(
				new Comparator<Edge<Location>>() {

					public int compare(Edge<Location> o1, Edge<Location> o2) {
						return o1.getCost() < o2.getCost() ? -1
								: o1.getCost() > o2.getCost() ? 1 : 0;
					}

				});
		simpleAc = (float) 0;
		openList.add(new Edge<Location>(graph.getVertex(start), new Float(0)));
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
				Location m = curr.getLabel().getData();
				if (m.equals(finish)) {
					simpleAc = currEdge.getCostAux();
					return constructPath(curr);
				}
				closedList.add((Vertex<Location>) curr);
				Collection<IEdge<Location>> children = curr.getSuccessors();

				if (!children.isEmpty()) {
					ArrayList<Edge<Location>> aux = new ArrayList<>();
					cost = currEdge.getCostAux() + 1;
					for (IEdge<Location> e : children) {
						IVertex<Location> from = e.getTgt();
						IVertex<Location> to = (Vertex<Location>) graph
								.getVertex(finish);
						if (to != null) {
							Integer a = heuristics.apply(from.getLabel()
									.getData(), to.getLabel().getData());
							Float he = cost + a;
							Edge<Location> toAdd = new Edge<Location>(from, he);
							toAdd.setCostAux(cost);
							aux.add(toAdd);
						}
					}
					openList.addAll(aux);

				}
			}
		}
		return new ArrayList<IVertex<Location>>();

	}
	private static ArrayList<IVertex<Location>> constructPath(IVertex<Location> curr) {
		ArrayList<IVertex<Location>> path = new ArrayList<>();
		Location l = (Location) curr.getLabel().getData();
		Location finish = new Location(-1, -1);
		while (!l.equals(finish)) {
			path.add(curr);
			Optional<IVertex<Location>> s = curr.getLabel().getParentVertex();
			curr = s.get();
			l = (Location) curr.getLabel().getData();
		}
		for (int i = 0, j = path.size() - 1; i < j; i++) {
			path.add(i, path.remove(j));
		}
		return path;
	}
}
