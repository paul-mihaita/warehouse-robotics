package main.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import utils.Job;
import utils.Location;
import utils.Robot;

public class Selection extends Thread{

	private Robot robot;
	private boolean run;
	private ArrayList<Job> copyJobs;
	private Location startLocation;
	private LinkedList<JobWorth> selectedlist;
	private LinkedList<JobWorth> convertedlist;


	public Selection(Robot robot, ArrayList<Job> jobs){
		
		this.robot=robot;
		this.startLocation=this.robot.getCurrentLocation();
		this.copyJobs=new ArrayList<Job>();
		
		//get a copy of the available jobs
		for(Job j:jobs){
			this.copyJobs.add(j);
		}
		
		this.start();
	}
	
	public void run(){
		
		this.run=true;
		
		JobWorth bestJob;
		this.selectedlist=new LinkedList<>();
		
		while(run){
			
			//if entire list has been selected then stop
			if(this.copyJobs.size()<=0){
				
				break;
			}
			//work out how good a job is by creating a jobworth object each
			this.convertedlist=this.convertList(this.startLocation);
			assert(this.convertedlist!=null);
			//get the best job
			bestJob=this.selectBestJobs(this.convertedlist);
			//remove from job list
			this.copyJobs.remove(bestJob.getJob());
			//add to list of selected jobs
			this.selectedlist.add(bestJob);
			
		}
	}
	
	public LinkedList<JobWorth> getSelectedList(){
		
		return this.selectedlist;
	}
	
	public LinkedList<JobWorth> convertList(Location startLocation){
		
		//check there are jobs to convert
		assert(this.copyJobs.size()>0);
		//make an empty list of jobworths
		LinkedList<JobWorth> jobWorths=new LinkedList<JobWorth>();
		for(Job job:this.copyJobs){//calculate worth of jobs and add to list
			
			JobWorth jobworth=new JobWorth(this.robot, job,startLocation);
			jobWorths.add(jobworth);
		}
		
		return jobWorths;
	}
	
	public JobWorth selectBestJobs(LinkedList<JobWorth> jobWorths){
		//check that there are elements in the list
		assert(jobWorths.size()>0);
		return Collections.max(jobWorths,null);
	}
	
	public void stopSelection(){
		
		this.run=false;
	}
}
