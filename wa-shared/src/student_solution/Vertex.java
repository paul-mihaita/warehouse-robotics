package student_solution;


import java.util.ArrayList;
import java.util.Collection;

import utils.Location;
import graph_entities.IEdge;
import graph_entities.IVertex;
import graph_entities.Label;

 public class Vertex<T> implements IVertex<T>
 {
	 Label<T> label;
	 Collection<IEdge<T>> edges;
	 public Vertex(){
		 edges = new ArrayList<>();
		 label = new Label();
	 }
	 public Vertex(Label label){
		 edges = new ArrayList<>();
		 setLabel(label);
	 }
	 public Vertex(Location loc){
		 Label label= new Label();
		 label.setData(loc);
		 edges = new ArrayList<>();
		 setLabel(label);
	 }
	 public Vertex (Label label,Collection<IEdge<T>> edges ){
		 this.edges = edges;
		 setLabel(label);
	 }
	@Override
	public int compareTo(IVertex<T> o) {
		Label compared = o.getLabel();
		Float vertexValue = label.getCost();
		Float comparedValue = compared.getCost();
		return vertexValue < comparedValue ? -1 :  vertexValue > comparedValue ? 1 : 0;
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