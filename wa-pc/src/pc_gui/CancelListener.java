package pc_gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import utils.Job;

public class CancelListener implements EventHandler<ActionEvent> {
	
	private Job j;

	public CancelListener(Job j) {
		this.j = j;
	}

	@Override
	public void handle(ActionEvent arg0) {
		System.out.println("Canceling job " + j.getJobID());
		j.cancel();
		GUI.update();
	}

}
