package main.gui;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import main.gui.listeners.LocationSetter;
import main.model.WarehouseFloor;
import utils.Robot;

public class EditPane extends GridPane {

	public EditPane(WarehouseFloor model) {
		super();

		int level = 0;
		for (Robot r : model.getRobots()) {

			Label robotTitle = new Label(r.getName());

			robotTitle.setFont(new Font(15));

			this.add(robotTitle, 0, level++, 2, 1);

			TextField location = new TextField();

			location.setText("<x>,<y>");

			location.setOnMouseClicked(new EventHandler<Event>() {
				@Override
				public void handle(Event event) {
					location.setText("");
				}
			});

			Button set = new Button("Set location");

			set.setOnAction(new LocationSetter(r, location));

			this.add(location, 0, level);
			this.add(set, 1, level++);

		}

	}

}
