package route;


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
		Planning plan = new Planning();
		Location start = new Location(0,1);
		Location finish = new Location(0,5);
		Location orientation = new Location(1,0);
		
		BiFunction<Location, Location, Integer> heuristics = new BiFunction<Location, Location, Integer> (){

			@Override
			public Integer apply(Location t, Location u) {
				// TODO Auto-generated method stub
				return (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
			}
			
		};
		//ArrayList<IVertex<Location>> route = plan.aStarSimple(graph,start ,finish ,  heuristics);
		
		/*for(IVertex<Location> n : route){
			Location l = (Location) n.getLabel().getData();
			System.out.println(l.getX() + "  " + l.getY());
		}*/
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(0,1);
		l.add(a);
		Location b = new Location(0,2);
		l.add(b);
		Location c = new Location(0,3);
		l.add(c);
		s.setRLoc(l);
		Vertex<Location> k = (Vertex<Location>) graph.getVertex(a);
		Vertex<Location> m = (Vertex<Location>) graph.getVertex(b);
		Vertex<Location> p = (Vertex<Location>) graph.getVertex(c);
		ArrayList<Location> fin = new ArrayList<>();
		fin.add(new Location(0,0));
		fin.add(new Location(0,5));
		fin.add(new Location(3,7));
		State t = new State();
		t.setRLoc(fin);
		
		ArrayList<Robot> robots = new ArrayList<>();
		Robot r1 = new Robot();
		Robot r2 = new Robot();
		Robot r3 = new Robot();
		r1.setCurrentLocation(a);
		r2.setCurrentLocation(b);
		r3.setCurrentLocation(c);
		robots.add(r1);
		robots.add(r2);
		robots.add(r3);
		
		BiFunction<State, State, Integer> heuristics2 = new BiFunction<State, State, Integer>() {

			@Override
			public Integer apply(State t, State u) {
				int cost = 0;
				List<Location> robots = t.getRLoc();
				List<Location> finish = u.getRLoc();
				for(int i=0;i<robots.size();i++){
					Location a = robots.get(i);
					Location b = finish.get(i);
					plan.aStarSimple(graph, a, b, heuristics);
					cost+= Planning.simpleAc;
				}
				return cost;
			}
			
		};
		HashMap <Integer,ArrayList<Location> >mapp = new HashMap<>();
		ArrayList<State> pp = plan.aStarCoop(graph, gridMap, robots, fin, heuristics2);
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
		System.out.println(Planning.simpleAc);
		
	}
}
