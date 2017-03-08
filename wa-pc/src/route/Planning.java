package route;

import graph_entities.IEdge;
import graph_entities.IVertex;
import graph_entities.Label;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.State;
import student_solution.Vertex;
import utils.Job;
import utils.Location;
import utils.Robot;
import lejos.geom.Line;

public class Planning {
	public static BiFunction<State, State, Integer> manhatanHeuristic = new BiFunction<State, State, Integer> (){
		@Override
		public Integer apply(State a, State b) {
			// TODO Auto-generated method stub
			List<Location> from = a.getRLoc();
			List<Location> to = b.getRLoc();
			int cost = 0;
			for(int i = 0; i < from.size();i++){
				Location t = from.get(i);
				Location u = to.get(i);
				cost+= (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
			}
			return cost;
		}
		
	};
	public static Graph<Location> createGraph(GridMap map) {

		Graph<Location> graph = new Graph<>();
		Location invalid = new Location(-1, -1);
		
		for (int x = 0; x < map.getXSize(); x++) {
			for (int y = 0; y < map.getYSize(); y++) {
				if (map.isValidGridPosition(x, y) && !map.isObstructed(x, y)) {
					// Point p = map.getCoordinatesOfGridPosition(x,y);
					Location pPoint = new Location(x, y);
					Collection<IEdge<Location>> children;
					if (graph.contains(pPoint)) {
						children = graph.get(pPoint);
					} else {
						graph.addVertex(new Vertex<Location>(pPoint));
						children = graph.get(pPoint);
					}
					ArrayList<Edge<Location>> neighbours = new ArrayList<>();
					neighbours.add(AstarBruteForce.getNeighbour(graph, map, x, y, 1, 0));
					neighbours.add(AstarBruteForce.getNeighbour(graph, map, x, y, -1, 0));
					neighbours.add(AstarBruteForce.getNeighbour(graph, map, x, y, 0, -1));
					neighbours.add(AstarBruteForce.getNeighbour(graph, map, x, y, 0, 1));
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
