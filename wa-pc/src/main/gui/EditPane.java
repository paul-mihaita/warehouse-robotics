package main.gui;

import javafx.scene.layout.GridPane;
import main.model.WarehouseFloor;
import utils.Robot;

public class EditPane extends GridPane{

	private WarehouseFloor model;

	public EditPane(WarehouseFloor model) {
		super();
		this.model = model;
		
		for(Robot r : model.getRobots()){
			
		}

	}

}
