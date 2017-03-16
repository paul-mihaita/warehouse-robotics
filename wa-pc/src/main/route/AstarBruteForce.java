package main.route;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.State;
import student_solution.Vertex;
import utils.Location;
import utils.Robot;

public class AstarBruteForce {
	private static int permId = 0;
	public static Float simpleAc = 0f;
	private static HashMap<Integer, int[]> result = new HashMap<Integer, int[]>();

	

	public static ArrayList<State> aStar(Graph<Location> graph, GridMap map,
			ArrayList<Robot> robots, ArrayList<Location> finish,
			BiFunction<State, State, Integer> heuristics, int w) {
		simpleAc = (float) 0;
		PriorityQueue<State> openList = new PriorityQueue<>(
				new Comparator<State>() {

					@Override
					public int compare(State o1, State o2) {

						return o1.compareTo(o2);
					}
				});

		State start = new State(getRobotsLoc(robots));
		State fin = new State(finish);
		State curr = start;
		State old = new State();
		openList.add(start);
		start.parent = old;
		curr = start;
		curr.setCost(0);
		ArrayList<State> closedList = new ArrayList<>();
		while (!openList.isEmpty()) {
			curr = openList.poll();
			if (curr.isFinal(fin)) {
				simpleAc = (float) (curr.getCost());
				return constructStatePath(curr);
			}
			if (!closedList.contains(curr)) {
				int prior = curr.getCost();
				ArrayList<State> nextStates = generateStates(curr, fin, graph,
						map, finish);
				for (State x : nextStates) {
					x.parent = curr;
					x.setCost(prior + diff(curr, x, finish));
					prior = x.getCost();
					int cost = prior + heuristics.apply(x, fin);
					x.setcostHeurisitc(cost);
				}
				openList.addAll(nextStates);
				closedList.add(curr);
			}
		}
		return new ArrayList<State>();
	}

