package main.gui.listeners;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import main.gui.GUI;
import main.gui.RobotPane;
import main.model.WarehouseFloor;

public class StartListener implements EventHandler<ActionEvent> {

	private WarehouseFloor model;

	public StartListener(WarehouseFloor model) {
		this.model = model;
	}

	@Override
	public void handle(ActionEvent event) {

		model.startRobots();
		GUI.refresh();

	}

}
