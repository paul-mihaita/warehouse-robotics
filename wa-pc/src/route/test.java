package route;


import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.function.BiFunction;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import student_solution.Graph;
import student_solution.Vertex;
import utils.Location;

public class test {
	public static void main (String[] args){
		GridMap gridMap = MapUtils.createRealWarehouse();
		Graph<Location> graph = Planning.createGraph(gridMap);
		Planning plan = new Planning();
		Location start = new Location(0,0);
		Location finish = new Location(5,5);
		Location orientation = new Location(1,0);
		
		BiFunction<IVertex<Location>, IVertex<Location>, Float> heuristics = new BiFunction<IVertex<Location>,IVertex<Location>,Float>(){

			@Override
			public Float apply(IVertex<Location> a, IVertex<Location> b) {
				Location t = a.getLabel().getData();
				Location u = b.getLabel().getData();
				return (float) (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
			}
			
		};
		ArrayList<IVertex<Location>> route = plan.aStar(graph,start ,finish , orientation, heuristics);
		for(IVertex<Location> n : route){
			Location l = (Location) n.getLabel().getData();
			System.out.println(l.getX() + "  " + l.getY());
		}
	}
}
