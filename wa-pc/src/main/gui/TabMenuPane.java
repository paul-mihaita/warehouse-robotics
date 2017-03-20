package main.gui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.model.WarehouseFloor;
import rp.util.Rate;

public class TabMenuPane extends TabPane{
	
	private RobotPane robotHolder;
	private JobPane jobHolder;
	private EditPane editHolder;
	private Thread refresh;
	
	public TabMenuPane(WarehouseFloor model) {
		super();
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		robotHolder = new RobotPane(model);
		this.getTabs().add(new Tab("Robots", robotHolder));
		jobHolder = new JobPane(model);
		this.getTabs().add(new Tab("Jobs", jobHolder));
		editHolder = new EditPane(model);
		this.getTabs().add(new Tab("Edit", editHolder));
		
		refresh = new Thread(){
			@Override
			public void run() {
				Rate r = new Rate(10);
				while(!this.isInterrupted()){
					refresh();
					r.sleep();
				}
			}
		};
		
		refresh.start();

	}

	public void refresh() {
		RobotPane.updateLabels();
		JobPane.updateLabels();
	}

	protected void interrupt() {
		refresh.interrupt();
	}
}
