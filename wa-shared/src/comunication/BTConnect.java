package comunication;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;
import movement.Movement.move;

public class BTConnect {
	private NXTComm comm;
	private PCOutputStream toNXT;
	private Logger log;

	public BTConnect(Logger log) throws NXTCommException {
		this.log = log;
		log.debug("BTConnect object created");
		log.info("Creating Comm object");
		try {
			this.comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			log.info("Connecting");
			comm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, "Keith", "0016530FDDAE"));
			this.toNXT = new PCOutputStream(comm.getOutputStream(), log);
		} catch (NXTCommException e) {
			log.error(e);
			throw e;
		}
	}

	public void sendMoves(List<move> moves) throws IOException {
		log.info("Sending moves");
		try {
			toNXT.setMovement(moves);
		} catch (IOException e) {
			log.error(e);
			throw e;
		}
	}
	
	public void close() throws IOException {
		log.info("closing connection");
		try {
			toNXT.close();
			comm.close();
		} catch (IOException e) {
			log.error(e);
			throw e;
		}
	}

	public void start() throws IOException {
		log.info("sending start message");
		toNXT.startMsg();
	}
}
