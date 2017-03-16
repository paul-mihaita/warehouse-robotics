package main.route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;
import lejos.robotics.navigation.Move;
import movement.Movement.move;

public class CommandCenter {
	private static GridMap gridMap = MapUtils.createRealWarehouse();
	private static Graph<Location> graph = Planning.createGraph(gridMap);
	private static HashMap<Robot, ArrayList<ArrayList<Location>>> pathsLocations = new HashMap<>();

	/*
	 * public static HashMap<Robot, ArrayList<ArrayList<move>>> generatePaths(
	 * 
	 * HashMap<Robot, Job> jobMap) { ArrayList<Robot> robots = new
	 * ArrayList<>(); Iterator<Robot> rob = jobMap.keySet().iterator();
	 * HashMap<Robot, ArrayList<ArrayList<move>>> paths = new HashMap<>(); while
	 * (rob.hasNext()) { ArrayList<move> individualPath = new ArrayList<>();
	 * ArrayList<ArrayList<move>> pathsForRobot = new ArrayList<>(); Robot robot
	 * = rob.next(); robots.add(robot); Job curJob = jobMap.get(robot);
	 * ArrayList<Task> tasks = curJob.getTasks();
	 * 
	 * orderTasks(tasks);
	 * 
	 * for (Task task : tasks) { individualPath = new ArrayList<>(); Location
	 * start = robot.getCurrentLocation(); Location finish =
	 * task.getItem().getLocation(); individualPath = generateMovements(
	 * Astar.aStar(graph, start, finish, Planning.manhatanHeuristic, 100, false,
	 * gridMap), robot.getOrientation()); pathsForRobot.add(individualPath);
	 * //set the robot location to the next items location
	 * 
	 * } paths.put(robot, pathsForRobot); } return paths;
	 * 
	 * }
	 */

	public static HashMap<Robot, ArrayList<ArrayList<move>>> generatePaths(HashMap<Robot, Job> jobMap) {
		pathsLocations.clear();
		ArrayList<Robot> robots = new ArrayList<>();
		Iterator<Robot> rob = jobMap.keySet().iterator();
		HashMap<Robot, ArrayList<ArrayList<move>>> paths = new HashMap<>();
		HashMap<Robot, ArrayList<Location>> finishLoc = new HashMap<>();
		int item = 0;
		int maxItem = -9999;
		while (rob.hasNext()) {
			item = 0;
			Robot robot = rob.next();
			Job curJob = jobMap.get(robot);
			ArrayList<Task> tasks = curJob.getTasks();
			finishLoc.put(robot, new ArrayList<Location>());
			orderTasks(tasks);
			for (Task task : tasks) {
				Location finish = task.getItem().getLocation();
				finishLoc.get(robot).add(finish);
				item++;
			}
			if (maxItem < item) {
				maxItem = item;
			}
			robots.add(robot);
		}
		Hashtable<Robot, Location> startOrientations = new Hashtable<Robot, Location>();
		Hashtable<Robot, Location> finalOrientation = new Hashtable<Robot, Location>();
		Hashtable<Robot, Location> startLocations = new Hashtable<Robot, Location>();

		for (Robot r : robots) {
			startOrientations.put(r, r.getOrientation());
			finalOrientation.put(r, r.getOrientation());
			startLocations.put(r, r.getCurrentLocation());
		}
		for (int i = 0; i < maxItem; i++) {
			Hashtable<Robot, Location> targets = new Hashtable<Robot, Location>();
			for (Robot r : robots) {
				ArrayList<Location> aux = finishLoc.get(r);
				if (aux.size() > i) {
					targets.put(r, aux.get(i));
					r.setOrientation(finalOrientation.get(r));
				} else {
					targets.put(r, r.getCurrentLocation());
					r.setOrientation(finalOrientation.get(r));
				}

			}
			ArrayList<ArrayList<Location>> x = AstarWHCA.aStar(graph, gridMap, robots, targets, 100);
			Hashtable<Robot, Location> finalLoc = new Hashtable<Robot, Location>();
			finalOrientation = new Hashtable<Robot, Location>();

			if (i == 0) {
				int j = 0;
				for (ArrayList<Location> y : x) {
					if (y.size() > 2) {
						ArrayList<ArrayList<move>> aux = new ArrayList<>();
						ArrayList<ArrayList<Location>> auxLoc = new ArrayList<>();
						Robot r = robots.get(j);
						aux.add(CommandCenter.generateMovements(y, r.getOrientation()));
						finalLoc.put(r, y.get(y.size() - 1));
						finalOrientation.put(r, getOrientation(y.get(y.size() - 2), y.get(y.size() - 1)));
						paths.put(r, aux);
						auxLoc.add(y);
						pathsLocations.put(r, auxLoc);
						j++;
					} else
						j++;
				}
			} else {
				int j = 0;
				for (ArrayList<Location> y : x) {
					if (y.size() > 2) {
						Robot r = robots.get(j);
						finalLoc.put(r, y.get(y.size() - 1));
						finalOrientation.put(r, getOrientation(y.get(y.size() - 2), y.get(y.size() - 1)));
						pathsLocations.get(r).add(y);
						j++;
					} else
						j++;
				}
			}
			for (Robot r : robots) {
				if (finalLoc.containsKey(r)) {
					r.setCurrentLocation(finalLoc.get(r));
					r.setOrientation(finalOrientation.get(r));
				}
			}
		}
		int k = 0;
		for (Robot r : robots) {
			r.setCurrentLocation(startLocations.get(k));
			r.setOrientation(startOrientations.get(k));
			k++;
		}
		return paths;

	}

