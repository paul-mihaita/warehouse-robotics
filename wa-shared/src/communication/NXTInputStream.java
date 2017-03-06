package communication;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import communication.CommConst.command;
import communication.CommConst.protocol;
import communication.thread.Converters;
import movement.Movement.move;
import utils.Robot;

public class NXTInputStream {
	private DataInputStream stream;

	public NXTInputStream(DataInputStream stream) {
		this.stream = stream;
	}
	
	public protocol readProtocol() throws IOException {
		int proto = stream.read();
		switch (proto) {
			case CommConst.MOVEMENT:
				return protocol.Movement;
			case CommConst.ROBOT:
				return protocol.Robot;
			case CommConst.COMMAND:
				return protocol.Command;
			default:
				throw new IOException("Invalid protocol: " + proto);
		}
	}
	public List<move> readMoves() throws IOException {
		int numMoves = stream.read();
		byte[] moveBytes = new byte[numMoves];
		int actualNum = stream.read(moveBytes);
		if (actualNum != numMoves) {
			throw new IOException("Number of moves mismatch: " + numMoves + " != " + actualNum);
		}
		List<move> moves = new ArrayList<move>();
		for (int i = 0; i < moveBytes.length; i++) {
			moves.add(Converters.byteToMove(moveBytes[i]));
		}
		return moves;
	}
	public void close() throws IOException {
		stream.close();
	}

	public protocol getProtocol() throws IOException {
		int proto = stream.read();
		switch (proto) {
			case CommConst.MOVEMENT:
				return protocol.Command;
			case CommConst.ROBOT:
				return protocol.Robot;
			case CommConst.COMMAND:
				return protocol.Command;
			default:
				throw new IOException("Invalid protocol recieved");

		}
	}
	public Robot readRobot() throws IOException {
		int size = stream.read();
		byte[] robotArr = new byte[size];
		int actualNum = stream.read(robotArr);
		if (actualNum != size) {
			throw new IOException("Robot byte array size mismatch: " + size + " != ");
		}
		return Converters.byteToRobot(robotArr);
	}

	public command readCommand() throws IOException {
		int cmd = stream.read();
		switch (cmd) {
			case CommConst.COM_START:
				return command.Start;
			case CommConst.COM_WAIT:
				return command.Wait;
			default:
				throw new IOException("Invalid protocol recieved");

		}
	}
}
