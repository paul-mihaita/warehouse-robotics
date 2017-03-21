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

		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()),
				in.getJobsArray(), in.getItemsArray(), log, true);
		// Testing

		log.debug("Number of Jobs: " + model.getJobs().size());

		GUI.create(model);
	}

}
