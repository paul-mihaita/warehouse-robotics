package main.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import utils.Job;
import utils.Location;
import utils.Robot;

public class Selection extends Thread {

	private HashSet<Robot> robot;
	private boolean run;
	private Location startLocation;
	private ArrayList<Job> copyJobs;
	private ArrayList<JobWorth> selectedlist;
	private ArrayList<JobWorth> convertedlist;
	private LinkedList<Job> checklist;

	public Selection(HashSet<Robot> robot, ArrayList<Job> jobs, LinkedList<Job> checklist) {

		this.checklist = checklist;
		this.robot = robot;
		//this.startLocation = this.robot.getCurrentLocation();
		this.copyJobs = new ArrayList<Job>();

		// get a copy of the available jobs
		for (Job j : jobs) {
			this.copyJobs.add(j);
		}

		this.start();
	}

	public void run() {

		this.run = true;

		JobWorth bestJob;
		this.selectedlist = new ArrayList<>();

		while (run) {

			// if entire list has been selected then stop
			if (this.copyJobs.size() <= 0) {

				break;
			}
			// work out how good a job is by creating a jobworth object each
			this.convertedlist = this.convertList(this.startLocation);
			assert (this.convertedlist != null);
			// get the best job
			bestJob = this.selectBestJobs(this.convertedlist);
			this.checklist.add(bestJob.getJob());
			// remove from job list
			this.copyJobs.remove(bestJob.getJob());
			// add to list of selected jobs
			this.selectedlist.add(bestJob);

		}
	}

	public ArrayList<JobWorth> getSelectedList() {

		return this.selectedlist;
	}

	public ArrayList<JobWorth> convertList(Location startLocation) {

		// check there are jobs to convert
		assert (this.copyJobs.size() > 0);
		// make an empty list of jobworths
		ArrayList<JobWorth> jobWorths = new ArrayList<JobWorth>();

		for (Job job : this.copyJobs) {// calculate worth of jobs and add to
										// list
			JobWorth jobworth=new JobWorth(copyJobs, robot);
			jobWorths.add(jobworth);
		}

		return jobWorths;
	}

	public JobWorth selectBestJobs(ArrayList<JobWorth> jobWorths) {

		// check that there are elements in the list
		assert (jobWorths.size() > 0);

		ArrayList<JobWorth> jobWorthClone = new ArrayList<JobWorth>();

		for (JobWorth jobworth : jobWorths) {

			jobWorthClone.add(jobworth);

		}
		JobWorth best = Collections.max(jobWorthClone, null);

		if (this.checklist.contains(best)) {
			jobWorthClone.remove(best);
			best = Collections.max(jobWorthClone, null);
		}
		return best;
	}

	public void stopSelection() {

		this.run = false;
	}
}
