package graph_entities;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public interface IGraph<T> {

	void addEdge(String vertexSrcId, String vertexTgtId, Float cost);

	Collection<IVertex<T>> getVertices();

	Collection<String> getVertexIds();

	IVertex<T> getVertex(String vertexId);

	// If you don't implement any of the above, you will lose marks, but
	// at least your submission will compile.
	String toDotRepresentation();

	void fromDotRepresentation(String dotFilePath);

	default Result<T> breadthFirstSearchFrom(String vertexId, Predicate<IVertex<T>> pred) {
		return new Result<T>();
	}

	default Result<T> depthFirstSearchFrom(String vertexId, Predicate<IVertex<T>> pred) {
		return new Result<T>();
	}

	default Result<T> dijkstraFrom(String vertexId, Predicate<IVertex<T>> pred) {
		return new Result<T>();
	}

	default Result<T> aStar(String startVertexId, String endVertexId,
			BiFunction<IVertex<T>, IVertex<T>, Float> heuristics) {
		return new Result<T>();
	}

	void addVertex(IVertex<T> vertex);
}
