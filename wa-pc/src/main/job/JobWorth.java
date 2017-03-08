package main.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import movement.Movement.move;
import utils.Item;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Route;
import utils.Task;

public class JobWorth {
	
	private Job job;
	private double reward_time;;
	private Route route;
	private Task quantity;
	
	ArrayList<utils.Robot> robots=new ArrayList<>();
	ArrayList<Job> jobs=new ArrayList<>();
	HashMap<Robot,Job> map;
	HashMap<String,String> jobsWithValue= new HashMap<>();
	
	public JobWorth(Robot robot, Job job, Location startLocation){	
		
		this.job=job;
		this.reward_time=rewardPerTime();
		
	}
	
	public Route getRoute(){
		
		return this.route;
	}
	
	public Job getJob(){
		
		return this.job;
	}
	
	public double getReward(){
		
		return this.reward_time;                                              
	}
	
	public Task getQuantity(){
		return this.quantity;
	}
	
	private double rewardPerTime(){
		
		double rewardSum=0;	
		ArrayList<Task> tasks = job.geTasks();
		for(Task t : tasks){
			rewardSum += ( t.getQuantity() * t.getItem().getReward());
		}
		int bestDistance=0;//this.route.getPath();//get total distance
		
		return rewardSum;//(rewardSum/bestDistance); 
		//(num of items* reward per item) divided by (total distance to execute pickups)	
	}
	
	public String testReward(ArrayList<Job> jobs, ArrayList<Robot> robots){
		
		for(Job job : jobs){
			map=new HashMap<>();
			
			for(Robot robot:robots){
				map.put(robot, job);
			}
			int jobPathCost=0, jobSumOfReward=0;
			
			HashMap<Robot,ArrayList<ArrayList<move>>> paths= CommandCenter.generatePaths(map);
			Iterator it1=paths.values().iterator();
			while(it1.hasNext()){
				ArrayList<ArrayList<move>> routes=new ArrayList<>();
				routes=(ArrayList<ArrayList<move>>) it1.next();
				
				for(ArrayList<move> moves : routes){
					jobPathCost+=moves.size();
				}
				for(Task task:job.geTasks()){
					jobSumOfReward=(int) task.getItem().getReward();
				}
			}
			
			jobsWithValue.put(Integer.toString(job.getJobID()), 
					Float.toString(jobSumOfReward /  (jobPathCost/robots.size()) * (job.sumOfWeight()/50) ));
		
		}
		//sort hashmap+arraylist of jobs
		Map<String,String>map=new HashMap<>();
		Set set=map.entrySet();
		Iterator it2=set.iterator();
		while(it2.hasNext()){
			Map.Entry m=(Map.Entry)it2.next();			
		}
		
	}		
}