	private static int diff(State old, State curr, ArrayList<Location> finish) {
		int cost = 0;
		Location out = new Location(-1, -1);
		List<Location> a = old.getRLoc();
		List<Location> b = curr.getRLoc();
		for (int i = 0; i < a.size(); i++) {
			Location t = a.get(i);
			Location u = b.get(i);
			if (!finish.get(i).equals(out)) {
				if (u.equals(finish.get(i))) {
					int d = (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY()
							- u.getY()));
					if (d > 0)
						cost += d;
					else
						cost += 1;
				}
			}
		}
		return cost;
	}

	private static ArrayList<State> constructStatePath(State curr) {
		ArrayList<State> result = new ArrayList<>();
		while (!curr.getRLoc().isEmpty()) {
			result.add(curr);
			curr = curr.parent;
		}
		return result;
	}

	private static List<Location> getRobotsLoc(ArrayList<Robot> robots) {
		ArrayList<Location> aux = new ArrayList<>();
		for (Robot r : robots) {
			aux.add(r.getCurrentLocation());
		}
		return aux;
	}

	

	public static Edge<Location> getNeighbour(Graph<Location> graph,
			GridMap map, int _x, int _y, int _dx, int _dy) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2)) {
				Vertex<Location> l = (Vertex<Location>) graph.getVertex(p2);
				if (!l.isReserved())
					return new Edge<Location>(graph.getVertex(p2), new Float(1));
				else
					return new Edge<Location>(new Vertex<Location>(
							new Location(-1, -1)), new Float(1));
			} else {
				graph.addVertex(new Vertex<Location>(p2));
				return new Edge<Location>(graph.getVertex(p2), new Float(1));

			}
		}
		return new Edge<Location>(new Vertex<Location>(p2), new Float(1));
	}

	public static ArrayList<State> generateStates(State current, State fin,
			Graph<Location> graph, GridMap map, ArrayList<Location> finish) {
		Location out = new Location(-1, -1);
		ArrayList<Location> rLoc = new ArrayList<>(current.getRLoc());
		for (Location r : rLoc) {
			((Vertex<Location>) graph.getVertex(r)).setReserved(false);
		}
		ArrayList<State> states = new ArrayList<>();
		HashMap<Integer, ArrayList<Location>> rPosTbl = new HashMap<>();
		for (int i = 0; i < rLoc.size(); i++) {

			Location fst = rLoc.get(i);

			ArrayList<Location> aux = new ArrayList<Location>();
			aux.add(fst);

			if (!finish.get(i).equals(out)) {
				aux.addAll(getNeibours(fst, map, graph, rLoc, i));
			}
			rPosTbl.put(i, aux);

		}
		Iterator<ArrayList<Location>> it = rPosTbl.values().iterator();
		int[] boundaries = new int[rPosTbl.size()];
		int i = 0;
		while (it.hasNext()) {
			boundaries[i] = it.next().size();
			i++;
		}
		int[] perm = new int[boundaries.length];


		int k = 0;
		permId = 0;
		result = new HashMap<Integer, int[]>();
		generateNumbers(boundaries, perm, k);
		Iterator<int[]> iterator = result.values().iterator();
		while (iterator.hasNext()) {
			int[] p = iterator.next();
			State toAdd = new State();
			ArrayList<Location> loc = new ArrayList<>();
			for (int j = 0; j < p.length; j++) {
				loc.add(rPosTbl.get(j).get(p[j]));
			}
			toAdd.setRLoc(loc);
			if (isViableState(toAdd)) {
				toAdd.parent = current;
				states.add(toAdd);


			}
		}
		return states;
	}

	private static boolean isViableState(State toAdd) {
		List<Location> list = toAdd.getRLoc();
		Set<Location> set = new TreeSet<Location>(new Comparator<Location>() {
			@Override
			public int compare(Location o1, Location o2) {
				return o1.compareTo(o2);
			}

		});
		for (Location l : list) {
			if (!set.add(l)) {
				return false;
			}
		}
		return true;
	}

	private static int[] generateNumbers(int[] boundaries, int[] perm, int k) {
		if (k == perm.length) {
			result.put(permId, perm.clone());
			permId++;
		} else {
			int nr = boundaries[k];
			for (int i = 0; i < nr; i++) {
				perm[k] = i;
				if (k < perm.length) {
					k++;
					generateNumbers(boundaries, perm, k);
					k--;
				}
			}
		}

		return perm;
	}

	private static ArrayList<Location> getNeibours(Location fst, GridMap map,
			Graph<Location> graph, ArrayList<Location> rLoc, int i) {
		ArrayList<Location> neighbours = new ArrayList<>();
		Location invalid = new Location(-1, -1);
		int x = fst.getX();
		int y = fst.getY();
		neighbours.add(neighbour(graph, rLoc, map, x, y, 1, 0, i));

		neighbours.add(neighbour(graph, rLoc, map, x, y, -1, 0, i));

		neighbours.add(neighbour(graph, rLoc, map, x, y, 0, -1, i));

		neighbours.add(neighbour(graph, rLoc, map, x, y, 0, 1, i));

		ArrayList<Location> toDelete = new ArrayList<>();

		for (Location neigh : neighbours) {
			if (neigh.equals(invalid)) {
				toDelete.add(neigh);
			}
		}
		neighbours.removeAll(toDelete);
		return neighbours;
	}

	private static Location neighbour(Graph<Location> graph,
			ArrayList<Location> rLoc, GridMap map, int _x, int _y, int _dx,
			int _dy, int i) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2) && !prevRobot(rLoc, i, p2)
					&& !((Vertex<Location>) graph.getVertex(p2)).isReserved()) {
				return p2;
			}
		}
		return new Location(-1, -1);
	}

	private static boolean prevRobot(ArrayList<Location> rLoc, int i,
			Location p2) {

		if (i >= 1)
			return rLoc.get(i - 1).equals(p2);
		else
			return false;
	}
}
