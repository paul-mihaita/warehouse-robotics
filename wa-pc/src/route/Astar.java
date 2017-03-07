package route;

import graph_entities.IEdge;
import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.State;
import student_solution.Vertex;
import utils.Location;
import utils.Robot;

public class Astar {

	public static int simpleAc = 0;

	public static Hashtable<Integer, List<State>> timeOcupation ;

	public static ArrayList<Location> aStar(Graph<Location> graph,
			Location stLoc, Location finLoc,
			BiFunction<State, State, Integer> heuristics, int maxStep,
			boolean reservedMatter, GridMap map) {
		PriorityQueue<State> openList = new PriorityQueue<>(
				new Comparator<State>() {

					public int compare(State o1, State o2) {
						return o1.compareTo(o2);
					}

				});
		ArrayList<Location> trsh = new ArrayList<Location>();
		trsh.add(finLoc);
		State finish = new State(trsh);
		simpleAc = 0;
		State start = new State();
		start.addToList(stLoc);
		State trs = new State();
		trs.addToList(new Location(-1, -1));
		start.parent = trs;
		openList.add(start);

		int cost = 0;

		ArrayList<Location> closedList = new ArrayList<>();

		Location curr = new Location(-1, -1);

		while (!openList.isEmpty()) {

			State currState = openList.poll();
			generateNextStates(currState, map, graph);
			Location rLoc = currState.getRLoc().get(0);

			if (!closedList.contains(rLoc)) {
				curr = rLoc;

				ArrayList<Location> path = constructPath(currState);
				int steps = path.size();
				if (curr.equals(finish.getRLoc().get(0))
						|| steps == maxStep + 1) {
					simpleAc = currState.getCost();
					return path;
				}
				closedList.add(curr);

				Optional<List<State>> trash = currState.getSuccessors();
				List<State> children = trash.get();

				if (!children.isEmpty()) {
					ArrayList<State> aux = new ArrayList<>();
					cost = currState.getCost() + 1;
					for (State from : children) {
						if (reservedMatter == false || (!isReserved(from, steps) )) {

							Integer aprox = heuristics.apply(from, finish);

							int he = cost + aprox;

							from.setcostHeurisitc(he);

							from.setCost(cost);
							aux.add(from);
						}
					}
					openList.addAll(aux);

				}
			}
		}
		return new ArrayList<Location>();

	}

	private static void generateNextStates(State currState, GridMap map,
			Graph<Location> graph) {
		Location l = currState.getRLoc().get(0);
		currState
				.setSuccessors(getStates(getNeibours(l, map, graph), currState));
	}

	private static ArrayList<State> getStates(ArrayList<Location> neibours,
			State parent) {
		ArrayList<State> s = new ArrayList<>();
		for (Location l : neibours) {
			State x = new State();
			x.parent = parent;
			x.addToList(l);
			s.add(x);
		}
		return s;
	}

	private static ArrayList<Location> getNeibours(Location fst, GridMap map,
			Graph<Location> graph) {
		ArrayList<Location> neighbours = new ArrayList<>();
		Location invalid = new Location(-1, -1);
		int x = fst.getX();
		int y = fst.getY();
		neighbours.add(neighbour(graph, map, x, y, 1, 0));

		neighbours.add(neighbour(graph, map, x, y, -1, 0));

		neighbours.add(neighbour(graph, map, x, y, 0, -1));

		neighbours.add(neighbour(graph, map, x, y, 0, 1));

		ArrayList<Location> toDelete = new ArrayList<>();

		for (Location neigh : neighbours) {
			if (neigh.equals(invalid)) {
				toDelete.add(neigh);
			}
		}
		neighbours.removeAll(toDelete);

		return neighbours;
	}

	private static Location neighbour(Graph<Location> graph, GridMap map,
			int _x, int _y, int _dx, int _dy) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2)) {
				return p2;
			}
		}
		return new Location(-1, -1);
	}

	private static boolean isReserved(State from, int steps) {
		if(timeOcupation.contains(steps)){
			List<State> statesAtTime = timeOcupation.get(steps);
			
			return contains(statesAtTime, from);
		}
		return false;
	}

	private static boolean contains(List<State> statesAtTime, State from) {
		if (!statesAtTime.isEmpty()) {
			for (State st : statesAtTime) {
				List<Location> a = st.getRLoc();
				List<Location> b = from.getRLoc();
				if (a.size() != b.size())
					return false;
				for (int i = 0; i < a.size(); i++) {
					if (!b.contains(a.get(i)))
						return false;
				}
			}
			return true;
		}else return false;
	}

	private static ArrayList<Location> constructPath(State curr) {
		ArrayList<Location> path = new ArrayList<>();
		Location l = curr.getRLoc().get(0);
		Location finish = new Location(-1, -1);
		while (!l.equals(finish)) {
			path.add(l);
			// System.out.println(l.getX() + "  "+l.getY() );
			if (curr.parent != null) {
				curr = curr.parent;
				l = curr.getRLoc().get(0);
			} else
				l = finish;
		}
		for (int i = 0, j = path.size() - 1; i < j; i++) {
			path.add(i, path.remove(j));
		}
		return path;
	}

	private static <T> void rotate(List<T> list) {
		if (!list.isEmpty()) {
			int lSize = list.size();
			T firstEl = list.get(0);
			for (int i = 0; i < lSize - 1; i++) {
				T nextEl = list.get(i + 1);
				list.add(i, nextEl);
			}
			list.add(firstEl);
		}
	}

	public static void reset() {

		timeOcupation = new Hashtable<>();

	}

	private static void tableAdd(State s, int time) {
		if (timeOcupation.containsKey(time)) {
			List<State> aux = timeOcupation.get(time);
			aux.add(s);
		} else {
			List<State> aux = new ArrayList<>();
			aux.add(s);
			timeOcupation.put(time, aux);
		}
	}

	public static void setReserved(ArrayList<Location> path) {
		int x = path.size()-1;
		for (int i = x; i>=0; i--) {
			State s = new State();
			s.addToList(path.get(x-i));
			tableAdd(s, i);
		}
	}
}
