package route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;

import lejos.geom.Point;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import student_solution.Edge;
import student_solution.Graph;
import student_solution.Vertex;
import utils.Location;
import utils.Node;

public class test {
	public static void main (String[] args){
		GridMap gridMap = MapUtils.createRealWarehouse();
		Graph graph = Planning.createGraph(gridMap);
		ArrayList<Vertex> x =  (ArrayList<Vertex>) graph.getVertices();
		Planning plan = new Planning();
		Location start = new Location(0,0);
		Location finish = new Location(5,5);
		Location orientation = new Location(1,0);
		
		BiFunction<Vertex<Location>, Vertex<Location>, Float> heuristics = new BiFunction<Vertex<Location>,Vertex<Location>,Float>(){

			@Override
			public Float apply(Vertex a, Vertex b) {
				Location t = (Location) a.getLabel().getData();
				Location u = (Location) b.getLabel().getData();
				return (float) (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() - u.getY()));
			}
			
		};
		ArrayList<Vertex> route = plan.aStar(graph,start ,finish , orientation, heuristics);
		for(Vertex n : route){
			Location l = (Location) n.getLabel().getData();
			System.out.println(l.getX() + "  " + l.getY());
		}
	}
}
