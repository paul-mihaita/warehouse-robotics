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
	private boolean isOnJob = robot.isOnJob();
	private Location location = node.getLocation();
	private int quantity = task.getQuantity();
	private String itemName = task.getItem().getItemName();
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
	
	private boolean PICKUP=true; //get from a method
	private boolean DROPOFF=false;
	
	public void run() {
		
		while (true) {
			
			if(PICKUP) {
				if (ENTER) {
					LCD.drawString(pickup, 4, 2);
					LCD.drawString(jobIDisp, 4, 4);
					LCD.drawString(itemDisp, 4, 8);
					location = node.getLocation();
					LCD.drawString(locationDisp, 4, 10);
					//PICKUP=false;
					//DROPOFF=true;
					
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
			if (DROPOFF) {
				if (ENTER) {
					numItems=0;
					isOnJob = false;
					LCD.drawString(dropoff, 4, 2);
					LCD.drawString(jobIDisp, 4, 4);
					LCD.drawString(dropoffItems, 4, 8);
					location = node.getLocation();
					LCD.drawString(locationDisp, 4, 10);
					//DROPOFF=false;
					//PICKUP=true;
					
				}
				if (ESCAPE) {
				
				}
				
			
			}
		}
	}

	 
	//print if on way to pickup or drop off *
	//print jobId *
	//print location of robot *
	
}
