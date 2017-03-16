package main.job;

import java.util.*;

import utils.Job;
import utils.Robot;
import utils.Route;

public class Assignment extends Thread{

	private Robot robot1,robot2,robot3;
	private boolean run;
	private boolean getList1,getList2,getList3;
	private boolean finishedAssignedJobs1,finishedAssignedJobs2,finishedAssignedJobs3;
	private boolean jobCompleted1,jobCompleted2,jobCompleted3;
	private boolean canStart1,canStart2,canStart3;
	private boolean robotGotLost1,robotGotLost2,robotGotLost3;
	private boolean jobCancelled1,jobCancelled2,jobCancelled3;
	private Selection selector1,selector2,selector3;
	private AssignedJob currentJob1,currentJob2,currentJob3;
	private ArrayList<Job> jobs;
	private ArrayList<JobWorth> assignJobs1, assignJobs2,assignJobs3;

	public Assignment(Robot robot1, Robot robot2, Robot robot3){

		this.robot1=robot1;
		this.robot2=robot2;
		this.robot3=robot3;

		this.assignJobs1=new ArrayList<>();
		this.assignJobs2=new ArrayList<>();
		this.assignJobs3=new ArrayList<>();

		this.canStart1=false;
		this.canStart2=false;
		this.canStart3=false;

		this.jobCompleted1=true;
		this.jobCompleted2=true;
		this.jobCompleted3=true;

		this.jobCancelled1=false;
		this.jobCancelled2=false;
		this.jobCancelled3=false;

		this.robotGotLost1=false;
		this.robotGotLost2=false;
		this.robotGotLost3=false;

		this.finishedAssignedJobs1=false;
		this.finishedAssignedJobs2=false;
		this.finishedAssignedJobs3=false;

		this.getList1=false;
		this.getList2=false;
		this.getList3=false;

		//cancellation

		this.start();
	}

	@Override
	public void run(){

		this.run=true;

		JobWorth jobToBeAssigned;
		int num=0;

		while(this.run){

			//robot1
			if(this.canStart1){

				this.selector1=new Selection(this.robot1,this.jobs);

				while(this.run){

					//give a new job if robot has completed job
					if(this.jobCompleted1||this.jobCancelled1){
						//if the robot got lost or job was cancelled
						if(this.robotGotLost1||this.jobCancelled1){

							num++;
							this.selector1=new Selection(this.robot1,this.jobs);
							this.getList1=false;
							this.robotGotLost1=false;
							this.jobCancelled1=false;
						}
					}

					if(getList1){

						this.assignJobs1=this.selector1.getSelectedList();	
						//get next job to be assigned
						jobToBeAssigned=this.assignJobs1.remove(0);
						//remove from list of jobs
						this.jobs.remove(jobToBeAssigned.getJob());
						//create new assigned job
						this.currentJob1=this.assign(this.robot1,jobToBeAssigned);


						this.jobCompleted1=false;
					}
					if((finishedAssignedJobs1)&&(this.assignJobs1.size()<=0)){
						this.run=false;
					}
				}
			}	


			//robot2
			if(this.canStart2){

				this.selector2=new Selection(this.robot2,this.jobs);

				while(this.run){

					if(this.jobCompleted2||this.jobCancelled2){
						
						if(this.robotGotLost2||this.jobCancelled2){
							
						num++;
						this.selector2=new Selection(this.robot2,this.jobs);
						this.getList2=false;
						this.robotGotLost2=false;
						this.jobCancelled2=false;
						}
					}

					if(getList2){

						this.assignJobs2=this.selector2.getSelectedList();
						jobToBeAssigned=this.assignJobs2.remove(0);
						this.jobs.remove(jobToBeAssigned.getJob());
						this.currentJob2=this.assign(robot2, jobToBeAssigned);

						this.jobCompleted2=false;
					}

					if((finishedAssignedJobs2)&&(this.assignJobs2.size()<=0)){
						this.run=false;
					}
				}
			}

			//robot3
			if(this.canStart3){

				this.selector3=new Selection(this.robot3,this.jobs);

				while(this.run){

					if(this.jobCompleted3||this.jobCancelled3){
						
						if(this.robotGotLost3||this.jobCancelled3){
						num++;
						this.selector3=new Selection(this.robot3,this.jobs);
						this.getList3=false;
						this.robotGotLost3=false;
						this.jobCancelled3=false;
						}
					}

					if(getList3){
						this.assignJobs3=this.selector3.getSelectedList();
						jobToBeAssigned=this.assignJobs3.remove(0);
						this.jobs.remove(jobToBeAssigned.getJob());
						this.currentJob3=this.assign(robot3, jobToBeAssigned);

						this.jobCompleted3=false;
					}

					if((finishedAssignedJobs3) && (this.assignJobs3.size()<=0)){
						this.run=false;
					}
				}
			}
		}
	}

	//creates a new assigned job for robot
	private AssignedJob assign(Robot robot, JobWorth jobWorth) {

		Route route;
		if(robot.equals(this.robot1)){

		}else if(robot.equals(this.robot2)){

		}else if(robot.equals(this.robot3)){

		}

		return new AssignedJob(jobWorth.getJob(),route,robot);
	}

	public void stopAssigning(){
		this.run=false;
	}
}
