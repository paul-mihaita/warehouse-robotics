package robot_gui;

import communication.Message;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;
import utils.Job;
import utils.Node;
import utils.Robot;
import utils.Task;
import utils.Location;

public class GUI extends Thread {
	
	Job job;
	Node node;
	Task task;
	Robot robot;
	private Message msg;

	public GUI(Robot robot, Message msg) {
		this.robot = robot;
		this.msg = msg;
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
		Button.LEFT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				LEFT = true;
				
			}

			@Override
			public void buttonReleased(Button b) {
				LEFT = false;
			}
		});
		Button.RIGHT.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				RIGHT = true;
				
			}

			@Override
			public void buttonReleased(Button b) {
				RIGHT = false;
			}
		});
		Button.ESCAPE.addButtonListener(new ButtonListener() {
			@Override
			public void buttonPressed(Button b) {
				ESCAPE = true;
				
			}

			@Override
			public void buttonReleased(Button b) {
				ESCAPE = false;
			}
		});

	}

	private int jobId = msg.getJob().getId();
	private boolean isOnJob = robot.isOnJob();
	private boolean isOnPickUp = robot.isOnPickup();
	private Location location = robot.getCurrentLocation();
	private int quantity = msg.getJob().getTask().getQuantity();
	private String itemName = msg.getJob().getTask().getItem().getItemName();
	private int numItems = 0;
	
	private boolean ENTER = false; 
	private boolean ESCAPE = false; 
	private boolean LEFT = false; 
	private boolean RIGHT = false; 
	
	private String pickup = "Pick-up";
	private String dropoff = "Drop off";
	private String jobIDisp = "Job: " + jobId;
	private String itemDisp = itemName + ": " + numItems + "/" + quantity;
	private String dropoffItems = "Drop off completed";
	private String locationDisp = "Location: " + location;
	private String itemMin = "No more items";
	
	public void run() {
		
		while (true) {
			
			if(isOnPickUp) {
				LCD.clear();
				LCD.drawString(pickup, 0, 1);
				jobId = msg.getJob().getId();
				LCD.drawString(jobIDisp, 0, 2);
				quantity = msg.getJob().getTask().getQuantity();
				itemName = msg.getJob().getTask().getItem().getItemName();
				LCD.drawString(itemDisp, 0, 3);
				location = robot.getCurrentLocation();
				LCD.drawString(locationDisp, 0, 4);
				if (ENTER) {
					LCD.clear();
					LCD.drawString(pickup, 0, 1);
					//jobId = msg.getJob().getId();
					LCD.drawString(jobIDisp, 0, 2);
					quantity = msg.getJob().getTask().getQuantity();
					itemName = msg.getJob().getTask().getItem().getItemName();
					LCD.drawString(itemDisp, 0, 3);
					location = robot.getCurrentLocation();
					LCD.drawString(locationDisp, 0, 4);
					
				}
				if (ESCAPE) {
					LCD.clear();
					LCD.drawString("BYE", 7, 3);
					System.exit(0);
				}
				if (LEFT) {
					if (numItems>0) {
						numItems--;
						itemDisp = itemName + ": " + numItems + "/" + quantity;
						LCD.drawString(itemDisp, 0, 3);
					}else
						LCD.drawString(itemMin, 0, 3);
				}
				if (RIGHT) {
					if (numItems<quantity) {
						numItems++;
						itemDisp = itemName + ": " + numItems + "/" + quantity;
						LCD.drawString(itemDisp, 0, 3);
					}else
						itemDisp = itemName + ": " + quantity + "/" + quantity;
						LCD.drawString(itemDisp, 0, 3);
				}
			}
			if (!isOnPickUp) {
				LCD.clear();
				numItems=0;
				robot.setOnJob(isOnJob);
				LCD.drawString(dropoff, 0, 1);
				jobId = msg.getJob().getId();
				LCD.drawString(jobIDisp, 0, 2);
				LCD.drawString(dropoffItems, 0, 3);
				location = robot.getCurrentLocation();
				LCD.drawString(locationDisp, 0, 4);
				if (ENTER) {
					LCD.clear();
					numItems=0;
					robot.setOnJob(isOnJob);
					LCD.drawString(dropoff, 0, 1);
					//jobId = msg.getJob().getId();
					LCD.drawString(jobIDisp, 0, 2);
					LCD.drawString(dropoffItems, 0, 3);
					location = robot.getCurrentLocation();
					LCD.drawString(locationDisp, 0, 4);
					
				}
				if (ESCAPE) {
					LCD.clear();
					LCD.drawString("BYE", 7, 3);
					System.exit(0);
				}
			}
			Delay.msDelay(200);
		}
	}

	
}
