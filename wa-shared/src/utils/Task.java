package utils;

public class Task {

	private Item item;

	private int quantity;

	public Task(String itemName, int quantity) {
		item = new Item(itemName);
		this.quantity = quantity;
	}
	
	@Override
	public String toString(){
		return "(" + item.getItemName() + ", " + this.getQuantity() + ") ";
	}

	// GET
	public int getQuantity() {
		return quantity;
	}

	public Item getItem() {
		return item;
	}

	// SET
	public void setItem(Item item) {
		this.item = item;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public float getTaskReward(){
		return item.getReward()*quantity;
	}
}
