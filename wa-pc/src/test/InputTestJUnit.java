package test;

import static org.junit.Assert.*;
import main.job.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import utils.Item;
import utils.Job;

public class InputTestJUnit {

	Input inputTest = new Input(false);

	/*
	 * @Before public void private void Setup() {
	 * 
	 * 
	 * }
	 */

	// fileHaveTheExtensione()
	@Test
	public void testFileHaveTheExtension1() {
		String file = "first";
		String file2 = "first.csv";
		assertTrue(inputTest.fileHaveTheExtension(file) != inputTest.fileHaveTheExtension(file2));
	}

	@Test
	public void testFileHaveTheExtension2() {
		String file = "first.csv";
		String file2 = "first.csv";
		assertTrue(inputTest.fileHaveTheExtension(file) == inputTest.fileHaveTheExtension(file2));
	}

	// fileRight()
	@Test
	public void testFileRight1() {
		String file = "first";
		assertTrue(inputTest.fileRight(file) == false);
	}

	@Test
	public void testFileRight2() {
		String file = "1.csv";
		assertTrue(inputTest.fileRight(file) == true);
	}

	// readTaskAndJobs()
	@Test
	public void testReadTaskAndJobs1() {
		String file = "1.csv";
		assertTrue(inputTest.readTaskAndJobs(file, false, false) == true);
	}

	@Test 
	public void testReadTaskAndJobs2() {
		String file = "11.csv";
		assertTrue(inputTest.readTaskAndJobs(file, false ,false) == false);
	}

	// readItemAndRewardAndWeight()
	@Test
	public void testReadItemAndRewardAndWeight1() {
		String file = "2.csv";
		assertTrue(inputTest.readItemAndRewardAndWeight(file) == true);
	}

	@Test
	public void testReadItemAndRewardAndWeight2() {
		String file = "22.csv";
		assertTrue(inputTest.readItemAndRewardAndWeight(file) == false);
	}

	// readItemAndXPositionAndYPosition()
	@Test
	public void testReadItemAndXPositionAndYPosition1() {
		String file = "3.csv";
		assertTrue(inputTest.readItemAndXPositionAndYPosition(file) == true);
	}

	@Test
	public void testReadItemAndXPositionAndYPosition2() {
		String file = "32.csv";
		assertTrue(inputTest.readItemAndXPositionAndYPosition(file) == false);
	}

	// initializeListOfJobs()
	@Test
	public void testInitializeListOfJobsWithExtension() {
		String file = "1.csv";
		String file2 = "2.csv";
		String file3 = "3.csv";
		assertTrue(inputTest.initializeListOfJobs(file, file2, file3) == true);
	}

	@Test
	public void testInitializeListOfJobsWithoutExtension() {
		String file = "1";
		String file2 = "2";
		String file3 = "3";
		assertTrue(inputTest.initializeListOfJobs(file, file2, file3) == true);
	}

	@Test
	public void testInitializeListOfJobs2() {
		String file = "32.csv";
		String file2 = "2";
		String file3 = "3";
		assertTrue(inputTest.initializeListOfJobs(file, file2, file3) == false);
	}

	// getListOfJobs()
	@Test
	public void testGetListOfJobs1() {
		String file = "1.csv";
		String file2 = "2.csv";
		String file3 = "3.csv";
		inputTest.initializeListOfJobs(file, file2, file3);
		ArrayList<Job> test = inputTest.getJobsArray();
		assertTrue(!Integer.toString(test.get(0).getJobID()).equals("1001"));
	}

	@Test
	public void testGetListOfJobs2() {
		String file = "1.csv";
		String file2 = "2.csv";
		String file3 = "3.csv";
		inputTest.initializeListOfJobs(file, file2, file3);
		ArrayList<Job> test = inputTest.getJobsArray();
		assertTrue(!Integer.toString(test.get(0).getJobID()).equals("1002"));
	}

	@Test
	public void testGetListOfJobs3() {
		String file = "1.csv";
		String file2 = "2.csv";
		String file3 = "3.csv";
		inputTest.initializeListOfJobs(file, file2, file3);
		ArrayList<Job> test = inputTest.getJobsArray();
		assertTrue(!Integer.toString(test.get(test.size() - 1).getJobID()).equals("1015"));
	}

	// initializeItemsList()
	@Test
	public void testInitializeItemsListWithExtension() {
		String file3 = "3.csv";
		assertTrue(inputTest.initializeItemsList(file3) == true);
	}

	@Test
	public void testInitializeItemsListWithoutExtension() {
		String file3 = "3";
		assertTrue(inputTest.initializeItemsList(file3) == true);
	}

}
