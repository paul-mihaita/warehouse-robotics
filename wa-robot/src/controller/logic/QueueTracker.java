package controller.logic;

import java.util.List;

import communication.CommConst.command;
import communication.Message;
import movement.Movement.move;
import robot_gui.GUI;

public class QueueTracker {

	private Message msg;
	private List<move> internal;
	private int pointer = 0;

	public QueueTracker(Message msg) {
		this.msg = msg;
		this.internal = null; // no internal queue yet, let the user pull first
	}

	/**
	 * This method will refresh the internal queue with the msg object i.e.
	 * pulls new moves from the server if there are new moves to get
	 */
	public void pull() {
		List<move> updated = msg.getMoves();
		if (compMoves(internal, updated)) {
			// no need to pull;
			return;
		}
		internal = updated; // pull moves
		//printInternal();
		pointer = 0;
	}

	private void printInternal() {
		for (move move : internal) {
			System.out.print(move);
		}
		System.out.println();
	}

	/**
	 * This method returns if the queue has reached the end, it has side
	 * effects if the result was true:
	 * 
	 * true ->
	 * It will reset the queue so that it is ready for new moves and the msg
	 * object has its command set to Finish.
	 * 
	 * @return whether the queue has reached the end
	 */
	public boolean finished() {
		if (pointer == internal.size()) {
			
			msg.setCommand(command.Wait);

			
			
			
			pointer = 0;
			internal = null;
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return the next move to be executed
	 */
	public move getNextMove() {
		return internal.get(pointer++);
	}

	/**
	 * compares to list of moves for equality
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean compMoves(List<move> a, List<move> b) {
		if (a == null || b == null)
			return false;
		if (a.size() != b.size())
			return false;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i) != b.get(i))
				return false;
		}
		return true;
	}
}
