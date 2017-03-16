package main.gui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.model.WarehouseFloor;

public class TabMenuPane extends TabPane{
	
	private RobotPane robotHolder;
	private JobPane jobHolder;
	
	public TabMenuPane(WarehouseFloor model) {
		super();
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		robotHolder = new RobotPane(model);
		this.getTabs().add(new Tab("Robots", robotHolder));
		jobHolder = new JobPane(model);
		this.getTabs().add(new Tab("Jobs", jobHolder));
	}

	public void refresh() {
		RobotPane.updateLabels();
		JobPane.updateLabels();
	}
}
