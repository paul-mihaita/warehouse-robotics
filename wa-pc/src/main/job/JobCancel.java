package main.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;

import utils.Item;
import utils.Job;
import utils.Task;

public class JobCancel {
	private static double ProbC = 0;
	private static Hashtable<Integer,Double> quantProb = new Hashtable<Integer,Double>();
	private static ArrayList<Job> jobs = new ArrayList<>();
	private static HashMap<Job, Boolean> jobsCanceled;
	private static void initialiseQuantProb(HashMap<Job, Boolean> jobs){
		
		Collection<Boolean> canceled = jobs.values();
		int contor = 0;
		for(boolean b : canceled){
			if (b == true)
				 contor++;
		}
		Hashtable<Integer, Integer> quantAux = new Hashtable<Integer,Integer>();
		ProbC = (double)contor / canceled.size();
		for(Job job : jobs.keySet()){
			if(jobs.get(job) == true){
				ArrayList<Task> tasks = job.getTasks();
				for(Task task : tasks){
					int quant = task.getQuantity();
					//Item itm = task.getItem();
					//float reward = task.getTaskReward();
					if(!quantAux.containsKey(quant)){
						quantAux.put(quant, 1);
					}else{
						Integer integer = quantAux.get(quant);
						quantAux.remove(quant);
						quantAux.put(quant,++integer );
					}
				}
			}
		}
		int nrOfTimes = 0;
		for(Integer val : quantAux.values()){
			nrOfTimes += val;
		}
		for(Integer key : quantAux.keySet()){
			if((double)quantAux.get(key) / nrOfTimes > 0.001)
				quantProb.put(key, (double)quantAux.get(key) / nrOfTimes );
			else{
				quantProb.put(key, (double) 0);

			}
		}
	}
	private static void feedData(ArrayList<Job> data){
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
	}
	public static void main (String [] args){
		Input input = new Input(false);
		jobsCanceled = input.getCancelledJobs();
		initialiseQuantProb(jobsCanceled);
		ArrayList<Job> jobsToPredict = new ArrayList<>();
		for(Job j : jobsCanceled.keySet()){
			jobsToPredict.add(j);
		}
		feedData(jobsToPredict);
		System.out.println(input.getItemsArray().size());
	}
}
