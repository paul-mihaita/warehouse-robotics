package student_solution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import utils.Location;


public class State {
	private List<Location> rPos;
	private int cost = 0;
	public State parent;
	public State(){
		rPos = new ArrayList<>();
	}
	public State(List<Location> x){
		setRLoc(x);
	}
	public List<Location> getRLoc(){
		return rPos;
	}
	public void setRLoc (List<Location> x){
		rPos = x;
	}
	public int getCost(){
		return cost;
	}
	public void setCost(int a){
		cost = a;
	}
	public boolean isFinal(State f){
		List<Location> fin = f.getRLoc();
		if(fin.size() == rPos.size()){
			Iterator<Location> nextRobot = rPos.iterator();
			Iterator<Location> nextItem = fin.iterator();
			while(nextRobot.hasNext()){
				Location rob = nextRobot.next();
				Location itm = nextItem.next();
				if(!rob.equals(itm)) return false;
			}
			return true;
		}
		return false;
	}
	public int compareTo(State o2) {
		
		return this.cost > o2.getCost() ? 1 : this.cost< o2.getCost() ? -1 : 0;
	}
	@Override
	public  boolean equals(Object other) {
		
	    if (other instanceof State) {
	    	 List<Location> a = this.getRLoc();
	    	 List<Location> b = ((State) other).getRLoc();
			return equalArrays(a,b);
	    }
	    return false;
	  }
	private boolean equalArrays(List<Location> a, List<Location> b) {
		if(a.size() != b.size())
			return false;
		Iterator<Location> ait = a.iterator();
		Iterator<Location> bit = b.iterator();
		while(ait.hasNext()){
			Location aaa = ait.next();
			Location bbb = bit.next();
			if(!aaa.equals(bbb)) return false;
		}
		return true;
	}
}
