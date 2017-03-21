package main.gui;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import main.model.WarehouseFloor;
import rp.util.Rate;

public class TabMenuPane extends TabPane {

	private RobotPane robotHolder;
	private JobPane jobHolder;
	private EditPane editHolder;
	private Thread refresh;
	private Runnable refreshCommand;

	public TabMenuPane(WarehouseFloor model) {
		super();

		refreshCommand = new Runnable() {
			@Override
			public void run() {
				RobotPane.updateLabels();
				JobPane.updateLabels();
			}
		};

		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		robotHolder = new RobotPane(model);
		this.getTabs().add(new Tab("Robots", robotHolder));
		jobHolder = new JobPane(model);
		this.getTabs().add(new Tab("Jobs", jobHolder));
		editHolder = new EditPane(model);
		this.getTabs().add(new Tab("Edit", editHolder));

		refresh = new Thread() {
			@Override
			public void run() {
				Rate r = new Rate(10);
				while (!refresh.isInterrupted()) {
					refresh();
					r.sleep();
				}
			}
		};

	}

	public void refresh() {
		Platform.runLater(refreshCommand);
	}

	protected void interrupt() {
		refresh.interrupt();
	}
}
