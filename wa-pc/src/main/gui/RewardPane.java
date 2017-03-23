package main.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import main.model.WarehouseFloor;
import utils.Job;

public class RewardPane extends GridPane {

	private WarehouseFloor model;

	public RewardPane(WarehouseFloor model) {
		super();
		this.model = model;
		this.setVgap(10);
		this.setHgap(10);

		update();
	}

	public void update() {
		RewardPane.this.getChildren().removeIf(p -> true);

		Label iDTitle = new Label("Completed Jobs");
		RewardPane.this.add(iDTitle, 0, 0);

		Label rewTitle = new Label("Reward");
		RewardPane.this.add(rewTitle, 1, 0);

		float total = 0f;
		int level = 1;
		for (Job j : model.getJobs().values()) {
			if (j.isCompleted()) {
				Label id = new Label("" + j.getJobID());
				RewardPane.this.add(id, 0, level);
				Label rewa = new Label("" + j.getJobReward());
				RewardPane.this.add(rewa, 1, level++);

				total += j.getJobReward();
			}
		}

		Label totalTitle = new Label("Total reward");
		RewardPane.this.add(totalTitle, 0, level);
		Label totalLabel = new Label("" + total);
		RewardPane.this.add(totalLabel, 1, level);

	}

}
