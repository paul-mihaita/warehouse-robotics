package main.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import bootstrap.Start;
import utils.Job;
import utils.Task;

public class Input {

	private static final Logger LOG = Logger.getLogger(Input.class);
	// SET TO TRUE IF IN THE FILES THE FIRST LINE IS THE TITLE
	boolean thereIsTheTitleFile = false;

	private ArrayList<Job> jobs = new ArrayList<Job>();

	public Input(boolean haveTitle) {
		thereIsTheTitleFile = haveTitle;
	}

	private boolean fileHaveTheExtension(String fileName) {
		return fileName.contains(".csv");
	}

	private boolean fileRight(String fileName) {
		LOG.debug("Reading if the file have the extension");
		String file;
		if (fileHaveTheExtension(fileName)) {
			file = fileName;
			LOG.debug("yes");
		} else {
			file = fileName + ".csv";
			LOG.debug("no");
		}
		File f = new File(file);

		Start.log.debug("File exists: " + f.exists());
		return f.exists();
	}

	public boolean readTaskAndJobs(String fileName) {

		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				// CREATE SCANNER TO READ FILE
				if (!fileHaveTheExtension(fileName)) {
					fileName = fileName + ".csv";
				}
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				int jobsID;
				// WHILE UNTIL THE END OF THE FILE
				boolean toDo = !thereIsTheTitleFile;
				while (inFile.hasNext()) {
					ArrayList<Task> tasks = new ArrayList<Task>();
					token = inFile.nextLine();
					if (toDo) {
						// READ AND SPLIT THE LINE WITH THE CARACTER ","
						String[] parts = token.split(",");

						// FIRST ELEMENT OF THE ARRAY PARTS IS THE JOBS ID
						jobsID = Integer.parseInt(parts[0]);
						// READ ALL THE TASK OF THE JOB
						for (int i = 1; i < parts.length; i += 2) {
							boolean alreadyThere = true;
							for (Task check : tasks) {
								if (check.getItem().getItemName().equals(parts[i])) {
									int sumQuantity = check.getQuantity() + Integer.parseInt(parts[i + 1]);
									check.setQuantity(sumQuantity);
									alreadyThere = false;
								}
							}
							if (alreadyThere) {
								tasks.add(new Task(parts[i], Integer.parseInt(parts[i + 1])));
							}
						}
						// ADD THE JOB TO THE ARRAY LIST OF JOBS
						jobs.add(new Job(jobsID, tasks));

					}
					toDo = true;
				}
				inFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		} ////////////////////////////////////////////////// FILE NOT RIGHT
		else
			return false;
	}

	public boolean readItemAndRewardAndWeight(String fileName) {

		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				if (!fileHaveTheExtension(fileName)) {
					fileName = fileName + ".csv";
				}
				// CREATE SCANNER TO READ FILE
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				boolean toDo = !thereIsTheTitleFile;
				// WHILE UNTIL THE END OF THE FILE
				while (inFile.hasNext()) {
					token = inFile.nextLine();
					if (toDo) {
						// READ AND SPLIT THE LINE WITH THE CARACTER ","
						String[] parts = token.split(",");

						// ADD TO EVERY SINGLE TASK THE ITEM WEIGHT AND THE ITEM
						// REWARD
						for (Job j : jobs) {
							for (Task t : j.getTasks()) {
								if (t.getItem().getItemName().equals(parts[0])) {
									t.getItem().setReward(Float.parseFloat(parts[1]));
									t.getItem().setWeight(Float.parseFloat(parts[2]));
								}
							}
						}
					}
					toDo = true;

				}
				inFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		} ////////////////////////////////////////////////// FILE NOT RIGHT
		else
			return false;
	}

	public boolean readItemAndXPositionAndYPosition(String fileName) {
		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				if (!fileHaveTheExtension(fileName)) {
					fileName = fileName + ".csv";
				}
				// CREATE SCANNER TO READ FILE
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				boolean toDo = !thereIsTheTitleFile;
				// WHILE UNTIL THE END OF THE FILE
				while (inFile.hasNext()) {
					token = inFile.nextLine();
					if (toDo) {
						// READ AND SPLIT THE LINE WITH THE CARACTER ","
						String[] parts = token.split(",");

						// ADD TO EVERY SINGLE TASK THE ITEM WEIGHT AND THE
						// ITEM
						// REWARD
						for (Job j : jobs) {
							Start.log.debug("In Jobid: " + j.getJobID());
							for (Task t : j.getTasks()) {
								if (t.getItem().getItemName().equals(parts[2])) {

									int x = Integer.parseInt(parts[0]);
									int y = Integer.parseInt(parts[1]);

									Start.log.debug(t.getItem().getName() + "'s Location: " + x + " "+ y);

									t.getItem().setLocation(x, y);
								}
							}

						}
					}
					toDo = true;
				}
				inFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		} ////////////////////////////////////////////////// FILE NOT RIGHT
		else
			return false;
	}

	public boolean initializeListOfJobs(String fileTaskAndJobs, String fileRewardsAndWeights,

			String fileItemLocations) {

		boolean check = true;
		check = check && readTaskAndJobs(fileTaskAndJobs);
		check = check && readItemAndRewardAndWeight(fileRewardsAndWeights);
		check = check && readItemAndXPositionAndYPosition(fileItemLocations);
		return check;
	}

	public ArrayList<Job> getJobsArray() {
		return jobs;
	}
}
