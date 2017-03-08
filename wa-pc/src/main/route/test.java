package main.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import movement.Movement.move;
import student_solution.State;
import utils.Item;
import utils.Job;
import utils.Location;
import utils.Robot;
import utils.Task;

public class test {
	public static void main(String[] args) {

		/*
		 * BiFunction<State, State, Integer> manhatan = new BiFunction<State,
		 * State, Integer> (){
		 * 
		 * @Override public Integer apply(State a, State b) { // TODO
		 * Auto-generated method stub List<Location> from = a.getRLoc();
		 * List<Location> to = b.getRLoc(); int cost = 0; for(int i = 0; i <
		 * from.size();i++){ Location t = from.get(i); Location u = to.get(i);
		 * cost+= (Math.abs(t.getX() - u.getX()) + Math.abs(t.getY() -
		 * u.getY())); } return cost; }
		 * 
		 * }; ArrayList<Location> route = Astar.aStar(graph,start ,finish ,
		 * manhatan, 3 ,false,gridMap); for(Location l : route){
		 * System.out.print(l.getX() + "  " + l.getY() + " | "); }
		 * System.out.println(); System.out.println(Astar.simpleAc);
		 */
		State s = new State();
		ArrayList<Location> l = new ArrayList<>();
		Location a = new Location(3, 0);
		l.add(a);
		Location b = new Location(4, 0);
		l.add(b);
		Location d = new Location(5, 0);
		l.add(d);
		s.setRLoc(l);

		ArrayList<Location> fin = new ArrayList<>();
		fin.add(new Location(5, 0));
		fin.add(new Location(11, 5));
		fin.add(new Location(3, 0));
		State t = new State();
		t.setRLoc(fin);

		ArrayList<Robot> robots = new ArrayList<>();
		Location c = new Location(-1, -1);
		Robot r1 = new Robot(null, null, c, a);
		Robot r2 = new Robot(null, null, c, b);
		Robot r3 = new Robot(null, null, c, d);
		r1.setOrientation(new Location(4, 0));
		r2.setOrientation(new Location(3, 0));
		r3.setOrientation(new Location(4, 0));
		robots.add(r1);
		robots.add(r2);
		robots.add(r3);

		ArrayList<Task> tasks = new ArrayList<Task>();
		Task task = new Task(" ", 0);
		Item itm = new Item("gfhf");
		itm.setLocation(11, 7);
		Job job = new Job(2, tasks);
		task.setItem(itm);
		tasks.add(task);
		job.setTasks(tasks);
		HashMap<Robot, Job> lll = new HashMap<>();
		
		ArrayList<Task> tasks2 = new ArrayList<Task>() ;
		Task task2 = new Task(" ", 0);
		Item itm2 = new Item("gfhf");
		itm2.setLocation(11, 5);
		Job job2 = new Job(2, tasks2);
		task2.setItem(itm2);
		tasks2.add(task2);
		job2.setTasks(tasks2);
		tasks.add(task2);
		job.setTasks(tasks);
		lll.put(r1, job);
		HashMap<Robot, ArrayList<ArrayList<move>>> xxx = CommandCenter.generatePaths(lll);
		Set<Robot> rob = xxx.keySet();
		int rr = 1;
		int robo = 1;
		for(Robot r : rob ){
			ArrayList<ArrayList<move>> auxx = xxx.get(r);
			rr = 0;
			System.out.println("Robot " + robo);
			for(ArrayList<move> au :auxx){
				System.out.print("Route " + rr+" : " );
				rr++;
				for(move auu : au){
					System.out.print(auu + " ");
				}
				System.out.println();
			}
			robo++;
		}
	}
}
