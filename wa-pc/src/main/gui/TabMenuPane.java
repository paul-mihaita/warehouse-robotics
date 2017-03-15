package main.gui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import main.model.WarehouseFloor;

public class TabMenuPane extends TabPane{
	
	public TabMenuPane(WarehouseFloor model) {
		super();
		this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		GridPane robotHolder = new RobotPane(model);
		this.getTabs().add(new Tab("Robots", robotHolder));
		// TODO: GridPane jobHolder = new JobPane(model);
	}
}
