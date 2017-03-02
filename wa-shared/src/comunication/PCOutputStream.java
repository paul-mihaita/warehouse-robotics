package comunication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;

import lejos.util.Delay;
import movement.Movement.move;

public class PCOutputStream {
	private OutputStream stream;
	private Logger log;
	
	public PCOutputStream(OutputStream stream, Logger log) {
		this.stream = stream;
		this.log = log;
	}
	
	public void setMovement(List<move> movement) throws IOException {
		writeMoves(movement);
	}

	private void writeMoves(List<move> movement) throws IOException {
		log.debug("Writing protocol: MOVEMENT");
		write(CommConst.MOVEMENT);
		int numMoves = movement.size();
		log.debug("Writing number of moves: " + numMoves);
		write(numMoves);
		byte[] moveBytes = convertMoves(movement, numMoves);
		log.debug("Writing moves");
		write(moveBytes);
	}
	
	private byte[] convertMoves(List<move> movement,int num) {
		byte[] returnArr = new byte[num];
		int i = 0;
		for (move m: movement) {
			returnArr[i++] = makeByte(m);
		}
		return returnArr;
	}

	private byte makeByte(move m) {
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
				//can't happen
				return 0;
		}
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

	public void startMsg() throws IOException {
		log.debug("Writing protocol: START");
		write(CommConst.START);
	}
}
