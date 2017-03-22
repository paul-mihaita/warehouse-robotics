package main.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import utils.Item;
import utils.Job;
import utils.Task;

public class JobCancel {
	private static double ProbC = 0;
	private static Hashtable<Item,Hashtable<Integer,Integer>> quantProb = new Hashtable<Item,Hashtable<Integer,Integer>>();
	private static ArrayList<Job> jobs = new ArrayList<>();
	private static HashMap<Job, Boolean> jobsCanceled;
	
	private static void initialiseQuantProb(HashMap<Job, Boolean> jobs){
		
		Collection<Boolean> canceled = jobs.values();
		int contor = 0;
		for(boolean b : canceled){
			if (b == true)
				 contor++;
		}
		Hashtable<Item,Hashtable<Integer,Integer>> quantAuxCanceled = getQuant(jobs,true);	
		Hashtable<Item,Hashtable<Integer,Integer>> quantAuxNotCanceled = getQuant(jobs,false);	
		
		ProbC = (double)contor / canceled.size();
		
		for(Item it : quantAuxCanceled.keySet()){
			Hashtable<Integer, Integer> list = quantAuxCanceled.get(it);
			int nrOfQuant = list.size();
			
		}
	}
	private static Hashtable<Item, Hashtable<Integer, Integer>> getQuant(HashMap<Job, Boolean> jobs,boolean canceled) {
		Hashtable<Item,Hashtable<Integer,Integer>> quantAux = new Hashtable<Item,Hashtable<Integer,Integer>>();
		for(Job job : jobs.keySet()){
			if(jobs.get(job) == canceled){
				ArrayList<Task> tasks = job.getTasks();
				for(Task task : tasks){
					int quant = task.getQuantity();
					if(quantAux.contains(task.getItem())){
						Hashtable<Integer, Integer> list = quantAux.get(task.getItem());
						if(list.containsKey(quant)){
							int v = list.get(quant);
							list.put(quant, ++v);
						}
						else{
							list.put(quant, 1);
						}
					}else{
						Hashtable<Integer, Integer> list = new Hashtable<Integer,Integer>();
							list.put(quant, 1);
							quantAux.put(task.getItem(), list);
					}
				}
			}
		}
		return quantAux;
	}
	/*private static void feedData(ArrayList<Job> data){
		jobs = data;
		Hashtable<Job,Boolean> predicitons = new Hashtable<Job,Boolean>();
		for(Job job : jobs){
			ArrayList<Task> tasks = job.getTasks();
			double jobProb = 0;
			double jobProbAux;
			for(Task task : tasks){
				int quant = task.getQuantity();
				if(quantProb.containsKey(quant))
					jobProbAux = quantProb.get(quant);
				else{
					Integer closest = getClosest(quant);
					jobProbAux = quantProb.get(closest);
				}
				if(jobProb == 0 )
					jobProb = jobProbAux;
				else{
					jobProb+= jobProbAux;
				}
			}
			System.out.println(jobProb  + "   " + jobsCanceled.get(job));
		}
	}
	private static Integer getClosest(int quant) {
		int min = Integer.MAX_VALUE;
		int aux;
		for(Integer in : quantProb.keySet()){
			aux = Math.abs(quant - in);
			if(aux < min)
				min = aux;
		}
		return min;
	}*/
	public static void main (String [] args){
		Input input = new Input(false);
		jobsCanceled = input.getCancelledJobs();
		initialiseQuantProb(jobsCanceled);
		ArrayList<Job> jobsToPredict = new ArrayList<>();
		for(Job j : jobsCanceled.keySet()){
			jobsToPredict.add(j);
		}
		//feedData(jobsToPredict);
	}
}
