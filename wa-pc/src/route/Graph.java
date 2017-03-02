package route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lejos.geom.Point;

public class Graph {
	private HashMap<Point,List<Point>> graph;
	
	public Graph(){
		graph = new HashMap();
	}
	public void addChild(Point to, Point e){
		if(graph.containsKey(to))
			graph.get(to).add(e);
		else{
			ArrayList<Point>  p = new ArrayList<>();
			p.add(e);
			graph.put(to, p);
		}
	}
	public void addChildren(Point to, ArrayList<Point> e){
		if(graph.containsKey(to)){
			List <Point> children = graph.get(to);
				for(Point el : e){
					if(!children.contains(el))
						children.add(el);
				}
		}else{
			graph.put(to, e);
		}
	}
	public List<Point> get(Point p){
		return graph.get(p);
	}
}
