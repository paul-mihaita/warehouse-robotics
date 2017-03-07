package main.route;


import graph_entities.IVertex;

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
		Location finish = new Location(0,1);
		Location orientation = new Location(0,3);
		
		BiFunction<Location, Location, Integer> heuristics = new BiFunction<Location, Location, Integer> (){

			@Override
			public Integer apply(Location t, Location u) {
				// TODO Auto-generated method stub
				return (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
			}
			
		};
		/*
		ArrayList<IVertex<Location>> route = plan.aStarSimple(graph,start ,finish ,  heuristics);
		for(IVertex<Location> n : route){
			Location l = (Location) n.getLabel().getData();
			System.out.print(l.getX() + "  " + l.getY() + " | ");
		}
		System.out.println();
		System.out.println(Planning.simpleAc);
*/
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(3,0);
		l.add(a);
		Location b = new Location(4,0);
		l.add(b);
		Location c = new Location(5,0);
		l.add(c);
		s.setRLoc(l);
		Vertex<Location> k = (Vertex<Location>) graph.getVertex(a);
		Vertex<Location> m = (Vertex<Location>) graph.getVertex(b);
		Vertex<Location> p = (Vertex<Location>) graph.getVertex(c);
		ArrayList<Location> fin = new ArrayList<>();
		fin.add(new Location(0,0));
		fin.add(new Location(-1,-1));
		fin.add(new Location(10,0));
		//fin.add(new Location(-1,-1));
		//fin.add(new Location(-1,-1));
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
		BiFunction<State, State, Integer> heuristics2 = new BiFunction<State, State, Integer>() {

			@Override
			public Integer apply(State t, State u) {
				int cost = 0;
				List<Location> robots = t.getRLoc();
				List<Location> finish = u.getRLoc();
				for(int i=0;i<robots.size();i++){
					Location a = robots.get(i);
					Location b = finish.get(i);
					if(costs.containsKey(a)){
						HashMap<Location,Integer> x = costs.get(a);
						if(x.containsKey(b)){
							cost+= x.get(b);
						}else{
							Astar.aStar(graph, a, b, heuristics);
							x.put(b,Astar.simpleAc.intValue());
							cost+= Astar.simpleAc;
						}
					}
					else{
						Astar.aStar(graph, a, b, heuristics);
						HashMap<Location, Integer> x = new HashMap<>();
						x.put(b,Astar.simpleAc.intValue());
						costs.put(a, x);
						cost+= Astar.simpleAc;
					}
				}
				return cost;
			}
			
		};
		HashMap <Integer,ArrayList<Location> >mapp = new HashMap<>();
		ArrayList<State> pp = AstarBruteForce.aStar(graph, gridMap, robots, fin, heuristics2, 100);
		boolean ok = true;
		for(State sta : pp){
			List<Location> lll = sta.getRLoc();

			for (int i = 0; i < lll.size(); i++) {
				Location pl = lll.get(i);
				if(ok == true){
					ArrayList<Location> kl = new ArrayList<Location>();
					kl.add(pl);
					mapp.put(i, kl);
				}else{
					mapp.get(i).add(pl);
				}
			}
			ok = false;
		}
		Iterator<ArrayList<Location>> it = mapp.values().iterator();
		int i = 0;
		while(it.hasNext()){
			ArrayList<Location> ghf = it.next();
			Collections.reverse(ghf);
			System.out.print("robot "+ i + ":  ");
			i++;
			for(Location lo : ghf){
				System.out.print( lo.getX() + " "+lo.getY() + " | ");
			}
			System.out.println();
		}
		System.out.println(AstarBruteForce.simpleAc);
		
	}
}
