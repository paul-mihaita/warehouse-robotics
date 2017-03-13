package communication;

public class CommConst {

	public static final int DC = -1;
	public static final int MOVEMENT = 1;
	public static final int ROBOT = 2;
	public static final int COMMAND = 3;

	public static final int COM_START = 10;
	public static final int COM_WAIT = 20;
	public static final int COM_FINISH = 30;

	public static final int GRACE = 100;

	public static final byte FORWARD = 0x01;
	public static final byte BACKWARD = 0x02;
	public static final byte TURNLEFT = 0x03;
	public static final byte TURNRIGHT = 0x04;
	public static final byte WAIT = 0x05;

	public enum protocol {
		Movement, Robot, Command, DC
	}

	public enum command {
		Start, Wait, Finish
	}
}
