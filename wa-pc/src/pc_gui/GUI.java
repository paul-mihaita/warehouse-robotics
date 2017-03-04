package pc_gui;
import java.util.HashMap;
import java.util.HashSet;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import utils.Job;

public class GUI extends Application {

	public static final int WIDTH = 150;
	public static final int HEIGHT = 600;

	private static HashSet<Job> jobs;
	private static HashMap<Job, Label> jobLabels;

	public static void create(HashSet<Job> jobs) {
		GUI.jobs = jobs;
		GUI.jobLabels = new HashMap<Job, Label>();
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Warehouse Controller");

		ScrollPane scroll = new ScrollPane();

		scroll.setMaxHeight(HEIGHT);
		scroll.setMinWidth(WIDTH);

		scroll.setHbarPolicy(ScrollBarPolicy.NEVER);

		GridPane grid = new GridPane();

		grid.setMaxHeight(HEIGHT);
		grid.setMinWidth(WIDTH);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(WIDTH / 100);
		grid.setVgap(WIDTH / 100);

		Button startButton = new Button("Start");

		startButton.setTextFill(Color.GREEN);
		startButton.setTextAlignment(TextAlignment.CENTER);
		startButton.setFont(new Font(20));
		
		startButton.setMinWidth(WIDTH);

		grid.add(startButton, 0, 0);

		int level = 1;
		for (Job j : jobs) {

			GridPane jobPane = new GridPane();

			jobPane.setMinWidth(WIDTH * 0.95);

			jobPane.setStyle("-fx-border-color: blue");

			Label l = new Label("Job ID: " + j.getJobID());
			Button b = new Button("Cancel");
			Label s = new Label("Status:");
			Label status = new Label(j.getStatus());

			status.setTextFill(statusColor(j.getStatus()));

			b.setOnAction(new CancelListener(j));

			jobPane.setAlignment(Pos.CENTER_LEFT);
			jobPane.setHgap(WIDTH / 10);
			jobPane.setVgap(WIDTH / 10);

			jobPane.add(l, 0, 0);
			jobPane.add(s, 0, 1);

			jobPane.add(b, 1, 0);
			jobPane.add(status, 1, 1);

			jobLabels.put(j, status);

			grid.add(jobPane, 0, level++);
		}

		scroll.setContent(grid);
		primaryStage.setScene(new Scene(scroll));
		primaryStage.show();

	}

	public static void update() {

		for (Job j : jobLabels.keySet()) {
			jobLabels.get(j).setText(j.getStatus());
			jobLabels.get(j).setTextFill(statusColor(j.getStatus()));
		}

	}

	private static Paint statusColor(String status) {
		/*
		 * aCtive, iNactive, cOmpleted, cAnceled
		 */
		switch (status.charAt(1)) {
		case 'C':
			return Color.GREEN;
		case 'N':
			return Color.BLACK;
		case 'O':
			return Color.BLUE;
		case 'A':
			return Color.RED;
		default:
			return Color.GRAY;
		}
	}
}
