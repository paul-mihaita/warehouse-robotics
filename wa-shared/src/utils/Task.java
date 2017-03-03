package utils;

public class Task {

  Item item;
  int quantity;
  
  public Task(String itemName,int quantity){
	  item= new Item(itemName);
	  this.quantity=quantity;
  }
  
  public Item getTaskItem(){
	  return item;
  }
}
