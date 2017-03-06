package utils;

public class Task {
	Item item;

	int quantity;

	public Task(String itemName, int quantity) {
		item = new Item(itemName);
		this.quantity = quantity;
	}

	// GET
	public int getQuantity() {
		return quantity;
	}

	public Item getTaskItem() {
		return item;
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
}
