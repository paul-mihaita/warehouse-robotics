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

	private static int permId = 0;
	public static Float simpleAc = 0f;
	private static HashMap<Integer, int[]> result = new HashMap<Integer, int[]>();

	public ArrayList<IVertex<Location>> aStarSimple(Graph<Location> graph,
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
		openList.add(new Edge<Location>( graph
				.getVertex(start), new Float(0)));
		Float cost = 0f;
		ArrayList<Vertex<Location>> closedList = new ArrayList<>();
		IVertex<Location> curr = new Vertex<Location>(new Location(-1, -1));
		IVertex<Location> cameFrom;
		while (!openList.isEmpty()) {
			Edge<Location> currEdge = openList.poll();
			if (!closedList.contains(currEdge.getTgt())) {
				cameFrom = curr;
				curr = currEdge.getTgt();
				currEdge.setCostAux(cameFrom.getLabel().getCost()+1);
				curr.getLabel().setParentVertex(cameFrom);
				Location m =  curr.getLabel().getData();
				if (m.equals(finish)) {
					simpleAc = cost;
					return constructPath(curr);
				}
				closedList.add( (Vertex<Location>) curr);
				Collection<IEdge<Location>> children = curr.getSuccessors();
				
				if (!children.isEmpty()) {
					ArrayList<Edge<Location>> aux = new ArrayList<>();
					cost = currEdge.getCostAux();
					for (IEdge<Location> e : children) {
						IVertex<Location> from = e.getTgt();
						IVertex<Location> to = (Vertex<Location>) graph
								.getVertex(finish);
						if (to != null) {
							Integer a = heuristics.apply(from.getLabel().getData(), to.getLabel().getData());
							Float he = cost + a ;
							
							aux.add(new Edge<Location>(from, he));
						}
					}
					openList.addAll(aux);

				}
			}
		}
		return new ArrayList<IVertex<Location>>();
	}
	public ArrayList<State> aStarCoop(Graph<Location> graph,GridMap map,
			ArrayList<Robot> robots,ArrayList<Location> finish,
			BiFunction<State, State, Integer> heuristics  , int w) {
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
		int counter = 0;
		while(!openList.isEmpty()){
			curr = openList.poll();
			if(curr.isFinal(fin) || counter == w){
				simpleAc = (float) (curr.getCost() +  3);
				return constructStatePath(curr);
			}
			if(!closedList.contains(curr)){
				int prior = curr.getCost();
				ArrayList<State> nextStates = generateStates(curr,fin, graph, map);
				for(State x : nextStates){
					x.parent = curr;
					int cost = prior + heuristics.apply(x,fin);
					x.setcostHeurisitc(cost);
					x.setCost(prior + diff(curr,x));
				}
				openList.addAll(nextStates);
				closedList.add(curr);
			}
		}
		return new ArrayList<State>();
	}
	private int diff(State old, State curr) {
		int cost = 0;
		List<Location> a = old.getRLoc();
		List<Location> b = curr.getRLoc();
		for(int i = 0; i < a.size();i++){
			Location t = a.get(i);
			Location u = b.get(i);
			cost += (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
		}
		return cost;
	}
	private ArrayList<State> setCost(PriorityQueue<State> auxList, int priorCost) {
		Iterator<State> it = auxList.iterator();
		ArrayList<State> x = new ArrayList<>();
		while(it.hasNext()){
			State curr = it.next();
			curr.setCost(priorCost);
			x.add(curr);
		}
		return x;
	}
	private ArrayList<State> constructStatePath(State curr) {
		ArrayList <State> result = new ArrayList<>();
		while(!curr.getRLoc().isEmpty()){
			result.add(curr);
			curr = curr.parent;
		}
		return result;
	}
	private List<Location> getRobotsLoc(ArrayList<Robot> robots) {
		ArrayList<Location> aux = new ArrayList<>();
		for(Robot r : robots){
			aux.add(r.getCurrentLocation());
		}
		return aux;
	}
	

	private ArrayList<IVertex<Location>> constructPath(IVertex<Location> curr) {
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

	private static Edge<Location> getNeighbour(Graph<Location> graph,
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

	public static ArrayList<State> generateStates(State current,
			State fin, Graph<Location> graph, GridMap map) {

		ArrayList<Location> rLoc = new ArrayList<>(current.getRLoc());
		ArrayList<Location> fLoc = new ArrayList<>(fin.getRLoc());
		for (Location r : rLoc) {
			((Vertex<Location>) graph.getVertex(r)).setReserved(false);
		}
		ArrayList<State> states = new ArrayList<>();
		HashMap<Integer, ArrayList<Location>> rPosTbl = new HashMap<>();
		for (int i = 0; i < rLoc.size(); i++) {

			Location fst = rLoc.get(i);
			
			ArrayList<Location> aux = new ArrayList<Location>();
			aux.add(fst);
			aux.addAll(getNeibours(fst, map, graph,rLoc,i));
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

		Map<Integer, int[]> numbers = new HashMap<Integer, int[]>();

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

				List<Location> d = toAdd.getRLoc();
				

			}
		}
		return states;
	}

	private static boolean isViableState(State toAdd) {
		List<Location> list = toAdd.getRLoc();
		Set<Location> set = new TreeSet<Location>(
				new Comparator<Location>() {
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
		neighbours.add(neighbour(graph,rLoc, map, x, y, 1, 0,i));

		neighbours.add(neighbour(graph, rLoc,map, x, y, -1, 0,i));

		neighbours.add(neighbour(graph,rLoc, map, x, y, 0, -1,i));

		neighbours.add(neighbour(graph,rLoc,map, x, y, 0, 1,i));

		ArrayList<Location> toDelete = new ArrayList<>();

		for (Location neigh : neighbours) {
			if (neigh.equals(invalid) ) {
				toDelete.add(neigh);
			}
		}
		neighbours.removeAll(toDelete);
		return neighbours;
	}

	

	private static Location neighbour(Graph<Location> graph, ArrayList<Location> rLoc, GridMap map,
			int _x, int _y, int _dx, int _dy, int i) {
		Location p2 = new Location(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = new Location(_x + _dx, _y + _dy);
			if (graph.contains(p2) && ! prevRobot(rLoc,i,p2)
					&& !((Vertex<Location>) graph.getVertex(p2)).isReserved()) {
				return p2;
			}
		}
		return new Location(-1, -1);
	}

	

	private static boolean prevRobot(ArrayList<Location> rLoc, int i, Location p2) {
	
		if(i >= 1)
			return rLoc.get(i-1).equals(p2);
		else return false;
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
						children = graph.get(pPoint);
					} else {
						graph.addVertex(new Vertex<Location>(pPoint));
						children = graph.get(pPoint);
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
