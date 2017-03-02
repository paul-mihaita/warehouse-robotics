package route;

import java.awt.Graphics2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rp.robotics.mapping.GridMap;
import utils.Location;
import utils.Node;
import lejos.geom.Line;
import lejos.geom.Point;
import movement.Movement;

public class Planning {
	private List<Point> points ;
	private Map<Point,List<Point>> graph;
	private GridMap map;
	
	
	public  List<Movement> aStar (Graph map ,Node start, Node finish, Location orientation){
		return null;
		
	}
}
