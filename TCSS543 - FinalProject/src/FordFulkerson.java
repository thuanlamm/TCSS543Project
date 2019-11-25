import java.util.*;

/**
 * Ford-Fulkerson algorithm Implementation.
 * 
 * @author Thuan Lam
 * @date 11-23-2019
 */
public class FordFulkerson {
	/** The given graph that needs to calculate the max flow value. */
	private SimpleGraph myGraph;
	/** The index of the sink vertex in the list of vertices. */
	private int mySinkVertexIndex = -1;
//	/** The name of the source vertex. s by default */
//	private String mySourceVertexName = "s";
	/** The name of the sink vertex. t by default*/
	private String mySinkVertexName = "t";
	/** Debug mode. */
	private boolean myDebugMode = false; // true of false
	
	
	/**
	 * The constructor of the FordFulkerson class.
	 * 
	 * @param theGraph the input graph that need to calculate max flow.
	 */
	public FordFulkerson(SimpleGraph theGraph) {
		myGraph = theGraph;
		mySinkVertexIndex = findVertexIndexByName(this.mySinkVertexName);
	}
	
	/**
	 * Set the name for the sink vertex.
	 * @param theSourceName the new name of the source vertex.
	 */
	public void setSinkVertexName(String theSinkName) {
		this.mySinkVertexName = theSinkName;
	}
	
	/**
	 * Get the max flow of the given graph.
	 * 
	 * @return the flow value.
	 */
	public double getMaxFlow() {
		Double maxFlow = 0.0;
		while (findAugmentingPath()) {
			Double bottleNeckValue = updateGraph();
			maxFlow += bottleNeckValue; // set bottleneckEdge in find path

			if (this.myDebugMode) {
				String st = getAugmentingPath(this.myGraph.vertexList.get(mySinkVertexIndex));
				st = st + " (bottle neck = " + bottleNeckValue.toString() + ") => Max flow = " + maxFlow;
				;
				System.out.println(st);
			}
		}
		return maxFlow;
	}

	/**
	 * Get the index of the vertex by the vertex's name.
	 * 
	 * @param theVertexName a string that is the name of a vertex.
	 * @return the index of the vertex in the list of vertices. Return -1 if not found.
	 */
	private int findVertexIndexByName(String theVertexName) {
		int index = 0;
		for (Iterator<Vertex> ite = myGraph.vertices(); ite.hasNext(); index++) {
			Vertex vertex = ite.next();
			if (vertex.getName().toString().equals(theVertexName)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * Try to find the augmenting path by using Depth-First-Search algorithm.
	 * 
	 * @return true if an augmenting path was found. Otherwise, false
	 */
	private boolean findAugmentingPath() {
		boolean[] visited = new boolean[myGraph.numVertices()];
		for (int j = 0; j < myGraph.numVertices(); j++)
			visited[j] = false;

		visited[0] = true;
		return dfsOnGraph(myGraph.vertexList.getFirst(), visited);
	}

	/**
	 * Get the path from the source vertex to the given vertex.
	 * 
	 * @param vertex the destination
	 * @return a string of the path from the source vertex to the given vertex. For
	 *         example, S->A->B->GivenVertex.
	 */
	private String getAugmentingPath(Vertex vertex) {
		String st = "";
		while (vertex != null) {
			st = vertex.getName().toString() + "->" + st;
			vertex = (Vertex) vertex.getData();
		}
		return st.substring(0, st.length() - 2).toString();
	}

	/**
	 * Get the index of the given edge from the list of edges.
	 * 
	 * @param theStartVertex the start vertex.
	 * @param theEndVertex   the end vertex.
	 * @return the index of the edge that connects 2 given vertices if found.
	 *         Otherwise, return -1.
	 */
	private int findEdgeIndex(Vertex theStartVertex, Vertex theEndVertex) {
		for (Iterator<Edge> ite = myGraph.incidentEdges(theStartVertex); ite.hasNext();) {
			Edge edge = ite.next();
			if (edge.getSecondEndpoint() == theEndVertex)
				return myGraph.edgeList.indexOf(edge);
		}
		return -1;
	}

	/**
	 * Function performs Depth-First-Search algorithm to identify whether there is a
	 * path between source and sink and returns true is there is a path else false.
	 * 
	 * @param theStartVertex
	 * @param theVisitedList
	 * @return true false
	 */

	/**
	 * A recursive function that uses the Depth-First-Search algorithm to find a
	 * path from the given vertex to the sink.
	 * 
	 * @param theStartVertex a vertex that we start from.
	 * @param theVisitedList a list of boolean values that we use to track all vertices.
	 * @return true if we can find a path to the sink. Otherwise, false.
	 */
	private boolean dfsOnGraph(Vertex theStartVertex, boolean[] theVisitedList) {
		if (!theVisitedList[mySinkVertexIndex]) {
			String st = "";

			if (this.myDebugMode) {
				st = getAugmentingPath(theStartVertex);
				System.out.println(st);
			}

			for (Iterator<Edge> ite = myGraph.incidentEdges(theStartVertex); ite.hasNext();) {
				Edge edge = ite.next();
				Vertex vertex = edge.getSecondEndpoint();

				if (this.myDebugMode)
					System.out.println(st + "=>" + vertex.getName());

				int index = myGraph.vertexList.indexOf(vertex);
				if (!theVisitedList[index] && (Double) edge.getData() > 0) {
					theVisitedList[index] = true;
					vertex.setData(edge.getFirstEndpoint());
					if (dfsOnGraph(vertex, theVisitedList))
						return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * Update the graph based on the augmenting path. The augmenting path
	 * information is in the sink vertex.
	 * 
	 * @return the bottle neck value.
	 */
	private double updateGraph() {
		Vertex previousVertex;
		Vertex currentVertex = myGraph.vertexList.get(mySinkVertexIndex);
		Double bottleNeckValue = Double.MAX_VALUE;
		int edgeIndex;

		// find bottle neck of the augmenting path
		while (currentVertex != null) {
			previousVertex = (Vertex) currentVertex.getData();
			if (previousVertex != null) {
				edgeIndex = findEdgeIndex(previousVertex, currentVertex);
				Edge edge = myGraph.edgeList.get(edgeIndex);
				bottleNeckValue = Math.min((double) bottleNeckValue, (double) edge.getData());
			}
			currentVertex = previousVertex;
		}

		// update all edges of the augmenting path.
		currentVertex = myGraph.vertexList.get(mySinkVertexIndex);
		while (currentVertex != null) {
			previousVertex = (Vertex) currentVertex.getData();
			if (previousVertex != null) {
				// update the forward flow
				edgeIndex = findEdgeIndex(previousVertex, currentVertex);
				Edge edge = myGraph.edgeList.get(edgeIndex);
				edge.setData((Double) edge.getData() - bottleNeckValue);

				// update the backward flow
				edgeIndex = findEdgeIndex(currentVertex, previousVertex);
				edge = myGraph.edgeList.get(edgeIndex);
				edge.setData((Double) edge.getData() + bottleNeckValue);
			}
			currentVertex = previousVertex;
		}
		return bottleNeckValue;
	}
}