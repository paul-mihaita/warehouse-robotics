package main.job;

import java.util.List;

import utils.Item;
import utils.Robot;
import utils.Route;

public class AssignedJob extends AJob{

	public Robot robot;
	public Route route;
	
	public AssignedJob(DropLocation location, List<Item> pickups, Robot robot, Route route, String id ){
		
		super(location,pickups,id);
		this.route=route;
		this.robot=robot;
		
	}
	
	public AssignedJob(AJob job, Route route, Robot robot){
		this(job.location,job.pickups,job.id,route,robot);
		this.cancelledinTrainingSet=job.cancelledinTrainingSet;
	}
	
	public boolean isAssigned(){
		return true;
	}
	
}
