package communication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import communication.CommConst.command;
import communication.CommConst.protocol;
import communication.thread.Converters;
import movement.Movement.move;
import utils.Robot;
import utils.Task;

public class NXTInputStream {
	private InputStream stream;
	private Logger log;

	public NXTInputStream(InputStream inputStream, Logger log) {
		this.stream = inputStream;
		this.log = log;
	}

	public protocol readProtocol() throws IOException {
		int proto = stream.read();
		log.debug("proto: " + proto);
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

	public Robot readRobot() throws IOException {
		int size = stream.read();
		log.debug("robot array size: " + size);
		byte[] robotArr = new byte[size];
		int actualNum = stream.read(robotArr);
		if (actualNum != size) {
			throw new IOException("Robot byte array size mismatch: " + size + " != ");
		}
		return Converters.byteToRobot(robotArr);
	}
	public Message readMessage() throws IOException {
		List<move> l = readMoves();
		command c = readCommand();
		BasicJob j = readJob();
		return new Message(l, c, j);
	}

	private BasicJob readJob() throws IOException {
		int id = stream.read();
		log.debug("read id: " + id);
		int quant = stream.read();
		log.debug("read quant: " + quant);
		String name = (char) stream.read() + "";
		log.debug("read item name: " + name);
		return new BasicJob(id, new Task(name, quant));
	}

	private List<move> readMoves() throws IOException {
		int numMoves = stream.read();
		log.debug("Number of moves: " + numMoves);
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


	private command readCommand() throws IOException {
		int cmd = stream.read();
		switch (cmd) {
			case CommConst.COM_START:
				log.debug("read command: " + command.Start);
				return command.Start;
			case CommConst.COM_WAIT:
				log.debug("read command: " + command.Wait);
				return command.Wait;
			case CommConst.COM_FINISH:
				log.debug("read command: " + command.Finish);
				return command.Finish;
			default:
				throw new IOException("Invalid protocol recieved: " + cmd);

		}
	}
}
