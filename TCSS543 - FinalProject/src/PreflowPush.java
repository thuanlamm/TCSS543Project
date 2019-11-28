///**
// * @author: Ishan Gupta
// * @author: Ali Nemati
// * @description: A class to compute maximum flow using Preflow Push Algorithm.
// */
//
//// Importing collections and iterator.
//import java.util.*;
//import java.util.Iterator;
//
//
///**
// * Preflow-Push Algorithm Implementation.
// * 
// * @author Thuan Lam
// * @date 11-25-2019
// */
//public final class PreflowPush {
//    /** The given graph that needs to calculate the max flow value. */
//    private SimpleGraph myGraph;	
//    /** The array that stores the high level of vertices. */
//	private double[] myHigh;
//    /** The array that stores the excess flow. */
//	private double[] myExcess;
//	/** The index of the source vertex in the list of vertices. */
//	private int mySourceVertexIndex = -1;
//	/** The index of the sink vertex in the list of vertices. */
//    private int mySinkVertexIndex = -1;
//	/** The name of the source vertex. s by default */
//	private String mySourceVertexName = "s";
//	/** The name of the sink vertex. t by default*/
//	private String mySinkVertexName = "t";
//    Double maxflow = (double) 0;
//    
//	/**
//	 * The constructor of the Preflow Push class.
//	 * 
//	 * @param theGraph the input graph that need to calculate max flow.
//	 */
//    public PreflowPush(SimpleGraph theGraph) {
//    	this.myGraph = theGraph;
//    	this.myHigh = new double[this.myGraph.numVertices()];
//    	this.myExcess = new double[this.myGraph.numVertices()];
//    	this.mySourceVertexIndex = findVertexIndexByName(this.mySourceVertexName);
//    	this.mySinkVertexIndex = findVertexIndexByName(this.mySinkVertexName);
//    }
//    
//	/**
//	 * Get the index of the vertex by the vertex's name.
//	 * 
//	 * @param theVertexName a string that is the name of a vertex.
//	 * @return the index of the vertex in the list of vertices. Return -1 if not found.
//	 */
//	private int findVertexIndexByName(String theVertexName) {
//		int index = 0;
//		for (Iterator<Vertex> ite = myGraph.vertices(); ite.hasNext(); index++) {
//			Vertex vertex = ite.next();
//			if (vertex.getName().toString().equals(theVertexName)) {
//				return index;
//			}
//		}
//		return -1;
//	}
//   
//	/**
//	 * Get the index of the given edge from the list of edges.
//	 * 
//	 * @param theStartVertex the start vertex.
//	 * @param theEndVertex   the end vertex.
//	 * @return the index of the edge that connects 2 given vertices if found.
//	 *         Otherwise, return -1.
//	 */
//	private int findEdgeIndex(Vertex theStartVertex, Vertex theEndVertex) {
//		for (Iterator<Edge> ite = myGraph.incidentEdges(theStartVertex); ite.hasNext();) {
//			Edge edge = ite.next();
//			if (edge.getSecondEndpoint() == theEndVertex)
//				return myGraph.edgeList.indexOf(edge);
//		}
//		return -1;
//	}
//	
//    /**
//     * Method to rename node to the current flow value.
//     * @param theVertexIndex
//     */
//    public void relabelVertex(int theVertexIndex)
//    {
//        Vertex vertex = (Vertex) myGraph.vertexList.get(theVertexIndex);
//        vertex.setData((double)vertex.getData() + 1);
//    }
//
//    /**
//     * Method to recalculate excess flow and push the excess flow.
//     * @param excess
//     * @param capital
//     * @param indexOne
//     * @param indexTwo
//     * @param indexThree
//     * @param indexFour
//     */
//    public void pushFlow(double excess, double capital, int indexOne, int indexTwo, int indexThree, int indexFour)
//    {
//        Double hold = Math.min(excess, capital);
//
//        Edge edgeOne = (Edge) myGraph.edgeList.get(indexOne);
//        Edge edgeTwo = (Edge) myGraph.edgeList.get(indexTwo);
//
//        edgeOne.setName((Double)edgeOne.getName() - hold);
//        edgeTwo.setName((Double)edgeTwo.getName() + hold);
//
//        myExcess[indexThree] = myExcess[indexThree] - hold;
//        myExcess[indexFour] = myExcess[indexFour] + hold;
//    }
//
//    /**
//     * Method to set the flow in the network.
//     *
//     */
//    public void setFlows()
//    {
//        Vertex start, end;                          // End points of edge, vertices.
//        Edge edgeOne, edgeTwo;                      // edges incoming and outgoing
//        Iterator<Vertex> vertex;                    // Vertex iterator
//        Iterator<Edge> edgeOneIt;                   // Edge iterator
//        double hold;                                // holder variable for values of flow
//        int index;                                  // index for vertices.
//
//        /*
//        Name the edges with the data/their value.
//         */
//        for (vertex = myGraph.vertices(); vertex.hasNext(); ) {
//
//            start =  vertex.next();
//
//            for (edgeOneIt = myGraph.incidentEdges(start); edgeOneIt.hasNext();) {
//                edgeOne =  edgeOneIt.next();
//                edgeOne.setName(edgeOne.getData());
//            }
//        }
//
//        /*
//        Calculate and set the flow value in the graph.
//         */
//        for (edgeOneIt = myGraph.incidentEdges((Vertex) myGraph.vertexList.getFirst()); edgeOneIt.hasNext();) {
//
//            edgeOne = edgeOneIt.next();
//            hold = (Double)edgeOne.getName();
//
//            edgeOne.setName((double) 0);
//            end = edgeOne.getSecondEndpoint();
//
//            index = findEdgeIndex(end, (Vertex) myGraph.vertexList.getFirst());
//            edgeTwo = (Edge) myGraph.edgeList.get(index);
//
//            edgeTwo.setName(hold);
//        }
//    }
//
//    /**
//     * Method to calculate and set excess flow in the graph
//     */
//    public void setSurplus()
//    {
//        double hold = new Double(0);                // temporary holder variable
//        Iterator<Edge> edgeIt;                      // edge iterator
//        Edge edge;                                  // edge variable
//        Vertex vertex;                              // vertex variable
//
//        /*
//        Calculate excess flow for edges.
//         */
//        for (edgeIt = myGraph.incidentEdges((Vertex) myGraph.vertexList.getFirst()); edgeIt.hasNext();) {
//            edge = edgeIt.next();
//            vertex = edge.getSecondEndpoint();
//
//            int index = myGraph.vertexList.indexOf(vertex);
//            myExcess[index] = (Double)edge.getData();
//
//            hold += (Double)edge.getData();
//        }
//
//        myExcess[0] = -hold;
//    }
//	
//    /**
//     * Main method to calculate maximum flow in a network
//     * using Preflow Push Algorithm.
//     * @param input graph
//     * @return maximum flow.
//     */
//    public double runPreflow(){
//        
//                                    // network/graph as the input.
//        Iterator<Vertex> vertexOne, vertexTwo;              // vertex iterators
//        Iterator<Edge> edgeOne, edgeTwo;                    // edge iterators
//        Vertex start, end, vertex;                          // vertices variables
//        Edge edgeStart, edgeEnd;                            // edge variables
//        int indexOne = 0, indexTwo = -10, counter = 0;      // index variables; counter
//        boolean flag = false;                               // checker variable to find end
//
//       /*
//        start by setting labels to zero to suplus values.
//         */
//        for(vertexTwo = myGraph.vertices(); vertexTwo.hasNext(); )
//        {
//            start = vertexTwo.next();
//            start.setData((double) 0);
//            myExcess[indexOne] = 0;
//            indexOne++;
//        }
//
//        indexOne = 0;
//        start = (Vertex) myGraph.vertexList.getFirst();
//        start.setData((double) myGraph.numVertices());
//        setFlows();
//        setSurplus();
//
//        /*
//        Till counter is not zero, calculate the excess flow.
//         */
//        while(true)
//        {
//            /*
//            Till sink is not reached, update indices for calculation of
//            excess flow.
//             */
//            for (vertexOne = myGraph.vertices(); vertexOne.hasNext(); ) {
//                vertex = vertexOne.next();
//                if(myExcess[indexOne] > 0 && !(vertex.getName().equals("t")))
//                {
//                    start = vertex;
//                    indexTwo = indexOne;
//                    counter++;
//                    break;
//                }
//                indexOne++;
//            }
//
//            indexOne = 0;
//
//            if(counter == 0)
//                break;
//
//            counter = 0;
//
//            /*
//            Push the excess flow back in the network following all the edges.
//             */
//            for (edgeOne = myGraph.incidentEdges(start); edgeOne.hasNext();) {
//                edgeStart = edgeOne.next();
//                end = edgeStart.getSecondEndpoint();
//                if((Double)edgeStart.getName() > 0 && (Double)start.getData() > (Double)end.getData())
//                {
//                    int indexThree = findEdgeIndex(end, start);
//                    pushFlow(myExcess[indexTwo], (Double)edgeStart.getName(), myGraph.edgeList.indexOf(edgeStart),
//                            indexThree,indexTwo,myGraph.vertexList.indexOf(end));
//                    flag = true;
//                    break;
//                }
//            }
//            if(!flag)
//                relabelVertex(myGraph.vertexList.indexOf(start));
//
//            flag = false;
//        }
//
//        /*
//        Calculate maxflow by getting all edges values.
//         */
//        for (edgeTwo = myGraph.incidentEdges((Vertex) myGraph.vertexList.get(mySinkVertexIndex)); edgeTwo.hasNext();) {
//            edgeEnd = edgeTwo.next();
//            maxflow = maxflow + (Double)edgeEnd.getName();
//        }
//        
//        return maxflow; //return maxflow value for the network.
//    }
//}
