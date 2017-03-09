package communication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.CommConst.protocol;
import communication.thread.Converters;
import movement.Movement.move;
import utils.Robot;

public class PCOutputStream extends AbstractOutputStream {
	private Logger log;

	public PCOutputStream(OutputStream stream, Logger log) {
		super(stream);
		this.log = log;
	}

	public void sendProtocol(protocol p) throws IOException {
		log.debug("Writing a protocol: " + p);
		switch (p) {
			case Command:
				log.debug("wrote: " + CommConst.COMMAND);
				write(CommConst.COMMAND);
				break;
			case Movement:
				log.debug("wrote: " + CommConst.MOVEMENT);
				write(CommConst.MOVEMENT);
				break;
			case Robot:
				log.debug("wrote: " + CommConst.ROBOT);
				write(CommConst.ROBOT);
				break;
		}
	}

	public void sendRobot(Robot robot) throws IOException {
		byte[] arrayToSend = Converters.robotToByte(robot);
		log.debug("Writing number of elements in array: " + arrayToSend.length);
		write(arrayToSend.length);
		log.debug("Writing robot");
		write(arrayToSend);
	}

	public void sendMoves(List<move> moves) throws IOException {
		int numMoves = moves.size();
		log.debug("Writing number of moves: " + numMoves);
		write(numMoves);
		byte[] moveBytes = Converters.movesToByte(moves, numMoves);
		log.debug("Writing moves");
		write(moveBytes);
	}

	public void sendCommand(command command) throws IOException {
		log.debug("Writing a command: " + command);
		switch (command) {
			case Start:
				log.debug("wrote: " + CommConst.COM_START);
				write(CommConst.COM_START);
				break;
			case Wait:
				log.debug("wrote: " + CommConst.COM_WAIT);
				write(CommConst.COM_WAIT);
				break;
			case Finish:
				log.debug("wrote: " + CommConst.COM_FINISH);
				write(CommConst.COM_FINISH);
				break;
		}
	}
}
