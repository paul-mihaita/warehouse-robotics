package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import lejos.util.Delay;
import main.gui.GUI;
import main.job.Input;
import main.model.WarehouseFloor;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;
import utils.Job;

public class GuiTest {

	public static final Logger log = Logger.getLogger(GuiTest.class);

	private static WarehouseFloor model;

	@BeforeClass
	public static void setUp() {

		Input in = new Input(false);

		model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()), in.getJobsArray(),
				in.getItemsArray(), in.getIDropLocationArray(), false);
		// Testing

		log.debug("Number of Jobs: " + model.getJobs().size());

		new Thread() {
			public void run() {
				GUI.create(model);
			};
		}.start();

		Delay.msDelay(10000);

	}

	@Test
	public void GUIStart() {
		new Thread() {
			public void run() {
				try {
					GUI.create(model);
					fail("GUI failed to start");
				} catch (Exception e) {
					log.debug("GUI has already started, hence it didn't previously crash");
				}
			};
		}.start();

	}

	@Test
	public void jobs() {

		Delay.msDelay(3000);

		boolean canceled = false;

		for (Job j : model.getJobs().values()) {
			if (j.isCanceled()) {
				canceled = true;
			}
		}

		assertTrue("No jobs were canceled", canceled);
	}

}
