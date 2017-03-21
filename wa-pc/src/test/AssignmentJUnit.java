package test;

import org.junit.Test;
import org.junit.Before;
import main.job.Selection;

import java.util.ArrayList;
import java.util.HashSet;

import utils.Job;
import utils.Robot;

public class AssignmentJUnit{

	private ArrayList<Job> actualset;
	private ArrayList<Job> trainingset;
	private Selection testSelector;
	private boolean checking;
	
	@Before
	public void setup() throws Exception{
		
		String filepath="C:\\Users\\narae\\OneDrive\\Documents\\RobotProgramming_files";
		
		String[] files=new String[7];
		files[0]=filepath+"\\warehouse\\cancellations.csv";
		files[1]=filepath+"\\warehouse\\drops.csv";
		files[2]=filepath+"\\warehouse\\items.csv";
		files[3]=filepath+"\\warehouse\\jobs.csv";
		files[4]=filepath+"\\warehouse\\locations.csv";
		files[5]=filepath+"\\warehouse\\training_jobs.csv";
		files[6]=filepath+"\\warehouse\\marking_file.csv";
	}
	
	@Test
	public void test() throws InterruptedException{
		
		HashSet<Robot> robots=new HashSet<Robot>();
		
	}
	
}
