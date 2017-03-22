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
import utils.Task;

public class PCInputStream {
	private DataInputStream stream;

	public PCInputStream(DataInputStream stream) {
		this.stream = stream;
	}

	public protocol readProtocol() throws IOException {
		int proto = stream.read();
		System.out.println(proto);
		switch (proto) {
			case CommConst.ROBOT:
				return protocol.Robot;
			case CommConst.MESSAGE:
				return protocol.Message;
			case CommConst.DC:
				return protocol.DC;
			default:
				throw new IOException("Invalid protocol: " + proto);
		}

	}

	private List<move> readMoves() throws IOException {
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

	public Message readMessage() throws IOException {
		List<move> l = readMoves();
		command c = readCommand();
		BasicJob j = readJob();
		return new Message(l,c,j);
	}
	private BasicJob readJob() throws IOException {
		int id = stream.read();
		int quant = stream.read();
		String name = (char) stream.read() + "";
		return new BasicJob(id, new Task(name, quant));
	}

	public protocol getProtocol() throws IOException {
		int proto = stream.read();
		switch (proto) {
			case CommConst.ROBOT:
				return protocol.Robot;
			case CommConst.MESSAGE:
				return protocol.Message;
			default:
				throw new IOException("Invalid protocol recieved: " + proto);

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

	private command readCommand() throws IOException {
		int cmd = stream.read();
		switch (cmd) {
			case CommConst.COM_START:
				return command.Start;
			case CommConst.COM_WAIT:
				return command.Wait;
			case CommConst.COM_FINISH:
				return command.Finish;
			default:
				throw new IOException("Invalid protocol recieved: " + cmd);

		}
	}
}
