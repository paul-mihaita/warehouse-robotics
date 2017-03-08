package route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import main.route.Planning;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import student_solution.Graph;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;
import movement.Movement.move;
public class CommandCenter {
	private static GridMap gridMap = MapUtils.createRealWarehouse();
	private static Graph<Location> graph = Planning.createGraph(gridMap);

	public static HashMap<Robot, ArrayList<ArrayList<move>>> generatePaths(
			
			HashMap<Robot, Job> jobMap) {
		ArrayList<Robot> robots = new ArrayList<>();
		Iterator<Robot> rob = jobMap.keySet().iterator();
		HashMap<Robot, ArrayList<ArrayList<move>>> paths = new HashMap<>();
		while (rob.hasNext()) {
			ArrayList<move> individualPath = new ArrayList<>();
			ArrayList<ArrayList<move>> pathsForRobot = new ArrayList<>();
			Robot robot = rob.next();
			robots.add(robot);
			Job curJob = jobMap.get(robot);
			ArrayList<Task> tasks = curJob.getTasks();

			orderTasks(tasks);

			for (Task task : tasks) {
				individualPath = new ArrayList<>();
				Location start = robot.getCurrentLocation();
				Location finish = task.getItem().getLocation();
				individualPath = generateMovements(Astar.aStar(graph,
						start, finish, Planning.manhatanHeuristic, 100, false,
						gridMap), robot.getOrientation());
				pathsForRobot.add(individualPath);
			}
			paths.put(robot, pathsForRobot);
		}
		return paths;

	}

	public static ArrayList<move> generateMovements(
			ArrayList<Location> path, Location orientation) {
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

	public static ArrayList<move> whatDirection(Location loc, Location target,
			move facing) {
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
				aux.add( move.TURNLEFT);
				aux.add( move.BACKWARD);
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
