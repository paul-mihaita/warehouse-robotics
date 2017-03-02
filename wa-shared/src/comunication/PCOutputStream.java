package comunication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import movement.Movement.move;

public class PCOutputStream {
	private OutputStream stream;
	
	public PCOutputStream(OutputStream stream) {
		this.stream = stream;
	}
	
	public void setMovement(List<move> movement) throws IOException {
		writeMoves(movement);
	}

	private void writeMoves(List<move> movement) throws IOException {
		stream.write(CommConst.MOVEMENT);
		stream.flush();
		int numMoves = movement.size();
		stream.write(numMoves);
		stream.flush();
		byte[] moveBytes = convertMoves(movement, numMoves);
		stream.write(moveBytes);
		stream.flush();
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
}
