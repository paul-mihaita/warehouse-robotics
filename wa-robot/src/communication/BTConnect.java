package communication;

import java.io.IOException;
import java.util.List;

import comunication.CommConst;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import movement.Movement.move;

public class BTConnect {
	private PCInputStream fromPC;
	private BTConnection connection;
	public BTConnect() {
		this.connection = Bluetooth.waitForConnection();
		this.fromPC = new PCInputStream(connection.openDataInputStream());
	}
	//When using; the corresponding PC must have called sendMoves()
	public List<move> getMoves() throws IOException {
		return fromPC.getMovement();
	}
	public void close() throws IOException {
		fromPC.close();
		connection.close();
	}
	public boolean shouldRun() throws IOException {
		return (fromPC.getNextInstruction() == CommConst.START);
	}
}
