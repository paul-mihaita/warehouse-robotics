package route;

import java.util.ArrayList;
import java.util.List;

import lejos.geom.Line;
import lejos.geom.Point;
import movement.Movement;
import rp.robotics.mapping.GridMap;
import utils.Location;
import utils.Node;

public class CreateGraph {

	private GridMap map;

	private Point getNeighbour(int _x, int _y, int _dx, int _dy) {
		Point p2 = new Point(-1, -1);
		if (map.isValidTransition(_x, _y, _x + _dx, _y + _dy)) {
			p2 = map.getCoordinatesOfGridPosition(_x + _dx, _y + _dy);
		}
		return p2;
	}

	public  Graph createGraph (GridMap map ){
		Graph graph = new Graph();
		Line[] lines = map.getLines();
		Point invalid = new Point(-1,-1);
		for (int x = 0; x < map.getXSize(); x++) {
			for (int y = 0; y < map.getYSize(); y++) {
				if (map.isValidGridPosition(x, y)) {
					Point p = map.getCoordinatesOfGridPosition(x,y);
					ArrayList<Point> children = (ArrayList<Point>) graph.get(p);
					ArrayList<Point> neighbours = new ArrayList<Point>();
					neighbours.add(getNeighbour( x, y, 1, 0));
					neighbours.add(getNeighbour( x, y, -1, 0));
					neighbours.add(getNeighbour( x, y, 0, -1));
					neighbours.add(getNeighbour( x ,y, 0 , 1));			
					for(Point neigh : neighbours){
						if(!neigh.equals(invalid) && !children.contains(neigh)){
							children.add(neigh);
						}
					}
				}
			}
		}
		return graph;
	}
}
