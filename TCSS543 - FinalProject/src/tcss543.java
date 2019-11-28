/**
 * This is the main class.
 * 
 * TCSS543 - Autumn 2019
 * The Final Project: Max Flow
 * 
 * @author Thuan Lam, Asmita, and Deepthi
 * @date 11-23-2019
 */
public class tcss543 {
	/**
	 * The main function.
	 * This function firstly checks the first argument if it is a text file name or not.
	 * Then try to open the given text file name. 
	 * 
	 * @param args the list of arguments. We need at least 1 parameter that is the input file name. 
	 */
	public static void main(String[] args) {		
		if (args.length == 0) 
			System.out.println("Error: Input file needed");
		else if (!args[0].contains(".txt")) 
			System.out.println("Error: Input file must be a text .txt file");	
		else 
	        runAlgorithms(args[0]);
	}
		
	
	/**
	 * The function that actually runs all algorithms.
	 * @param theInputFileName the input file name.
	 */
    public static void runAlgorithms(String theInputFileName)  {
    	
        int NUMBER_OF_RUNNINGS = 1; //<<===== <<===== <<=====
        
        //Ford-Fulkerson
        double fordFulkersonMaxflow = 0.0;
        long totalRunningTime = 0;
        for (int i = 0; i < NUMBER_OF_RUNNINGS; i++) {
			SimpleGraph graph = new SimpleGraph();
			GraphInput.LoadSimpleGraph(graph, theInputFileName);
     			
            FordFulkerson fordFulkerson = new FordFulkerson(graph);

            long startTime = System.nanoTime();
            fordFulkersonMaxflow = fordFulkerson.getMaxFlow();
            long endTime = System.nanoTime();
            totalRunningTime += (endTime - startTime) / 1000000;
        }
        System.out.println("Ford Fulkerson took: " + totalRunningTime + " ms.");
        
     
        //Capability Scaling
        double capabilityScalingMaxFlow = 0.0;
        totalRunningTime = 0;      
        for (int i = 0; i < NUMBER_OF_RUNNINGS; i++) {
			SimpleGraph graph = new SimpleGraph();
			GraphInput.LoadSimpleGraph(graph, theInputFileName);
			
            CapabilityScalingFordFulkerson capabilityScaling = new CapabilityScalingFordFulkerson(graph);
            
            long startTime = System.nanoTime();
            capabilityScalingMaxFlow = capabilityScaling.getMaxFlow();
            long endTime = System.nanoTime();
            totalRunningTime += (endTime - startTime) / 1000000;
        }
        System.out.println("Scaling Ford Fulkerson took: " + totalRunningTime + " ms");

        
        //Preflow Push
        double preflowPushMaxFlow = 0.0;
//        totalRunningTime = 0;      
//        for (int i = 0; i < NUMBER_OF_RUNNINGS; i++) {
//			SimpleGraph graph = new SimpleGraph();
//			graph = GraphInput.LoadSimpleGraph(graph, theInputFileName);
//			
//			PreflowPush preflow = new PreflowPush(graph);
//            
//            long startTime = System.nanoTime();
//            preflowPushMaxFlow = preflow.runPreflow();
//            long endTime = System.nanoTime();
//            totalRunningTime += (endTime - startTime) / 1000000;
//        }
//        System.out.println("Preflow Push took: " + totalRunningTime + " ms");        
        
        System.out.println("");
        System.out.println("Ford-Fulkerson Maximum Flow: " + fordFulkersonMaxflow);
        System.out.println("Scaling Ford-Fulkerson Maximum Flow: " + capabilityScalingMaxFlow);
        System.out.println("Preflow Push Maximum Flow: " + preflowPushMaxFlow);
    }
}
