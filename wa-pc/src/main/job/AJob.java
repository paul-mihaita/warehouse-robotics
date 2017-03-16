package main.job;

import java.util.List;

import utils.Item;

public class AJob {

	public final String id;
	public boolean cancelledinTrainingSet;
	public DropLocation location;
	public final List<Item> pickups;
	
	public AJob(DropLocation location, List<Item> pickups, String id){
		this.location=location;
		this.pickups=pickups;
		this.id=id;
	}
	
	public AJob(DropLocation location, List<Item> pickups){
		this(location,pickups);
	}
	
	public boolean isAssigned(){
		return false;
	}
}
