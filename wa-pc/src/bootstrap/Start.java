package bootstrap;

import org.apache.log4j.Logger;

import communication.thread.Server;
import main.job.Input;
import main.pc_gui.GUI;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;
import utils.Robot;
import utils.WarehouseFloor;

public class Start {

	public static final Logger log = Logger.getLogger(Start.class);


	public static void main(String[] args) {

		log.info("Starting");
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()), in.getJobsArray());
		Robot[] robots = model.getRobots().toArray(new Robot[0]);
		
		//Server server = new Server(robots, msgs, log);
		GUI.create(model);
	}

}
