package main.route;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Graph;
import student_solution.State;
import utils.Location;
import utils.Robot;

public class AstarWHCA {

	public static ArrayList<ArrayList<Location>> aStar(Graph<Location> graph, GridMap map,
			ArrayList<Robot> robots, Hashtable<Robot, Location> targets, int maxStep ) {
		
		BiFunction<State, State, Integer> heuristics = new BiFunction<State, State, Integer>() {

			@Override
			public Integer apply(State a, State b) {
				Location start = a.getRLoc().get(0);
				Location finish = b.getRLoc().get(0);
				Astar.aStar(graph, start, finish, Planning.manhatanHeuristic, maxStep, false, map ,0);
				return Astar.simpleAc;
			}

		};
		ArrayList<ArrayList<Location>> robotPaths = new ArrayList<>();
		int routesComputed = 0;
		int nrOfRobots = robots.size();
		while (routesComputed < nrOfRobots) {
			int max = -9999;
			for(Robot r : robots){
				if(max < r.getMovesCompleted())
					max = r.getMovesCompleted();
			}
			Robot robot = robots.get(routesComputed);
			if(targets.containsKey(robot)){
				Location start = robot.getCurrentLocation();
				Location fin = targets.get(robot);
				if(!fin.equals(new Location(-1,-1))){
					ArrayList<Location> path = Astar.aStar(graph, start, fin,
							heuristics, maxStep, true, map , max );
					robotPaths.add(path);
					Astar.setReserved(path,max);
					routesComputed++;
				}else routesComputed++;
			}else routesComputed++;
		}
		return robotPaths;
	}

	public static void resetTable() {
		Astar.reset();
	}

	/*private static <T> void rotate(List<T> list) {
		if (!list.isEmpty()) {
			int lSize = list.size();
			T firstEl = list.get(0);
			for (int i = 0; i < lSize - 1; i++) {
				T nextEl = list.get(i + 1);
				list.add(i, nextEl);
			}
			list.add(firstEl);
		}
	}*/

}
