package main.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import jdk.management.resource.internal.inst.SocketOutputStreamRMHooks;

import java.util.Comparator;

import main.route.CommandCenter;
import movement.Movement.move;
import utils.Job;
import utils.Robot; 
import utils.Route;
import utils.Task;

public class JobWorth {
	
	private Job job;
	private Route route;
	private Task quantity;
	HashSet<utils.Robot> robots=new HashSet<>();
	ArrayList<Job> jobs=new ArrayList<>();
	HashMap<Robot,Job> map;
	HashMap<String,String> jobsWithValue= new HashMap<>();
	HashMap<String,String> finalJobsWithValue= new HashMap<>();	

	public JobWorth(ArrayList<Job> jobs, HashSet<Robot> robots){	
		this.robots = robots;
		this.jobs = jobs;
	}
	
	public Route getRoute(){
		return this.route;
	}
	
	public Job getJob(){
		
		return this.job;
	}
	
	public Task getQuantity(){
		return this.quantity;
	}	

	public HashMap<String, String> getReward(){
		
		for(Job job : jobs){
			map=new HashMap<>();
			
			for(Robot robot:robots){
				map.put(robot, job);
			}
			int jobPathCost = 0;
			
			HashMap<Robot,ArrayList<ArrayList<move>>> paths = CommandCenter.generatePaths(map);
			Iterator it1 = paths.values().iterator();
			while(it1.hasNext()){
				ArrayList<ArrayList<move>> routes=new ArrayList<>();
				routes = (ArrayList<ArrayList<move>>) it1.next();	
				for(ArrayList<move> route : routes){
					jobPathCost += route.size();
				}
			}
			jobsWithValue.put(Integer.toString(job.getJobID()), 
					Float.toString( job.getJobReward() / 
							( (jobPathCost / robots.size()) * (job.sumOfWeight() / 50) )
					));
			
		}
		
		finalJobsWithValue=sortByValue(jobsWithValue);
		printMap(finalJobsWithValue);
		return finalJobsWithValue;						
	}	
	
	public static HashMap<String,String> sortByValue (HashMap<String,String> jobsWithValue){
			
			List<HashMap.Entry<String,String>> list=new LinkedList<HashMap.Entry<String,String>>(jobsWithValue.entrySet());
			//sorting based on values
			Collections.sort(list,new Comparator<HashMap.Entry<String,String>>(){
				public int compare(Entry<String,String> o1, Entry<String,String> o2){
					
						return (o1.getValue()).compareTo(o2.getValue());
				}
			});
			HashMap<String,String> sortedJobsWithValue=new HashMap<String,String>();
			for(HashMap.Entry<String,String> entry:list){
				sortedJobsWithValue.put(entry.getKey(), entry.getValue());
			}
			return sortedJobsWithValue;
	}	
	
	public static void printMap(HashMap<String, String> map){
		for (String key: map.keySet()){
            String k = key.toString();
            String value = map.get(key).toString();  
            System.out.println("JobID: " + k + "\t worth: " + value);  
		}
			
	}
	

}