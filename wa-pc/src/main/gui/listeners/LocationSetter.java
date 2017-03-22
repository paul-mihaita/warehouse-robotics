package main.gui.listeners;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import main.gui.GUI;
import utils.Location;
import utils.Robot;

public class LocationSetter implements EventHandler<ActionEvent> {
	
	private Robot r;
	private TextField location;

	public LocationSetter(Robot r, TextField location) {
		
		this.r = r;
		this.location = location;

	}

	@Override
	public void handle(ActionEvent arg0) {
		
		String lText = location.getText();
		String[] parts = lText.split(",");
		
		if (parts.length == 2){
			try {
				r.setCurrentLocation(new Location(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));

				r.setOrientation(new Location(Integer.parseInt(parts[0]) + 1, Integer.parseInt(parts[1])));

			} catch (NumberFormatException e) {
				GUI.log.error("Incorect Number format for setting location");
			}
		}

	}

}
