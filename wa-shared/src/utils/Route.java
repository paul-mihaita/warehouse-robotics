package utils;

import java.util.ArrayList;
import java.util.List;

import movement.Movement.move;

public class Route {
	private List<move> path = new ArrayList<move>();

	public List<move> getPath() {
		return path;
	}

	public void setPath(List<move> path) {
		this.path = path;
	}
}
