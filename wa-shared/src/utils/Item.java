package utils;

//Item of a task
public class Item {

	String name;
	Location location;
	float weight;
	float reward;

	public Item(String name) {
		this.name = name;
	}

	public String getItemName() {
		return name;
	}

	public void setReward(float reward) {
		this.reward = reward;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public void setLocation(int x, int y) {
		location = new Location(x, y);
	}

}
