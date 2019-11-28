import java.util.*;

/**
 * Capability Scaling Ford-Fulkerson algorithm Implementation.
 * 
 * @author Thuan Lam
 * @date 11-24-2019
 */
public class CapabilityScalingFordFulkerson {
	/** The given graph that needs to calculate the max flow value. */
	private SimpleGraph myGraph;
	/** The index of the sink vertex in the list of vertices. */
	private int mySinkVertexIndex = -1;
	/** The name of the source vertex. s by default */
	private String mySourceVertexName = "s";
	/** The name of the sink vertex. t by default*/
	private String mySinkVertexName = "t";
	/** Debug mode. */
	private boolean myDebugMode = true; // true of false
	
	
	/**
	 * The constructor of the Capability Scaling FordFulkerson class.
	 * 
	 * @param theGraph the input graph that need to calculate max flow.
	 */
	public CapabilityScalingFordFulkerson(SimpleGraph theGraph) {
		myGraph = theGraph;
		addBackEdges();
		mySinkVertexIndex = findVertexIndexByName(this.mySinkVertexName);
		if (mySinkVertexIndex < 0) {
			System.out.println("Cannot find the Sink vertex 't'");
		}
	}

	/**
	 * Get the max flow of the graph.
	 * 
	 * @return the flow value.
	 */
	public double getMaxFlow() {
		double maxFlow = 0.0;
		int deltaValue = getDelta();
		
		if (myDebugMode)
			System.out.println("Delta = " + deltaValue);
		
		while (deltaValue > 0) {
			while (findAugmentingPath(deltaValue)) {
				double bottleNeckValue = updateGraph();
				maxFlow += bottleNeckValue; // set bottleneckEdge in find path

				if (this.myDebugMode) {
					String st = getAugmentingPath((Vertex) this.myGraph.vertexList.get(mySinkVertexIndex));
					st = st + " (bottle neck = " + Double.toString(bottleNeckValue) + ") => Max flow = " + maxFlow;
					;
					System.out.println(st);
				}
			}
			
			deltaValue = (deltaValue >>  1);
		}
		return maxFlow;
	}

	/**
	 * Add back edge for every edge in the graph.
	 */
	private void addBackEdges() {
		LinkedList<Edge> backEdges = new LinkedList<Edge>();
		
        for (Iterator<?> ite = myGraph.edges(); ite.hasNext(); ) {
            Edge edge = (Edge) ite.next();     
            Edge newEdge = new Edge(edge.getSecondEndpoint(), edge.getFirstEndpoint(), 0.0, null);
            backEdges.addLast(newEdge);
        }
        
		for (Iterator<Edge> ite = backEdges.iterator(); ite.hasNext();) {
			Edge edge = ite.next();
			this.myGraph.insertEdge(edge.getFirstEndpoint(), edge.getSecondEndpoint(), 0.0, null);
		}
		
		if (this.myDebugMode) {
	        for (Iterator<?> ite = myGraph.edges(); ite.hasNext(); ) {
	            Edge edge = (Edge) ite.next();
	            Vertex v1 = edge.getFirstEndpoint();
	            Vertex v2 = edge.getSecondEndpoint();
	            double capability = (double)edge.getData();
	            System.out.println("edge " + v1.getName() + v2.getName() + " cap = " + capability);
	        }
		}
    }
	
