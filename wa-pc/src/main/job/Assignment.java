package main.job;

import java.util.*;

import utils.Job;
import utils.Robot;
import utils.Route;

public class Assignment extends Thread{

	HashSet<Robot> robots=new HashSet<Robot>();
	private boolean run;
	private boolean getList;
	private boolean finishedAssignedJobs;
	private boolean jobCompleted;
	private boolean canStart;
	private boolean robotGotLost;
	private boolean jobCancelled;
	private Selection selector;
	private AssignedJob currentJob;
	private AssignedJob assignedjob;
	private ArrayList<Job> jobs;
	private ArrayList<JobWorth> assignJobs;
	private LinkedList<Job> checklist;

	public Assignment(HashSet<Robot> robots, LinkedList<Job> trainingJobs){

		this.robots=robots;

		this.assignJobs=new ArrayList<>();

		this.canStart=false;

		this.jobCompleted=true;

		this.jobCancelled=false;

		this.robotGotLost=false;

		this.finishedAssignedJobs=false;

		this.getList=false;
		
		this.checklist=new LinkedList<>();

		this.start();
	}

	@Override
	public void run(){

		this.run=true;

		JobWorth jobToBeAssigned;

		while(this.run){

			if(this.canStart){

				this.selector=new Selection(this.robots,this.jobs,this.checklist);

				while(this.run){

					//give a new job if robots have completed job or has none assigned
					if(this.jobCompleted||this.jobCancelled){
						//if robots got lost or job was cancelled
						if(this.robotGotLost||this.jobCancelled){

							this.selector=new Selection(this.robots,this.jobs,this.checklist);
							this.getList=false;
							this.robotGotLost=false;
							this.jobCancelled=false;
						}
					}

					if(getList){

						this.assignJobs=this.selector.getSelectedList();	
						//get next job to be assigned
						jobToBeAssigned=this.assignJobs.remove(0);
						//remove from list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());
						//create new assigned job
						this.currentJob=this.assign(this.robots,jobToBeAssigned);

						this.jobCompleted=false;
					}

					try{
						Thread.sleep(100);
					} catch(InterruptedException e){
						e.printStackTrace();
					}

					if((finishedAssignedJobs)&&(this.assignJobs.size()<=0)){
						this.run=false;
					}
				}
			}
		}
	}	

	//creates a new assigned job for robot
	private AssignedJob assign(HashSet<Robot> robots, JobWorth jobworth) {

		Route route=null;
		if(robots.equals(this.robots)){

		}
								
		return new AssignedJob( assignedjob, route, robots);
		
	}							

	public void stopAssigning(){
		this.run=false;
	}
}
