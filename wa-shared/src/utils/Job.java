package utils;

public class Job {

  int jobID;
	ArrayList<Task> task= new ArrayList<Task>();
	
	public int sumOfWeight(int quantity, int weight){
		
		int sumOfWeight = quantity*weight;
		return sumOfWeight;
  }
  
}