	/**
	 * Get the index of the vertex by the vertex's name.
	 * 
	 * @param theVertexName a string that is the name of a vertex.
	 * @return the index of the vertex in the list of vertices. Return -1 if not found.
	 */
	private int findVertexIndexByName(String theVertexName) {
		int index = 0;
		for (Iterator<?> ite = myGraph.vertices(); ite.hasNext(); index++) {
			Vertex vertex = (Vertex) ite.next();
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
	private boolean findAugmentingPath(double theDeltaValue) {
		boolean[] visited = new boolean[myGraph.numVertices()];
		for (int j = 0; j < myGraph.numVertices(); j++)
			visited[j] = false;

		visited[0] = true;
		return dfsOnGraph((Vertex) myGraph.vertexList.getFirst(), visited, theDeltaValue);
	}

	/**
	 * Get the path from the source vertex to the given vertex.
	 * 
	 * @param vertex the destination
	 * @return a string of the path from the source vertex to the given vertex.
	 *         For example, S->A->B->GivenVertex.
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
		for (Iterator<?> ite = myGraph.incidentEdges(theStartVertex); ite.hasNext();) {
			Edge edge = (Edge) ite.next();
			if (edge.getSecondEndpoint() == theEndVertex)
				return myGraph.edgeList.indexOf(edge);
		}
		return -1;
	}

	/**
	 * Get the delta value based on all edges' capabilities.
	 * 
	 * @return an integer as the delta value.
	 */
	private int getDelta() {
		double maxCapacity = Double.MIN_VALUE;
		int sourceVertexIndex = findVertexIndexByName(this.mySourceVertexName);
		Vertex sourceVertex = (Vertex) this.myGraph.vertexList.get(sourceVertexIndex);
		
		if (myDebugMode)
			System.out.println("Delta:"); 
			
        for (Iterator<?> edges = this.myGraph.incidentEdges(sourceVertex); edges.hasNext();) {
            Edge edge = (Edge) edges.next();
			maxCapacity = Math.max(maxCapacity, (double) edge.getData());
			
			if (myDebugMode)
				System.out.println("    edge " + edge.getFirstEndpoint().getName().toString() + edge.getSecondEndpoint().getName().toString() + " = " + edge.getData());
		}
		return 1 << (int) (Math.log(maxCapacity) / Math.log(2));
	}

	/**
	 * A recursive function that uses the Depth-First-Search algorithm with the
	 * delta value to find a path from the given vertex to the sink.
	 * 
	 * @param theStartVertex a vertex that we start from.
	 * @param theVisitedList a list of boolean values that we use to track all vertices.
	 * @return true if we can find a path to the sink. Otherwise, false.
	 */
	private boolean dfsOnGraph(Vertex theStartVertex, boolean[] theVisitedList, double theDeltaValue) {
		if (!theVisitedList[mySinkVertexIndex]) {
			String st = "";

			if (this.myDebugMode) {
				st = getAugmentingPath(theStartVertex);
				System.out.println(st);
			}

			for (Iterator<?> ite = myGraph.incidentEdges(theStartVertex); ite.hasNext();) {
				Edge edge = (Edge) ite.next();
				Vertex vertex = edge.getSecondEndpoint();

				if (this.myDebugMode)
					System.out.println(st + "=>" + vertex.getName());

				int index = myGraph.vertexList.indexOf(vertex);
				if (!theVisitedList[index] && (Double) edge.getData() >= theDeltaValue) {
					theVisitedList[index] = true;
					vertex.setData(edge.getFirstEndpoint());
					if (dfsOnGraph(vertex, theVisitedList, theDeltaValue))
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
		Vertex currentVertex = (Vertex) myGraph.vertexList.get(mySinkVertexIndex);
		Double bottleNeckValue = Double.MAX_VALUE;
		int edgeIndex;

		// find bottle neck of the augmenting path
		while (currentVertex != null) {
			previousVertex = (Vertex) currentVertex.getData();
			if (previousVertex != null) {
				edgeIndex = findEdgeIndex(previousVertex, currentVertex);
				Edge edge = (Edge) myGraph.edgeList.get(edgeIndex);
				bottleNeckValue = Math.min((double) bottleNeckValue, (double) edge.getData());
			}
			currentVertex = previousVertex;
		}

		// update all edges of the augmenting path.
		currentVertex = (Vertex) myGraph.vertexList.get(mySinkVertexIndex);
		while (currentVertex != null) {
			previousVertex = (Vertex) currentVertex.getData();
			if (previousVertex != null) {
				// update the forward flow
				edgeIndex = findEdgeIndex(previousVertex, currentVertex);
				Edge edge = (Edge) myGraph.edgeList.get(edgeIndex);
				edge.setData((Double) edge.getData() - bottleNeckValue);

				// update the backward flow
				edgeIndex = findEdgeIndex(currentVertex, previousVertex);
				edge = (Edge) myGraph.edgeList.get(edgeIndex);
				edge.setData((Double) edge.getData() + bottleNeckValue);
			}
			currentVertex = previousVertex;
		}
		return bottleNeckValue;
	}
}