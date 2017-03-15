package communication;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import communication.CommConst.command;
import communication.CommConst.protocol;
import communication.thread.Converters;
import movement.Movement.move;
import utils.Robot;

public class NXTOutputStream extends AbstractOutputStream {

	public NXTOutputStream(OutputStream stream) {
		super(stream);
	}

	public void sendProtocol(protocol p) throws IOException {
		switch (p) {
			case Robot:
				write(CommConst.ROBOT);
				break;
			case Message:
				write(CommConst.MESSAGE);
				break;
			case DC:
				// writing a DC message
				IOException e = new IOException("tried to write a DC protocol");
				throw e;
		}
	}

	public void sendRobot(Robot robot) throws IOException {
		sendProtocol(protocol.Robot);
		byte[] arrayToSend = Converters.robotToByte(robot);
		write(arrayToSend.length);
		write(arrayToSend);
	}

	public void sendMessage(Message msg) throws IOException {
		sendProtocol(protocol.Message);
		sendMoves(msg.getMoves());
		sendCommand(msg.getCommand());
		sendJob(msg.getJob());
	}

	private void sendJob(basicJob job) throws IOException {
		write(job.getId());
		write(job.getTask().getQuantity());
		write((byte) job.getTask().getItem().getName().charAt(0));

	}

	private void sendMoves(List<move> moves) throws IOException {
		int numMoves = moves.size();
		write(numMoves);
		byte[] moveBytes = Converters.movesToByte(moves, numMoves);
		write(moveBytes);
	}

	private void sendCommand(command command) throws IOException {
		switch (command) {
			case Start:
				write(CommConst.COM_START);
				break;
			case Wait:
				write(CommConst.COM_WAIT);
				break;
			case Finish:
				write(CommConst.COM_FINISH);
				break;
		}
	}
}
