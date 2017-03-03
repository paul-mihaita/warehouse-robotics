package pc_gui;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

	public static final int WIDTH = 400;
	public static final int HEIGHT = 600;

	public static void main(String[] args) {
		launch(args);
	}

	public GUI() {
		super();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Warehouse Controller");
		ScrollPane scroll = new ScrollPane();
		scroll.setMinSize(WIDTH, HEIGHT);
		scroll.setMaxSize(WIDTH, HEIGHT);
		GridPane grid = new GridPane();

		grid.setAlignment(Pos.CENTER);
		grid.setHgap(WIDTH / 100);
		grid.setVgap(WIDTH / 100);

		for (int i = 0; i < 10; i++) {
			GridPane g = new GridPane();
			Label l = new Label("Job ID: " + i);
			Button b = new Button("Cancel");

			g.setAlignment(Pos.CENTER);
			g.setHgap(WIDTH / 100);
			g.setVgap(WIDTH / 100);
			
			g.add(l, 0, 0);
			g.add(b, 1, 0);
			
			grid.add(g, 0, i);
		}

		scroll.setContent(grid);
		primaryStage.setScene(new Scene(scroll));
		primaryStage.show();

	}

}
