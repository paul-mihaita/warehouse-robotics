package main.job;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import org.apache.log4j.Logger;

import utils.DropLocation;
import utils.Item;
import utils.Job;
import utils.Task;

public class Input {

	private static final Logger LOG = Logger.getLogger(Input.class);
	// SET TO TRUE IF IN THE FILES THE FIRST LINE IS THE TITLE
	boolean thereIsTheTitleFile = false;
	private final int MAX_LINES = Integer.MAX_VALUE;
	private ArrayList<Job> jobs = new ArrayList<Job>();
	private ArrayList<Job> trainignArray = new ArrayList<Job>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<DropLocation> dropLocations = new ArrayList<DropLocation>();
	private HashMap<Job, Boolean> jobsWithCancellation = new HashMap<Job, Boolean>();

	private final String PATH = "./data_files/";
	String path = "./data_files/";
	private final String JOBS_FILE = PATH + "jobs";
	private final String ITEMS_FILE = PATH + "items";
	private final String LOCATIONS_FILE = PATH + "locations";
	private final String DROPS_FILE = PATH + "drops";
	private final String CANCELLATIONS_FILE = PATH + "cancellations";
	private final String TRAINING_JOBS_FILE = PATH + "training_jobs";
	/******************************************************************/
	// Set limitation in reading file: first variable enable or not, second
	// number of jobs.
	private boolean limit = false;
	private final int MAX_LINES = 80;

	/******************************************************************/
	public Input(boolean haveTitle) {
		thereIsTheTitleFile = haveTitle;

		this.initializeListOfJobs(JOBS_FILE, ITEMS_FILE, LOCATIONS_FILE);
		this.initializeItemsList(LOCATIONS_FILE);
		this.initializedropLocations(DROPS_FILE);
		this.initializeCancellation(CANCELLATIONS_FILE, TRAINING_JOBS_FILE);

	}

	private void initializeCancellation(String cancellation_file, String training_job_file) {
		this.readTaskAndJobs(training_job_file, limit, true);
		this.initializeCancelledJobs(cancellation_file);
	}

	public Input() {

	}

	public boolean fileHaveTheExtension(String fileName) {
		return fileName.contains(".csv");
	}

	public boolean fileRight(String fileName) {
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

		LOG.debug("File exists: " + f.exists());
		return f.exists();
	}

	public boolean readTaskAndJobs(String fileName, boolean limitation, boolean training) {

		int k = 0;
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
				while (inFile.hasNext() && k < MAX_LINES) {
					if (limitation)
						k++;

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
						if (!training)
							jobs.add(new Job(jobsID, tasks));
						else
							trainignArray.add(new Job(jobsID, tasks));

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
							//LOG.debug("In Jobid: " + j.getJobID());
							for (Task t : j.getTasks()) {

								//LOG.debug("Item test name " + parts[2]);
								if (t.getItem().getItemName().equals(parts[2])) {

									int x = Integer.parseInt(parts[0]);
									int y = Integer.parseInt(parts[1]);

									//LOG.debug(t.getItem().getName() + "'s Location: " + x + " " + y);

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
		check = check && readTaskAndJobs(fileTaskAndJobs, limit, false);
		check = check && readItemAndRewardAndWeight(fileRewardsAndWeights);
		check = check && readItemAndXPositionAndYPosition(fileItemLocations);
		return check;
	}

	public boolean initializeItemsList(String fileName) {
		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				if (!fileHaveTheExtension(fileName)) {
					fileName = fileName + ".csv";
				}
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				boolean toDo = !thereIsTheTitleFile;
				while (inFile.hasNext()) {
					token = inFile.nextLine();
					if (toDo) {
						String[] parts = token.split(",");
						if (!items.contains(parts[2])) {
							//LOG.debug("item: " + parts[2] + " were added");
							Item temp = new Item(parts[2]);
							int x = Integer.parseInt(parts[0]);
							int y = Integer.parseInt(parts[1]);
							temp.setLocation(x, y);
							items.add(temp);
						}
					}
					toDo = true;
				}
				inFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		} else
			return false;
	}

	public boolean initializedropLocations(String fileName) {
		int i = 0;
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
				while (inFile.hasNext() && i < MAX_LINES) {
					token = inFile.nextLine();
					if (toDo) {
						String[] parts = token.split(",");
						if (parts.length == 2) {
							LOG.debug("Drop= " + token);
							LOG.debug("drop location: " + i);
							DropLocation temp = new DropLocation("DropLocation_" + (i + 1));
							int x = Integer.parseInt(parts[0].trim());
							int y = Integer.parseInt(parts[1].trim());
							LOG.debug("x= " + x + "\ty=" + y);
							temp.setLocation(x, y);
							dropLocations.add(temp);
						}
					}
					toDo = true;
				}
				inFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return true;
		} else
			return false;
	}

	public ArrayList<DropLocation> getIDropLocationArray() {
		return dropLocations;
	}

	public boolean initializeCancelledJobs(String fileName) {
		/////////////////////////////////////////////////// FILE RIGHT
		if (fileRight(fileName)) {
			try {
				if (!fileHaveTheExtension(fileName)) {
					fileName = fileName + ".csv";
				}
				Scanner inFile = new Scanner(new File(fileName));
				String token;
				boolean isCancelled;
				boolean toDo = !thereIsTheTitleFile;
				while (inFile.hasNext()) {
					// i++; //uncomment to limit the number of lines to read
					token = inFile.nextLine();
					if (toDo) {
						String[] parts = token.split(",");
						if (parts.length == 2) {
							// LOG.debug("JobID: " + parts[0] + "\tCancellation:
							// " + parts[1]);
							int jobID = Integer.parseInt(parts[0]);
							for (Job j : trainignArray) {
								if (j.getJobID() == jobID) {
									int ind = Integer.parseInt(parts[1]);
									if (ind == 1) {
										jobsWithCancellation.put(j, true);
									} else {
										jobsWithCancellation.put(j, false);

									}
									// LOG.debug("Inserted in HashMap:\nJob with
									// ID: " + j.getJobID()+ "\tand
									// Cancellation: " +
									// Boolean.parseBoolean(parts[1]));
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
		} else
			return false;
	}

	public Job getJobWithID(int id) {
		for (Job job : jobs) {
			if (job.getJobID() == id)
				return job;
		}
		return null;
	}

	public HashMap<Job, Boolean> getCancelledJobs() {
		return jobsWithCancellation;
	}

	public ArrayList<Item> getItemsArray() {
		return items;
	}

	public ArrayList<Job> getJobsArray() {
		return jobs;
	}

	public ArrayList<DropLocation> getDropLocations() {
		return dropLocations;
	}

	public HashMap<Job, Boolean> getJobsWithCancellation() {
		return jobsWithCancellation;
	}

}