	public static HashMap<Robot, ArrayList<ArrayList<Location>>> getPathLocations() {
		return pathsLocations;
	}

	private static Location getOrientation(Location location, Location location2) {
		if (location.getX() < location2.getX()) {
			return new Location(location2.getX() + 1, location2.getY());

		} else if (location.getX() > location2.getX()) {
			return new Location(location2.getX() - 1, location2.getY());
		} else if (location.getY() < location2.getY()) {
			return new Location(location2.getX(), location2.getY() + 1);

		} else if (location.getY() > location2.getY()) {
			return new Location(location2.getX(), location2.getY() - 1);

		} else
			return location2;
	}

	public static ArrayList<move> generateMovements(ArrayList<Location> path, Location orientation) {
		ArrayList<move> moves = new ArrayList<>();
		move facing = getFacing(path.get(0), orientation);

		for (int i = 1; i < path.size(); i++) {
			Location loc = path.get(i - 1);
			Location target = path.get(i);
			ArrayList<move> aux = whatDirection(loc, target, facing);
			facing = aux.get(0);
			moves.add(aux.get(1));
		}
		return moves;
	}

	public static ArrayList<move> whatDirection(Location loc, Location target, move facing) {
		ArrayList<move> aux = new ArrayList<>();
		int lox = loc.getX();
		int loy = loc.getY();
		int tgx = target.getX();
		int tgy = target.getY();
		if (tgx > lox) {
			if (facing == move.FORWARD) {
				aux.add(move.TURNRIGHT);
				aux.add(move.TURNRIGHT);
			} else if (facing == move.BACKWARD) {
				aux.add(move.TURNRIGHT);
				aux.add(move.TURNLEFT);
			} else if (facing == move.TURNLEFT) {
				aux.add(move.TURNRIGHT);
				aux.add(move.BACKWARD);
			} else if (facing == move.TURNRIGHT) {
				aux.add(move.TURNRIGHT);
				aux.add(move.FORWARD);
			}
			return aux;
		}
		if (tgx < lox) {
			if (facing == move.FORWARD) {
				aux.add(move.TURNLEFT);
				aux.add(move.TURNLEFT);
			} else if (facing == move.BACKWARD) {
				aux.add(move.TURNLEFT);
				aux.add(move.TURNRIGHT);
			} else if (facing == move.TURNLEFT) {
				aux.add(move.TURNLEFT);
				aux.add(move.FORWARD);
			} else if (facing == move.TURNRIGHT) {
				aux.add(move.TURNLEFT);
				aux.add(move.BACKWARD);
			}
			return aux;
		}
		if (tgy > loy) {
			if (facing == move.FORWARD) {
				aux.add(move.FORWARD);
				aux.add(move.FORWARD);
			} else if (facing == move.BACKWARD) {
				aux.add(move.FORWARD);
				aux.add(move.BACKWARD);
			} else if (facing == move.TURNLEFT) {
				aux.add(move.FORWARD);
				aux.add(move.TURNRIGHT);
			} else if (facing == move.TURNRIGHT) {
				aux.add(move.FORWARD);
				aux.add(move.TURNLEFT);
			}
			return aux;
		}
		if (tgy < loy) {
			if (facing == move.FORWARD) {
				aux.add(move.BACKWARD);
				aux.add(move.BACKWARD);
			} else if (facing == move.BACKWARD) {
				aux.add(move.BACKWARD);
				aux.add(move.FORWARD);
			} else if (facing == move.TURNLEFT) {
				aux.add(move.BACKWARD);
				aux.add(move.TURNLEFT);
			} else if (facing == move.TURNRIGHT) {
				aux.add(move.BACKWARD);
				aux.add(move.TURNRIGHT);
			}
			return aux;
		}
		aux.add(facing);
		aux.add(move.WAIT);
		return aux;
	}

	public static move getFacing(Location loc, Location orientation) {

		if (orientation.getX() > loc.getX())
			return move.TURNRIGHT;
		else if (orientation.getX() < loc.getX())
			return move.TURNLEFT;
		else if (orientation.getY() > loc.getY())
			return move.FORWARD;
		else if (orientation.getY() < loc.getY())
			return move.BACKWARD;
		return move.WAIT;
	}

	public static void orderTasks(ArrayList<Task> tasks) {
		// ordering tasks, TODO
	}

}
