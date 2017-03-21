package main.gui;

import javafx.scene.canvas.GraphicsContext;
import main.model.WarehouseFloor;

public class PathAnimator extends Thread {
	
	private WarehouseFloor model;
	private GraphicsContext gc;

	public PathAnimator(WarehouseFloor model, GraphicsContext gc) {
		super();

		this.model = model;
		this.gc = gc;
	}

	@Override
	public void run() {
		this.setName("Node animator");

		while (!this.isInterrupted()) {
		}
	}
}
