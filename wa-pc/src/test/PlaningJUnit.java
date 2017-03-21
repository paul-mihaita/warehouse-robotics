package test;

import graph_entities.IVertex;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import main.job.InputTest;
import main.route.CommandCenter;
import movement.Movement.move;
import student_solution.State;
import utils.Item;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;

import org.apache.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.*;
public class PlaningJUnit {
	private final Logger LOG = Logger.getLogger(PlaningJUnit.class);
	public static void main(String[] args) {
		PlaningJUnit t = new PlaningJUnit();
		t.testAddFromOriginToMaxXMaxY();
		t.testTwoRobotsNotCollide();
	}
	@Test
	public  void testAddFromOriginToMaxXMaxY(){
		System.out.println("testAddFromOriginToMaxXMaxY");
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(0, 0);
		l.add(a);
		
		s.setRLoc(l);

		ArrayList<Robot> robots = new ArrayList<>();
		Location c = new Location(-1, -1);
		Robot r1 = new Robot(null, null, c, a);
		
		r1.setOrientation(new Location (1,0));
		
		robots.add(r1);
		

		ArrayList<Task> tasks = new ArrayList<Task>() ;
		Task task = new Task(" ", 0);
		Item itm = new Item("gfhf");
		itm.setLocation(11, 7);
		Job job = new Job(2, tasks);
		task.setItem(itm);
		tasks.add(task);
		job.setTasks(tasks);
		HashMap<Robot, Job> lll = new HashMap<>();
		
		Item itm2 = new Item("gfhf");
		itm2.setLocation(11, 5);
		lll.put(r1, job);
		CommandCenter.generatePaths(lll);
		HashMap<Robot, ArrayList<ArrayList<Location>>> xxx = CommandCenter.getPathLocations();
		Set<Robot> rob = xxx.keySet();
		int rr = 1;
		int robo = 1;
		LOG.debug("Creating tasks jobs and robot");
		for(Robot r : rob ){
			ArrayList<ArrayList<Location>> auxx = xxx.get(r);
			rr = 0;
			System.out.println("Robot " + robo);
			for(ArrayList<Location> au :auxx){
				System.out.print("Route " + rr+" : " );
				rr++;
				for(Location auu : au){
					System.out.print(auu.toString() + " ");
				}
				System.out.println();
				assertEquals(itm.getLocation(), au.get(au.size()-1));
				LOG.debug("printing routes");

			}
			robo++;
		}
	}
	@Test
	public  void testTwoRobotsNotCollide(){
		System.out.println("testTwoRobotsNotCollide");
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(0, 0);
		l.add(a);
		Location b = new Location(5, 0);
		l.add(b);
		s.setRLoc(l);
		

		ArrayList<Robot> robots = new ArrayList<>();
		Location c = new Location(-1, -1);
		Robot r1 = new Robot(null, null, c, a);
		Robot r2 = new Robot(null, null, c, b);

		r1.setOrientation(new Location (1,0));
		r2.setOrientation(new Location (4,0));
		robots.add(r1);
		robots.add(r2);
		

		ArrayList<Task> tasks = new ArrayList<Task>() ;
		Task task = new Task(" ", 0);
		Item itm = new Item("gfhf");
		itm.setLocation(5, 0);
		Job job = new Job(2, tasks);
		task.setItem(itm);
		tasks.add(task);
		job.setTasks(tasks);
		HashMap<Robot, Job> lll = new HashMap<>();
		
		ArrayList<Task> tasks2 = new ArrayList<Task>() ;
		Task task2 = new Task(" ", 0);
		Item itm2 = new Item("gfhf");
		itm2.setLocation(0, 0);
		task2.setItem(itm2);
		tasks2.add(task2);
		tasks2.add(task);
		Job job2 = new Job(2, tasks2);
		job2.setTasks(tasks2);
		lll.put(r1, job);
		lll.put(r2, job2);
		CommandCenter.generatePaths(lll);
		HashMap<Robot, ArrayList<ArrayList<Location>>> xxx = CommandCenter.getPathLocations();
		Set<Robot> rob = xxx.keySet();
		
		int rr = 1;
		int robo = 1;
		ArrayList<ArrayList<Location>> pths = new ArrayList<>();
		LOG.debug("Creating tasks jobs and robot");

		for(Robot r : rob ){
			ArrayList<ArrayList<Location>> auxx = xxx.get(r);
			rr = 0;
			Item it = null ;
			ArrayList<Task> list = lll.get(r).getTasks();
			Task tas = list.get(0);
			it = tas.getItem();
			System.out.println("Robot "+robo);
			for(ArrayList<Location> au :auxx){
				System.out.print("Route " + rr+" : " );
				rr++;
				for(Location auu : au){
					System.out.print(auu.toString() + " ");
					
				}
				pths.add(au);
				System.out.println();
				assertEquals(it.getLocation(), au.get(au.size()-1));
				LOG.debug("printing routes");
			}
			robo++;
		}
		ArrayList<Location> aa = pths.get(0);
		ArrayList<Location> bb = pths.get(1);
		for(int j = 0; j < Math.min(aa.size(), bb.size())-1;j++){
			assertTrue(!aa.get(j).equals(bb.get(j+1)));
		}
		
	}
}
