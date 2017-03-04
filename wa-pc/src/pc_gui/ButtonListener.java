package pc_gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import utils.Job;

public class ButtonListener implements EventHandler<ActionEvent> {
	
	private Job j;

	public ButtonListener(Job j) {
		this.j = j;
	}

	@Override
	public void handle(ActionEvent arg0) {
		System.out.println("Canceling job " + j.getJobID());
		j.cancel();
		GUI.update();
	}

}
