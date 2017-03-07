package bootstrap;

import org.apache.log4j.Logger;

import main.job.Input;
import main.pc_gui.GUI;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;
import utils.WarehouseFloor;

public class Start {

	private static Logger log = Logger.getLogger(Start.class);


	public static void main(String[] args) {

		log.info("Starting");
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()), in.getJobsArray());
		GUI.create(model);
	}

}
