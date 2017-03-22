package student_solution;

import graph_entities.IEdge;
import graph_entities.IVertex;

public class Edge<T> implements IEdge<T> {
	IVertex<T> target;
	Float cost;
	Float costAux = (float) 0; // used for a*

	public Edge() {
		target = new Vertex<T>();
		cost = 0.0f;
	}

	public Edge(IVertex<T> target, Float cost) {
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

	public void setCost(Float a) {
		cost = a;
	}

	public Float getCostAux() {
		return costAux;
	}

	public void setCostAux(Float a) {
		costAux = a;
	}

	@Override
	public String toString() {
		String s = "";
		s += " -> " + target.getLabel().getName() + "[label=\"" + cost + "\"];";
		return s;
	}
};