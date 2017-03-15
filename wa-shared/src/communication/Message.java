package communication;

import java.util.ArrayList;
import java.util.List;

import communication.CommConst.command;
import movement.Movement.move;

public class Message {
	private List<move> moves;
	private command command;
	private boolean needsUpdate;
	private basicJob job;

	public Message(List<move> moves, command command, basicJob job) {
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

	public basicJob getJob() {
		return job;
	}

	public void setJob(basicJob job) {
		this.job = job;
		needsUpdate = true;
	}

}
