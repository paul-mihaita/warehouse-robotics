package robot_gui;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import utils.Job;
import utils.Location;
import utils.Node;
import utils.Robot;
import utils.Task;

public class GUI implements Runnable {
	
	Job job;
	Node node;
	Task task;
	Robot robot;

	private int jobId = job.getJobID();
	//private int jobId = 1002;
	private boolean isOnJob = robot.isOnJob();
	//private boolean isOnJob = true;
	private boolean isOnPickUp = robot.isOnPickup();
	//private boolean isOnPickUp = true;
	private Location location = robot.getCurrentLocation();
	private int quantity = task.getQuantity();
	//private int quantity = 5;
	private String itemName = task.getItem().getItemName();
	//private String itemName = "test item";
	private int numItems = 0;
	
	private boolean ENTER = Button.ENTER.isPressed();
	private boolean ESCAPE = Button.ESCAPE.isPressed();
	private boolean LEFT = Button.LEFT.isPressed();
	private boolean RIGHT = Button.RIGHT.isPressed();
	
	private String pickup = "Pick-up";
	private String dropoff = "Drop off";
	private String jobIDisp = "Current job: " + jobId;
	private String itemDisp = itemName + ": " + numItems + "/" + quantity;
	private String dropoffItems = "Drop off completed";
	private String locationDisp = "Current location: " + location;
	private String itemMax = "Reached max num of items";
	private String itemMin = "No more items";
	
	public void run() {
		
		while (true) {
			
			if(isOnPickUp) {
				if (ENTER) {
					LCD.drawString(pickup, 4, 2);
					LCD.drawString(jobIDisp, 4, 4);
					LCD.drawString(itemDisp, 4, 8);
					location = node.getLocation();
					LCD.drawString(locationDisp, 4, 10);
					
					
				}
				if (ESCAPE) {
					
				}
				if (LEFT) {
					if (numItems>=0) {
						numItems--;
						LCD.drawString(itemDisp, 4, 8);
					}else
						LCD.drawString(itemMin, 4, 8);
				}
				if (RIGHT) {
					if (numItems<=quantity) {
						numItems++;
						LCD.drawString(itemDisp, 4, 8);
					}else
						LCD.drawString(itemMax, 4, 8);
				}
			}
			if (!isOnPickUp) {
				if (ENTER) {
					numItems=0;
					robot.setOnJob(isOnJob);
					LCD.drawString(dropoff, 4, 2);
					LCD.drawString(jobIDisp, 4, 4);
					LCD.drawString(dropoffItems, 4, 8);
					location = robot.getCurrentLocation();
					LCD.drawString(locationDisp, 4, 10);
					
					
				}
				if (ESCAPE) {
				
				}
				
			
			}
		}
	}

	
}
