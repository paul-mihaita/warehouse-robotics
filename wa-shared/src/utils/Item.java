package utils;
//Item of a task
public class Item {
	
  String name;
  Location location;
  float weight;
  String reward;

  public Item(String name){
	  this.name=name;
  }
  
  public String getItemName(){
	  return name;
  }
  
  public void setReward(String reward){
	  this.reward=reward;
  }
  
  public void setWeight(Float weight){
	  this.weight=weight;
  }
  
}
