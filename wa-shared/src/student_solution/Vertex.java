package student_solution;

import java.util.ArrayList;
import java.util.Collection;

import graph_entities.IEdge;
import graph_entities.IVertex;
import graph_entities.Label;

public class Vertex<T> implements IVertex<T> {
	Label<T> label;
	Collection<IEdge<T>> edges;
	boolean reserved = false;

	public Vertex() {
		edges = new ArrayList<>();
		label = new Label<T>();
	}

	public Vertex(Label<T> label) {
		edges = new ArrayList<>();
		setLabel(label);
	}

	public Vertex(T loc) {
		Label<T> label = new Label<T>();
		label.setData(loc);
		edges = new ArrayList<>();
		setLabel(label);
	}

	public Vertex(Label<T> label, Collection<IEdge<T>> edges) {
		this.edges = edges;
		setLabel(label);
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean x) {
		reserved = x;
	}

	@Override
	public int compareTo(IVertex<T> o) {
		Label<T> compared = o.getLabel();
		Float vertexValue = label.getCost();
		Float comparedValue = compared.getCost();
		return vertexValue < comparedValue ? -1 : vertexValue > comparedValue ? 1 : 0;
	}

	@Override
	public void addEdge(IEdge<T> edge) {
		edges.add(edge);
	}

	@Override
	public Collection<IEdge<T>> getSuccessors() {
		return edges;
	}

	@Override
	public Label<T> getLabel() {
		return label;
	}

	@Override
	public void setLabel(Label<T> label) {
		this.label = label;
	}

}