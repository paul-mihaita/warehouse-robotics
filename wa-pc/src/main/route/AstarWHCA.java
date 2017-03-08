package main.route;


import java.util.ArrayList;

import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Graph;
import student_solution.State;
import utils.Location;
import utils.Robot;

public class AstarWHCA {

	public static ArrayList<ArrayList<Location>> aStar(Graph<Location> graph, GridMap map,
			ArrayList<Robot> robots, ArrayList<Location> finish, int maxStep) {
		
		BiFunction<State, State, Integer> heuristics = new BiFunction<State, State, Integer>() {

			@Override
			public Integer apply(State a, State b) {
				Location start = a.getRLoc().get(0);
				Location finish = b.getRLoc().get(0);
				Astar.aStar(graph, start, finish, Planning.manhatanHeuristic, maxStep, false, map);
				return Astar.simpleAc;
			}

		};
		ArrayList<ArrayList<Location>> robotPaths = new ArrayList<>();
		int routesComputed = 0;
		int nrOfRobots = robots.size();
		Astar.reset();
		while (routesComputed < nrOfRobots) {

			Robot robot = robots.get(routesComputed);

			Location start = robot.getCurrentLocation();
			Location fin = finish.get(routesComputed);

			ArrayList<Location> path = Astar.aStar(graph, start, fin,
					heuristics, maxStep, true, map );
			
			
			robotPaths.add(path);
			Astar.setReserved(path);
			routesComputed++;
		}
		return robotPaths;
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
