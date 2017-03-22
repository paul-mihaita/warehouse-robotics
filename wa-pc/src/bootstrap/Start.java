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
		boolean server;
		if (args.length == 0)
			server = false;
		else
			server = true;
		log.info("Starting");

		Input in = new Input(false);
		

		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()),
				in.getJobsArray(), in.getItemsArray(), in.getIDropLocationArray(), server);
		// Testing

		log.debug("Number of Jobs: " + model.getJobs().size());

		GUI.create(model);
	}

}
