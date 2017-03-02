package communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comunication.CommConst;
import lejos.robotics.localization.PoseProvider;
import lejos.util.Delay;
import movement.Movement.move;

public class PCInputStream {
	private DataInputStream stream;
	private List<move> moves;
	public PCInputStream(DataInputStream stream) {
		this.stream = stream;
		moves = new ArrayList<move>();
	}
	
	public List<move> getMovement() throws IOException {
		communicate();
		return moves;
	}

	public int getNextInstruction() throws IOException {
		return communicate();
	}
	
	private int communicate() throws IOException {
		int protocol = stream.read();
		switch (protocol) {
			case CommConst.MOVEMENT:
				readMoves();
				return protocol;
			case CommConst.START:
				return protocol;
			default:
				throw new IOException("Invalid protocol: " + protocol);
		}
		
	}
	
	private void readMoves() throws IOException {
		int numMoves = stream.read();
		byte[] moveBytes = new byte[numMoves];
		int actualNum = stream.read(moveBytes);
		if (actualNum != numMoves) {
			throw new IOException("Number of moves mismatch: " + numMoves + " != " + actualNum);
		}
		moves.clear();
		for (int i = 0; i < moveBytes.length; i++) {
			moves.add(makeMove(moveBytes[i]));
		}
		
	}
	private move makeMove(byte b) throws IOException {
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

	public void close() throws IOException {
		stream.close();
	}
}
