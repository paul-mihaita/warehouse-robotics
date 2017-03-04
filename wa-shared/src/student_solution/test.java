package student_solution;

import java.util.ArrayList;

import graph_entities.IEdge;
import graph_entities.Label;


public class test {
	public static void main(String[] args){
		String path = System.getProperty("user.dir");
		path += "/files/graph.txt";
		Graph<String> aux = new Graph();
		aux.fromDotRepresentation(path);
		/*
		Label<String> al = new Label();
		Label<String> bl = new Label();
		Label<String> cl = new Label();
		al.setName("a");
		bl.setName("b");
		cl.setName("c");
		al.setCost(3f);
		bl.setCost(4f);
		cl.setCost(5f);
		Vertex<String> a = new Vertex(al);
		Vertex<String> b = new Vertex(bl);
		Vertex<String> c = new Vertex(cl);
		ArrayList<IEdge<String>> edges = new ArrayList<>();
		Edge toB = new Edge(b,5f);
		Edge toA = new Edge(a,4f);
		Edge toC = new Edge(c,3f);
		a.addEdge(toB);
		a.addEdge(toC);
		b.addEdge(toC);
		c.addEdge(toA);
		aux.addVertex("1", a);
		aux.addVertex("2", b);
		aux.addVertex("3", c);
		*/
		System.out.println(aux.toDotRepresentation());

	}
}
