package robot_gui;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;
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
	public GUI(Robot robot) {
		this.robot = robot;
		Button.ENTER.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				ENTER = true;
				
			}

			@Override
			public void buttonReleased(Button b) {
				ENTER = false;
			}
		});
	}

	//private int jobId = job.getJobID();
	private int jobId = 1002;
	//private boolean isOnJob = robot.isOnJob();
	private boolean isOnJob = true;
	//private boolean isOnPickUp = robot.isOnPickup();
	private boolean isOnPickUp = true;
	//private Location location = robot.getCurrentLocation();
	private String location = "location";
	//private int quantity = task.getQuantity();
	private int quantity = 5;
	//private String itemName = task.getItem().getItemName();
	private String itemName = "test item";
	private int numItems = 0;
	
	private boolean ENTER = false; //Button.ENTER.isPressed();
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
					LCD.clear();
					LCD.drawString(pickup, 2, 1);
					LCD.drawString(jobIDisp, 2, 2);
					LCD.drawString(itemDisp, 2, 3);
					//location = node.getLocation();
					LCD.drawString(locationDisp, 2, 4);
					
				}
				if (ESCAPE) {
					LCD.clear();
					LCD.drawString("BYE", 7, 3);
					System.exit(0);
				}
				if (LEFT) {
					if (numItems>=0) {
						numItems--;
						LCD.drawString(itemDisp, 2, 3);
					}else
						LCD.drawString(itemMin, 2, 3);
				}
				if (RIGHT) {
					if (numItems<=quantity) {
						numItems++;
						LCD.drawString(itemDisp, 2, 3);
					}else
						LCD.drawString(itemMax, 2, 3);
				}
			}
			if (!isOnPickUp) {
				if (ENTER) {
					LCD.clear();
					numItems=0;
					robot.setOnJob(isOnJob);
					LCD.drawString(dropoff, 2, 1);
					LCD.drawString(jobIDisp, 2, 2);
					LCD.drawString(dropoffItems, 2, 3);
					//location = robot.getCurrentLocation();
					LCD.drawString(locationDisp, 2, 4);
					
				}
				if (ESCAPE) {
					LCD.clear();
					LCD.drawString("BYE", 7, 3);
					System.exit(0);
				}
			}
			Delay.msDelay(100);
		}
	}

	
}
