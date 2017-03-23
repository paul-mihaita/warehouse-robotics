package utils;

//Item of a task
public class Item {
	private String name;
	private Location location;
	private float weight;
	private float reward;

	public Item(String name) {
		this.weight = 0;
		this.reward = 0;
		this.name = name;
	}

	// GETS
	public String getItemName() {
		return name;
	}

	public Location getLocation() {
		return location;
	}

	public float getWeight() {
		return weight;
	}

	public float getReward() {
		return reward;
	}

	public String getName() {
		return name;
	}

	// SETS
	public void setReward(float reward) {
		this.reward = reward;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public void setLocation(int x, int y) {
		location = new Location(x, y);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public boolean equalsItem(Item item) {
		return item.getName().equals(this.getName());
	}
}
