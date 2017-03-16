package main.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import bootstrap.Start;
import main.gui.GUI;
import main.model.WarehouseFloor;
import main.route.Planning;
import rp.robotics.mapping.MapUtils;
import utils.Robot;

public class JobWorthTest {

	public static final Logger log = Logger.getLogger(Start.class);
	
	public static void main(String[] args){

		Input in = new Input(false);
		//in.initializeListOfJobs("1", "2", "3");
		
		WarehouseFloor model = new WarehouseFloor(Planning.createGraph(MapUtils.createRealWarehouse()),
				in.getJobsArray(), null, log, false);

		
		
		//HashMap<String, String> jobsReward = jobWorth.getReward(); 
		
		//GUI.create(model);
		
	}
	
	

}
