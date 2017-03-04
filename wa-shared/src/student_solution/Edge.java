package student_solution;

import graph_entities.IVertex;
import graph_entities.IEdge;

 public class Edge<T> implements IEdge<T>
 {
	 IVertex<T> target;
	 Float cost;
	 public Edge(){
		 target = new Vertex<T>();
		 cost = 0.0f;
	 }
	 public Edge(IVertex<T> target, Float cost){
		 this.target = target;
		 this.cost = cost;
	 }
	@Override
	public IVertex<T> getTgt() {
		return target;
	}

	@Override
	public Float getCost() {
		return cost;
	}
	
	@Override
	public String toString() {
		String s  = "";
		s+=" -> "+target.getLabel().getName() + "[label=\""+cost+"\"];";
		return s;
	}
 };