package job;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import utils.Item;
import utils.Job;
import utils.Task;

public class Input {

	private ArrayList<Job> jobs = new ArrayList<Job>();
	// private ArrayList<Item> items = new ArrayList<Item>();

	private boolean fileRight(String fileName) {
		String file = fileName + ".csv";
		File f = new File(file);
		return !f.exists();
	}

	public boolean readTaskAndJobs(String fileName) {
		ArrayList<Task> tasks = new ArrayList<Task>();
		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				// CREATE SCANNER TO READ FILE
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				int jobsID;
				// WHILE UNTIL THE END OF THE FILE
				while (inFile.hasNext()) {
					token = inFile.nextLine();

					// READ AND SPLIT THE LINE WITH THE CARACTER ","
					String[] parts = token.split(",");

					// FIRST ELEMENT OF THE ARRAY PARTS IS THE JOBS ID
					jobsID = Integer.parseInt(parts[0]);
					// READ ALL THE TASK OF THE JOB
					for (int i = 1; i < parts.length; i += 2) {
						tasks.add(new Task(parts[i], Integer.parseInt(parts[i + 1])));
					}
					// ADD THE JOB TO THE ARRAY LIST OF JOBS
					jobs.add(new Job(jobsID, tasks));
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
				// CREATE SCANNER TO READ FILE
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				// WHILE UNTIL THE END OF THE FILE
				while (inFile.hasNext()) {
					token = inFile.nextLine();
					// READ AND SPLIT THE LINE WITH THE CARACTER ","
					String[] parts = token.split(",");

					// ADD TO EVERY SINGLE TASK THE ITEM WEIGHT AND THE ITEM
					// REWARD
					for (Job j : jobs) {
						for (Task t : j.geTasks()) {
							if (t.getTaskItem().getItemName().equals(parts[0])) {
								t.getTaskItem().setReward(Float.parseFloat(parts[1]));
								t.getTaskItem().setWeight(Float.parseFloat(parts[2]));
							}
						}
					}
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
}
