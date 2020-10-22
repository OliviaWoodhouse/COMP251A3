package A3;
public class BellmanFord{

	
	/**
	 * Utility class. Don't use.
	 */
	public class BellmanFordException extends Exception{
		private static final long serialVersionUID = -4302041380938489291L;
		public BellmanFordException() {super();}
		public BellmanFordException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 * 
	 * Use this to specify a negative cycle has been found 
	 */
	public class NegativeWeightException extends BellmanFordException{
		private static final long serialVersionUID = -7144618211100573822L;
		public NegativeWeightException() {super();}
		public NegativeWeightException(String message) {
			super(message);
		}
	}
	
	/**
	 * Custom exception class for BellmanFord algorithm
	 *
	 * Use this to specify that a path does not exist
	 */
	public class PathDoesNotExistException extends BellmanFordException{
		private static final long serialVersionUID = 547323414762935276L;
		public PathDoesNotExistException() { super();} 
		public PathDoesNotExistException(String message) {
			super(message);
		}
	}
	
    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    BellmanFord(WGraph g, int source) throws BellmanFordException{
        /* Constructor, input a graph and a source
         * Computes the Bellman Ford algorithm to populate the
         * attributes 
         *  distances - at position "n" the distance of node "n" to the source is kept
         *  predecessors - at position "n" the predecessor of node "n" on the path
         *                 to the source is kept
         *  source - the source node
         *
         *  If the node is not reachable from the source, the
         *  distance value must be Integer.MAX_VALUE
         *  
         *  When throwing an exception, choose an appropriate one from the ones given above
         */
        
        /* YOUR CODE GOES HERE */
    		this.distances = new int[g.getNbNodes()];
    		this.predecessors = new int[g.getNbNodes()];
    		for (int i=0;i<g.getNbNodes();i++) {
    			this.distances[i] = Integer.MAX_VALUE;
    		}
    		this.distances[source] = 0;
    		//relaxes all edges
    		for (int i=1;i<g.getNbNodes();i++) {
    			for (int j=0;j<g.getEdges().size();j++) {
    				Integer node1 = g.getEdges().get(j).nodes[0];
    				Integer node2 = g.getEdges().get(j).nodes[1];
    				int weight = g.getEdges().get(j).weight;
    				if (this.distances[node1]!=Integer.MAX_VALUE && (this.distances[node1]+weight)<this.distances[node2]) {
    					this.distances[node2]=this.distances[node1]+weight;
    					this.predecessors[node2] = node1;
    				}
    			}
    		}
    		//checks if there is a negative-weight cycle in the graph
    		for (int i=0;i<g.getEdges().size();i++) {
    			Integer node1 = g.getEdges().get(i).nodes[0];
    			Integer node2 = g.getEdges().get(i).nodes[1];
    			int weight = g.getEdges().get(i).weight;
    			if (this.distances[node1]!=Integer.MAX_VALUE && (this.distances[node1]+weight)<this.distances[node2]) {
    				throw new NegativeWeightException();
    			}
    		}
    }

    public int[] shortestPath(int destination) throws BellmanFordException{
        /*Returns the list of nodes along the shortest path from 
         * the object source to the input destination
         * If not path exists an Exception is thrown
         * Choose appropriate Exception from the ones given 
         */

        /* YOUR CODE GOES HERE (update the return statement as well!) */
        int source = this.source;
        int[] tempPath = new int[distances.length];
        int iterator = 0;
        tempPath[iterator] = destination;
        iterator++;
        int currPredecessor = this.predecessors[destination];
        while (currPredecessor!=source&&iterator<distances.length) {
        	 tempPath[iterator] = currPredecessor;
        	 currPredecessor = this.predecessors[currPredecessor];
        	 iterator++;
        }
        if (currPredecessor!=source) {
        		throw new PathDoesNotExistException();
        }
        tempPath[iterator] = currPredecessor;
        //let iterator+1=size of path
        int[] finalPath = new int[iterator+1];
        //reverses the list and stores it into finalPath
        for (int i=0;i<finalPath.length;i++) {
        		finalPath[i] = tempPath[iterator];
        		iterator--;
        }
        return finalPath;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        try{
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        }
        catch (BellmanFordException e){
            System.out.println(e);
        }

   } 
}
