package communication.thread;

import java.io.IOException;
import java.util.List;

import comunication.CommConst;
import movement.Movement.move;
import utils.Location;
import utils.Robot;

public class Converters {
	public static Robot byteToRobot(byte[] a) {
		Location current = new Location(a[0], a[1]);
		Location orientation = new Location(a[2], a[3]);
		char[] c = new char[a.length - 4];
		for (int i = 0; i < c.length; i++) {
			c[i] = (char) a[i + 4];
		}
		String name = new String(c);
		return new Robot(name, name, orientation, current); // bit of a hack
	}

	public static byte[] robotToByte(Robot r) {
		String name = r.getName();
		byte[] returnArr = new byte[name.length() + 4];
		Location location = r.getCurrentLocation();
		returnArr[0] = (byte) location.getX();
		returnArr[1] = (byte) location.getY();
		location = r.getOrientation();
		returnArr[2] = (byte) location.getX();
		returnArr[3] = (byte) location.getX();
		char[] c = name.toCharArray();
		for (int i = 0; i < c.length; i++) {
			returnArr[i + 4] = (byte) c[i];
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
}
