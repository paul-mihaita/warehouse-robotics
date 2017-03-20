package robot_gui;

import communication.Message;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.util.Delay;
import utils.Robot;
import utils.Location;

public class GUI extends Thread {

	private Robot robot;

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

	private boolean isMoving;
	private int jobId; 
	private boolean isOnJob; 
	private boolean isOnPickUp;
	private Location location; 
	private int quantity; 
	private String itemName; 
	private int numItems;

	private boolean ENTER;
	private boolean ESCAPE;
	private boolean LEFT;
	private boolean RIGHT;

	private String pickup = "Pick-up";
	private String dropoff = "Drop off";
	private String jobIDisp = "Job: " + jobId;
	private String itemDisp = itemName + ": " + numItems + "/" + quantity;
	private String dropoffItems = "Drop off completed";
	private String locationDisp = "Location: " + location;
	private String itemMin = "No more items";

	public void run() {

		while (true) {
			jobId = msg.getJob().getId();
			isOnJob = robot.isOnJob();
			isOnPickUp = robot.isOnPickup();
			location = robot.getCurrentLocation();
			quantity = msg.getJob().getTask().getQuantity();
			itemName = msg.getJob().getTask().getItem().getItemName();
			isMoving = robot.isMoving();
			
			if (isMoving == false) {
				isOnPickUp = robot.isOnPickup();

				if (isOnPickUp) {
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
							jobId = msg.getJob().getId();
							LCD.drawString(jobIDisp, 0, 2);
							quantity = msg.getJob().getTask().getQuantity();
							itemName = msg.getJob().getTask().getItem().getItemName();
							LCD.drawString(itemDisp, 0, 3);
							location = robot.getCurrentLocation();
							LCD.drawString(locationDisp, 0, 4);
							isMoving = true;

						}
						if (ESCAPE) {
							LCD.clear();
							LCD.drawString("BYE", 7, 3);
							System.exit(0);
						}
						if (LEFT) {
							if (numItems > 0) {
								numItems--;
								itemDisp = itemName + ": " + numItems + "/" + quantity;
								LCD.drawString(itemDisp, 0, 3);
							} else
								LCD.drawString(itemMin, 0, 3);
						}
						if (RIGHT) {
							if (numItems < quantity) {
								numItems++;
								itemDisp = itemName + ": " + numItems + "/" + quantity;
								LCD.drawString(itemDisp, 0, 3);
							} else {
								itemDisp = itemName + ": " + quantity + "/" + quantity;
								LCD.drawString(itemDisp, 0, 3);
							}
						}
					}
					if (!isOnPickUp) {
						LCD.clear();
						numItems = 0;
						isOnJob = robot.isOnJob();
						robot.setOnJob(isOnJob);
						LCD.drawString(dropoff, 0, 1);
						jobId = msg.getJob().getId();
						LCD.drawString(jobIDisp, 0, 2);
						LCD.drawString(dropoffItems, 0, 3);
						location = robot.getCurrentLocation();
						LCD.drawString(locationDisp, 0, 4);
						if (ENTER) {
							LCD.clear();
							numItems = 0;
							robot.setOnJob(isOnJob);
							LCD.drawString(dropoff, 0, 1);
							jobId = msg.getJob().getId();
							LCD.drawString(jobIDisp, 0, 2);
							LCD.drawString(dropoffItems, 0, 3);
							location = robot.getCurrentLocation();
							LCD.drawString(locationDisp, 0, 4);
							isMoving = true;

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

	}
}
