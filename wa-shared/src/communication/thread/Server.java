package communication.thread;

public class Server {

	private void initConnection() throws NXTCommException {
		this.comm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		log.info("Connecting");
		comm.open(new NXTInfo(NXTCommFactory.BLUETOOTH, robot.getName(), robot.getBtAddress()));
		this.toNXT = new PCOutputStream(comm.getOutputStream(), log);
	}

}
