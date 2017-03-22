package student_solution;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import graph_entities.IEdge;
import graph_entities.IGraph;
import graph_entities.IVertex;
import graph_entities.Label;
import utils.Location;

public class Graph<T> implements IGraph<T> {
	Map<String, IVertex<T>> vertices;
	public int id = 0;
	private BufferedReader read;

	public Graph() {
		vertices = new ConcurrentHashMap<String, IVertex<T>>();
	}

	public Graph(Map<String, IVertex<T>> vertices) {
		this.vertices = vertices;
	}

	@Override
	public void addVertex(IVertex<T> vertex) {
		if (!vertices.containsValue(vertex)) {
			vertices.put(Integer.toString(++id), vertex);
		}

	}

	@Override
	public void addEdge(String vertexSrcId, String vertexTgtId, Float cost) {
		IVertex<T> targetVertex = vertices.get(vertexTgtId);
		IVertex<T> toAddEdge = vertices.get(vertexSrcId);
		toAddEdge.addEdge(new Edge<T>(targetVertex, cost));
	}

	static String removeWhite(String line) {
		String s = line.replaceAll("\\s+", "");
		return s;
	}

	public String toDotRepresentation() {
		Collection<IVertex<T>> vertices = this.getVertices();
		Iterator<IVertex<T>> it = vertices.iterator();
		String dotRep = "digraph {\n";
		String twoTabs = "\t\t";

		while (it.hasNext()) {
			IVertex<T> vertex = it.next();
			Label<T> aux = vertex.getLabel();
			dotRep += twoTabs + aux.getName() + '\n';
		}
		it = vertices.iterator();
		while (it.hasNext()) {
			Collection<IEdge<T>> successors;
			IVertex<T> vertex = (IVertex<T>) it.next();
			successors = vertex.getSuccessors();
			for (IEdge<T> edge : successors) {
				dotRep += twoTabs + vertex.getLabel().getName() + edge.toString() + '\n';
			}
		}

		dotRep += "}" + '\n';
		return dotRep;
	}

	public void fromDotRepresentation(String dotFilePath) {
		FileReader from;
		try {
			HashMap<String, String> names = new HashMap<String, String>();
			from = new FileReader(dotFilePath);
			read = new BufferedReader(from);
			read.readLine();
			int index = 0;
			CharSequence cs = "label=\"";
			while (read.ready()) {
				String line = read.readLine();
				line = removeWhite(line);
				if (line.equals("}") || line.equals("")) {

				} else if (line.contains(cs.toString())) {

					String[] parts = line.split("->");
					String source = parts[0];
					parts = parts[1].split(cs.toString());
					String target = parts[0].substring(0, parts[0].length() - 1);
					int ind = parts[1].indexOf('"');
					String number = parts[1].substring(0, ind);
					addEdge(names.get(source), names.get(target), Float.parseFloat(number));
				} else {
					Label<T> l = new Label<T>();
					l.setName(line);
					addVertex(new Vertex<T>(l));
					names.put(line, Integer.toString(index));
					index++;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<IVertex<T>> getVertices() {
		return new ArrayList<IVertex<T>>(vertices.values());
	}

	@Override
	public Collection<String> getVertexIds() {
		return vertices.keySet();
	}

	@Override
	public IVertex<T> getVertex(String vertexId) {
		return vertices.get(vertexId);
	}

	public boolean contains(IVertex<T> v) {
		return vertices.containsValue(v);
	}

	public boolean contains(T l) {
		Iterator<IVertex<T>> it = vertices.values().iterator();
		while (it.hasNext()) {
			IVertex<T> x = it.next();
			T m = x.getLabel().getData();
			if (m.equals(l)) {

				return true;
			}
		}
		return false;
	}

	public Collection<IEdge<T>> get(T pPoint) {
		Iterator<IVertex<T>> it = vertices.values().iterator();
		while (it.hasNext()) {
			IVertex<T> x = it.next();
			if (x.getLabel().getData().equals(pPoint)) {
				return x.getSuccessors();
			}
		}
		return null;
	}

	public IVertex<T> getVertex(T pPoint) {
		Iterator<IVertex<T>> it = vertices.values().iterator();
		while (it.hasNext()) {
			IVertex<T> x = it.next();
			Location l = (Location) x.getLabel().getData();
			if (l.equals(pPoint)) {
				return x;
			}
		}
		return null;
	}

}
