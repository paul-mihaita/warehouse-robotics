package comunication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import communication.thread.Converters;
import comunication.CommConst.command;
import comunication.CommConst.protocol;
import lejos.util.Delay;
import movement.Movement.move;
import utils.Location;
import utils.Robot;

public class PCOutputStream {
	private OutputStream stream;
	private Logger log;
	
	public PCOutputStream(OutputStream stream, Logger log) {
		this.stream = stream;
		this.log = log;
	}

	private void write(int b) throws IOException {
		stream.write(b);
		stream.flush();
		Delay.msDelay(CommConst.GRACE);
	}
	private void write(byte[] b) throws IOException {
		stream.write(b);
		stream.flush();
		Delay.msDelay(CommConst.GRACE);
	}
	public void close() throws IOException {
		stream.close();
	}

	public void sendProtocol(protocol p) throws IOException {
		log.debug("Writing a protocol: " + p);
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
				write(CommConst.COM_START);
				break;
			case Wait:
				write(CommConst.COM_WAIT);
				break;
		}
		
	}
}
