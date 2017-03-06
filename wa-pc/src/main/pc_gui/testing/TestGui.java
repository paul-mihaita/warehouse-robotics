package main.pc_gui.testing;

import java.util.ArrayList;
import main.pc_gui.GUI;
import utils.Item;
import utils.Job;
import utils.Task;

public class TestGui {

	public static void main(String[] args) {

		ArrayList<Job> jobs = new ArrayList<Job>();

		ArrayList<Task> tasks = new ArrayList<Task>();

		Item k = new Item("k");
		k.setWeight(20);
		k.setLocation(10, 2);
		k.setReward(20);

		Task t = new Task("k", 12);

		t.setItem(k);

		tasks.add(t);

		Item z = new Item("k");
		z.setWeight(2);
		z.setLocation(1, 20);
		z.setReward(2);

		Task x = new Task("k", 2);

		x.setItem(z);

		tasks.add(x);

		Job j = new Job(0, tasks);

		Job j1 = new Job(1, tasks);
		jobs.add(j1);
		jobs.add(j);
		GUI.create(jobs);
	}

}
