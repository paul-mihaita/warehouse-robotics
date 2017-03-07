package main.route;


import graph_entities.IVertex;
import route.Astar;
import route.AstarBruteForce;
import route.AstarHCA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.util.HashMap;
import student_solution.Graph;
import student_solution.State;
import student_solution.Vertex;
import utils.Location;
import utils.Robot;

public class test {
	public static void main (String[] args){
		GridMap gridMap = MapUtils.createRealWarehouse();
		Graph<Location> graph = Planning.createGraph(gridMap);
		Location start = new Location(0,1);
		Location finish = new Location(0,7);
		Location orientation = new Location(0,3);
		
		/*BiFunction<State, State, Integer> manhatan = new BiFunction<State, State, Integer> (){

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
		ArrayList<Location> route = Astar.aStar(graph,start ,finish , manhatan, 3 ,false,gridMap);
		for(Location l : route){
			System.out.print(l.getX() + "  " + l.getY() + " | ");
		}
		System.out.println();
		System.out.println(Astar.simpleAc);
*/
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(3,0);
		l.add(a);
		Location b = new Location(0,0);
		l.add(b);
		Location c = new Location(5,0);
		l.add(c);
		s.setRLoc(l);
		Vertex<Location> k = (Vertex<Location>) graph.getVertex(a);
		Vertex<Location> m = (Vertex<Location>) graph.getVertex(b);
		Vertex<Location> p = (Vertex<Location>) graph.getVertex(c);
		ArrayList<Location> fin = new ArrayList<>();
		fin.add(new Location(0,0));
		fin.add(new Location(3,0));
		fin.add(new Location(10,0));
		State t = new State();
		t.setRLoc(fin);
		
		ArrayList<Robot> robots = new ArrayList<>();
		Robot r1 = new Robot(null, null, c, a);
		Robot r2 = new Robot(null, null, c, b);
		Robot r3 = new Robot(null, null, c, c);
		robots.add(r1);
		robots.add(r2);
		robots.add(r3);
		HashMap<Location,HashMap<Location,Integer>> costs = new HashMap<>();
		
		HashMap <Integer,ArrayList<Location> >mapp = new HashMap<>();
		AstarHCA.aStar(graph, gridMap, robots, fin, 100);
	}
}
