package communication.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import communication.BasicJob;
import communication.CommConst;
import movement.Maths;
import movement.Movement.move;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Tuple;

public class Converters {
	private static final int constItems = 4;

	public static Robot byteToRobot(byte[] a) throws IOException {
		if (a.length <= constItems)
			throw new IOException("Robot array was less than 6 in size");
		Location current = new Location(a[0], a[1]);
		boolean isOnPickup = false;
		try {
			isOnPickup = byteToBoolean(a[2]);
		} catch (IOException e) {
			// shouldn't happen
		}
		int movesCompleted = a[3];
		char[] c = new char[a.length - constItems];
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) a[i + constItems];
		}
		String name = new String(c);
		Robot r = new Robot(name, null, null, current);
		r.setMoves(movesCompleted);
		r.setOnPickup(isOnPickup);
		return r;
	}

	public static byte[] robotToByte(Robot r) {
		String name = r.getName();
		byte[] returnArr = new byte[name.length() + constItems];
		Location location = r.getCurrentLocation();
		returnArr[0] = (byte) location.getX();
		returnArr[1] = (byte) location.getY();
		returnArr[2] = booleanToByte(r.isOnPickup());
		returnArr[3] = (byte) r.getMovesCompleted();
		char[] c = name.toCharArray();
		for (int i = 0; i < c.length; i++) {
			returnArr[i + constItems] = (byte) c[i];
		}
		return returnArr;
	}

	public static byte[] movesToByte(List<move> movement, int num) {
		byte[] returnArr = new byte[num];
		int i = 0;
		for (move m : movement) {
			returnArr[i++] = moveToByte(m);
		}
		return returnArr;
	}

	public static byte moveToByte(move m) {
		switch (m) {
			case FORWARD:
				return CommConst.FORWARD;
			case BACKWARD:
				return CommConst.BACKWARD;
			case TURNLEFT:
				return CommConst.TURNLEFT;
			case TURNRIGHT:
				return CommConst.TURNRIGHT;
			case WAIT:
				return CommConst.WAIT;
			default:
				// can't happen
				return 0;
		}
	}

	public static move byteToMove(byte b) throws IOException {
		switch (b) {
			case CommConst.FORWARD:
				return move.FORWARD;
			case CommConst.BACKWARD:
				return move.BACKWARD;
			case CommConst.TURNLEFT:
				return move.TURNLEFT;
			case CommConst.TURNRIGHT:
				return move.TURNRIGHT;
			case CommConst.WAIT:
				return move.WAIT;
			default:
				throw new IOException("Incorrect byte code for movement");
		}
	}

	public static byte booleanToByte(boolean b) {
		if (b) {
			return 1;
		}
		return 0;
	}

	public static boolean byteToBoolean(byte b) throws IOException {
		if (b == 1) {
			return true;
		} else if (b == 0) {
			return false;
		}
		throw new IOException("Incorrect byte code for a boolean");
	}

	public static String robotToString(Robot r) {
		String str = r.getName() + "_" + r.getBtAddress() + "-";
		str += "(" + r.getCurrentLocation().getX() + ":" + r.getCurrentLocation().getY() + ")";
		str += "(" + r.getOrientation().getX() + ":" + r.getOrientation().getY() + ")";
		str += "-" + r.isOnJob() + ":" + r.isOnPickup();
		return str;
	}

	public static BasicJob toBasicJob(Job t, int TaskNum) {
		return new BasicJob(t.getJobID(), t.getTasks().get(TaskNum));
	}

	// not sure if i am using enough generics...
	public static Tuple<HashMap<Robot, ArrayList<ArrayList<move>>>, HashMap<String, Location>> locationToMove(
			HashMap<Robot, ArrayList<ArrayList<Location>>> pathLocations, HashMap<String, Location> initalOrientation) {

		// let it infer types for readability lol
		HashMap<Robot, ArrayList<ArrayList<move>>> returnHash = new HashMap<>();
		HashMap<String, Location> returnOrientation = new HashMap<>();

		for (Robot robot : pathLocations.keySet()) {
			ArrayList<ArrayList<Location>> paths = adjustY(pathLocations.get(robot));
			Tuple<ArrayList<ArrayList<move>>, Location> temp = locationToMove(paths,
					initalOrientation.get(robot.getName()));
			returnHash.put(robot, temp.getX());
			returnOrientation.put(robot.getName(), temp.getY());
		}
		return new Tuple<>(returnHash, returnOrientation);
	}

	private static ArrayList<ArrayList<Location>> adjustY(ArrayList<ArrayList<Location>> arrayList) {
		ArrayList<ArrayList<Location>> returnList = new ArrayList<ArrayList<Location>>();
		for (ArrayList<Location> arrayList2 : arrayList) {
			ArrayList<Location> cur = new ArrayList<Location>();
			returnList.add(cur);
			for (Location location : arrayList2) {
				cur.add(new Location(location.getX(), -1 * location.getY()));
			}
		}
		return arrayList;
	}

	private static Tuple<ArrayList<ArrayList<move>>, Location> locationToMove(ArrayList<ArrayList<Location>> arrayList,
			Location initalOrientation) {
		ArrayList<ArrayList<move>> returnList = new ArrayList<ArrayList<move>>();
		validateVector(initalOrientation);
		Location returnLocation = new Location(initalOrientation.getX(), initalOrientation.getY());
		int j = 0;
		while (j < arrayList.size()) {
			ArrayList<Location> path = arrayList.get(j);
			returnList.add(j, new ArrayList<move>());
			int i = 0;
			while (i < path.size() - 1) {
				Location minusLocation = Maths.minusLocation(path.get(i + 1), path.get(i));
				int angle = Maths.findAngle(returnLocation, minusLocation);
				returnLocation = minusLocation;
				returnList.get(j).add(angleToMove(angle));
				i++;
			}
			j++;
		}
		return new Tuple<>(returnList, returnLocation);
	}

	private static move angleToMove(int angle) {
		switch (angle) {
			case 0:
				return move.FORWARD;
			case 90:
				return move.TURNLEFT;
			case 180:
				return move.BACKWARD;
			case 270:
				return move.TURNRIGHT;
			default:
				throw new IllegalArgumentException("ooops " + angle);
		}
	}

	private static void validateVector(Location initalOrientation) {
		if (initalOrientation.equals(new Location(1, 0)))
			return;
		if (initalOrientation.equals(new Location(-1, 0)))
			return;
		if (initalOrientation.equals(new Location(0, -1)))
			return;
		if (initalOrientation.equals(new Location(0, 1)))
			return;
		throw new IllegalArgumentException(
				"nice vector m8 real fucking funny, ecks dee " + initalOrientation.toString());
	}
}
