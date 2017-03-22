package communication;

import java.util.ArrayList;
import java.util.List;

import communication.CommConst.command;
import movement.Movement.move;

public class Message {
	private List<move> moves;
	private command command;
	private boolean needsUpdate;
	private BasicJob job;

	public Message(List<move> moves, command command, BasicJob job) {
		this.moves = moves;
		this.command = command;
		this.job = job;
		needsUpdate = true;
	}

	public List<move> getMoves() {
		return moves;
	}

	public void setMoves(List<move> moves) {
		this.moves = moves;
		needsUpdate = true;
	}
	public void setMoves(move[] moves) {
		List<move> temp = new ArrayList<move>();
		for (int i = 0; i < moves.length; i++) {
			temp.add(moves[i]);
		}
		setMoves(temp);
	}

	public command getCommand() {
		return command;
	}

	public void setCommand(command message) {
		this.command = message;
		needsUpdate = true;
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public void updated() {
		needsUpdate = false;
	}

	public BasicJob getJob() {
		return job;
	}

	public void setJob(BasicJob job) {
		this.job = job;
		needsUpdate = true;
	}

	public void update(Message readMessage) {
		this.moves = readMessage.getMoves();
		this.command = readMessage.getCommand();
		this.job = readMessage.getJob();
		needsUpdate = true;
	}
	
	@Override
	public String toString() {
		String returnStr = command.toString() + " - ";
		for (move move : moves) {
			returnStr += move.toString() + ",";
		}
		return returnStr;
	}

}
