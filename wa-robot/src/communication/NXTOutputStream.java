package communication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import communication.thread.Converters;
import comunication.AbstractOutputStream;
import comunication.CommConst;
import comunication.CommConst.command;
import comunication.CommConst.protocol;
import movement.Movement.move;
import utils.Robot;

public class NXTOutputStream extends AbstractOutputStream {

	public NXTOutputStream(OutputStream stream) {
		super(stream);
	}
	public void sendProtocol(protocol p) throws IOException {
		switch (p) {
			case Command:
				write(CommConst.COMMAND);
				break;
			case Movement:
				write(CommConst.MOVEMENT);
				break;
			case Robot:
				write(CommConst.ROBOT);
				break;
		}
	}
	public void sendRobot(Robot robot) throws IOException {
		byte[] arrayToSend = Converters.robotToByte(robot);
		write(arrayToSend.length);
		write(arrayToSend);
	}
	public void sendMoves(List<move> moves) throws IOException {
		int numMoves = moves.size();
		write(numMoves);
		byte[] moveBytes = Converters.movesToByte(moves, numMoves);
		write(moveBytes);
	}
	public void sendCommand(command command) throws IOException {
		switch (command) {
			case Start:
				write(CommConst.COM_START);
				break;
			case Wait:
				write(CommConst.COM_WAIT);
				break;
		}
	}
}
