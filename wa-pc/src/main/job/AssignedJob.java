package main.job;

import java.util.HashSet;
import java.util.List;

import utils.DropLocation;
import utils.Item;
import utils.Robot;
import utils.Route;

public class AssignedJob extends AJob{

	public HashSet<Robot> robots=new HashSet<Robot>();
	public Route route;
	
	public AssignedJob(DropLocation location, List<Item> pickups, HashSet<Robot> robots, Route route, String id ){
		
		super(location,pickups,id);
		this.route=route;
		this.robots=robots;
		
	}
	
	public AssignedJob(AJob job, Route route, HashSet<Robot> robots){
		this(job.location,job.pickups,robots,route,job.id);
		this.cancelledinTrainingSet=job.cancelledinTrainingSet;
	}
	
	public boolean isAssigned(){
		return true;
	}
	
}
