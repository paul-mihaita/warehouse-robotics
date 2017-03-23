package robot_gui;

import communication.Message;
import communication.CommConst.command;
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

	private String pickup;
	private String dropoff;
	private String jobIDisp;
	private String itemDisp;
	private String dropoffItems;
	private String locationDisp;
	private String itemMin;

	public void update() {
		jobId = msg.getJob().getId();
		isOnJob = robot.isOnJob();
		isOnPickUp = robot.isOnPickup();
		location = robot.getCurrentLocation();
		quantity = msg.getJob().getTask().getQuantity();
		itemName = msg.getJob().getTask().getItem().getItemName();
		isMoving = robot.isMoving();

		pickup = "Pick-up";
		dropoff = "Drop off";
		jobIDisp = "Job: " + jobId;
		itemDisp = itemName + ": " + numItems + "/" + quantity;
		dropoffItems = "Drop off completed";
		locationDisp = "Loc: " + location.toString();
		itemMin = "No more items";
	}

	public void run() {

		while (true) {
			update();
			if (!isMoving) {
				if (isOnPickUp) {
					drawUI(pickup, itemDisp);
					if (ENTER) {
						drawUI(pickup,itemDisp);
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
							itemDisp = "Item "+ itemName + ": " + numItems + "/" + quantity;
							LCD.drawString(itemDisp, 1, 3);
						} else
							LCD.drawString(itemMin, 1, 3);
					}
					if (RIGHT) {
						if (numItems < quantity) {
							numItems++;
							itemDisp = itemName + ": " + numItems + "/" + quantity;
							LCD.drawString(itemDisp, 1, 3);
						} else {
							itemDisp = itemName + ": " + quantity + "/" + quantity;
							LCD.drawString(itemDisp, 1, 3);
						}
					}
					if (numItems == quantity) {
						if (msg.getCommand() != command.Finish){
							numItems = 0;
							drawUI("Item picked up", "");
							msg.setCommand(command.Finish); //we finished this pick up so tell the pc
						}
					}
				}
				else {
					numItems = 0;
					drawUI(dropoff, dropoffItems);
					if (ENTER) {
						numItems = 0;
						drawUI(dropoff, dropoffItems);
						isMoving = true;
						msg.setCommand(command.Finish); //we finished this drop off so tell the pc
					}
					if (ESCAPE) {
						LCD.clear();
						LCD.drawString("BYE", 7, 3);
						System.exit(0);
					}
				}
				Delay.msDelay(400);
			}
		}
	}

	private void drawUI(String state, String item) {
		LCD.clear();
		LCD.drawString(state, 1, 1);
		LCD.drawString(jobIDisp, 1, 2);
		LCD.drawString(item, 1, 3);
		LCD.drawString(locationDisp, 1, 4);
	}

}
