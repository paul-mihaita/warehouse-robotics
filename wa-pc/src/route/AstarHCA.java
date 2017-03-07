package route;

import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import student_solution.Graph;
import student_solution.State;
import utils.Location;
import utils.Robot;

public class AstarHCA {

	public static void aStar(Graph<Location> graph, GridMap map,
			ArrayList<Robot> robots, ArrayList<Location> finish, int maxStep) {
		BiFunction<State, State, Integer> manhatan = new BiFunction<State, State, Integer> (){

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
		BiFunction<State, State, Integer> heuristics = new BiFunction<State, State, Integer>() {

			@Override
			public Integer apply(State a, State b) {
				Location start = a.getRLoc().get(0);
				Location finish = b.getRLoc().get(0);
				Astar.aStar(graph, start, finish, manhatan, 100, false, map);
				return Astar.simpleAc;
			}

		};
		int routesComputed = 0;
		int nrOfRobots = robots.size();
		Astar.reset();
		while (routesComputed < nrOfRobots) {

			Robot robot = robots.get(0);

			Location start = robot.getCurrentLocation();
			Location fin = finish.get(routesComputed);


			ArrayList<Location> path = Astar.aStar(graph, start, fin,
					heuristics, maxStep, true, map);
			Astar.setReserved(path);
			Collection<List<State>> x = Astar.timeOcupation.values();
			for(List<State> l : x){
				for(State y : l){
					 List<Location> ly = y.getRLoc();
					 for(Location lll: ly){
						 System.out.print(lll.getX() + " " +lll.getY() + " | ");
					 }
				}
				
				System.out.println("and");
			}
			for(Location l :path){
				System.out.print(l.getX() + " " +l.getY() + " | ");
			}
			System.out.println();
			routesComputed++;
			rotate(robots);
		}

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

}
