package bootstrap;

import org.apache.log4j.Logger;

import main.gui.GUI;
import main.job.Input;
import main.model.WarehouseFloor;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;

public class Start {

	public static final Logger log = Logger.getLogger(Start.class);

	public static void main(String[] args) {

		log.info("Starting");
		Input in = new Input(false);
		in.initializeListOfJobs("1", "2", "3");
		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()),
				in.getJobsArray(), log);
		GUI.create(model);
	}

}
