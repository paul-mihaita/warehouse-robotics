package main.gui.listeners;

import javafx.event.Event;
import javafx.event.EventHandler;
import main.gui.MapPane;

public class MouseFlip implements EventHandler<Event>{
	
	private boolean clicked;
	private MapPane pane;
	
	public MouseFlip(MapPane pane) {
		this.clicked = false;
		this.pane = pane;
	}

	@Override
	public void handle(Event event) {
		
		if (clicked){
			pane.setScaleY(1);
			clicked = false;
		} else {
			pane.setScaleY(-1);
			clicked = true;
		}
		
	}

}
