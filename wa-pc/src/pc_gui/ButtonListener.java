package pc_gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class ButtonListener implements EventHandler<ActionEvent> {
	
	private int jobID;
	private Label status;

	public ButtonListener(int i) {
		this.jobID = i;
		this.status = status;
	}

	@Override
	public void handle(ActionEvent arg0) {
		System.out.println("Canceling job " + jobID);
	}

}
