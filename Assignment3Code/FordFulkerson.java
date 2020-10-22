package A3;
import java.io.*;
import java.util.*;




public class FordFulkerson {

	
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> Stack = new ArrayList<Integer>();
		// YOUR CODE GOES HERE
		ArrayList<Boolean> visited = new ArrayList<Boolean>();
		int[] predecessors = new int[graph.getNbNodes()];
		predecessors[source] = source;
		for (int i=0;i<graph.getNbNodes();i++) {
			visited.add(false);
		}
		Stack<Integer> tempStack = new Stack<>();
		tempStack.push(source);
		while (tempStack.empty()==false) {
			source = tempStack.peek();
			tempStack.pop();
			if (source==destination) {
				Stack.add(source);
				break;
			}
			//if not yet visited
			if (visited.get(source)==false) {
				Stack.add(source);
				visited.set(source, true);
			}
			ArrayList<Integer> adjList = adjacencyList(source, graph);
			for (int i=0;i<adjList.size();i++) {
				int temp = adjList.get(i);
				if (!(visited.get(temp))&&!tempStack.contains(temp)) {
					tempStack.push(temp);
					predecessors[temp]=source;
				}
			}
		}
		//check that path is correct because sometimes Stack can go a bit wonky
		int i = Stack.size()-1;
		while (i>0) {
			if (predecessors[Stack.get(i)]!=Stack.get(i-1)) {
				int iterator = i-1;
				while (Stack.get(iterator)!=predecessors[Stack.get(i)]) {
					Stack.remove(Stack.get(iterator));
					iterator--;
					i--;
				}
				i = iterator;
			} else {
				i--;
			}
		}
		//if destination was not reached must return an empty ArrayList
		if (!Stack.contains(destination)) {
			Stack.clear();
		}
		return Stack;
	}
	
	
	
	public static void fordfulkerson(Integer source, Integer destination, WGraph graph, String filePath){
		String answer="";
		String myMcGillID = "260734701"; //Please initialize this variable with your McGill ID
		int maxFlow = 0;
		// YOUR CODE GOES HERE
		//if there is no path from source to destination maxFlow=-1
		ArrayList<Integer> path = pathDFS(source, destination, graph);
		if (path.isEmpty()) {
			maxFlow = -1;
		} else {
			//copy contents into a residual graph
			WGraph rGraph = new WGraph(graph);
			//creates empty back edges for each forward edge if a back edge does not already exist for a pair of nodes
			for (int i=0;i<graph.listOfEdgesSorted().size();i++) {
				Edge edgeToCopy = graph.getEdges().get(i);
				Edge backEdge = new Edge(edgeToCopy.nodes[1], edgeToCopy.nodes[0],0);
				if (rGraph.getEdge(edgeToCopy.nodes[1], edgeToCopy.nodes[0])==null) {
					rGraph.addEdge(backEdge);
				}
			}
			//sets all weights in original graph to 0
			for (int i=0;i<graph.listOfEdgesSorted().size();i++) {
				graph.getEdges().get(i).weight = 0;
			}
			path = pathDFS(source, destination, rGraph);
			while (isAugmented(path)) {
				//finds bottleneckValue
				int bottleneckValue = Integer.MAX_VALUE;
				for (int i=0;i<path.size()-1;i++) {
					Integer node1 = path.get(i);
					Integer node2 = path.get(i+1);
					Edge tempEdge = rGraph.getEdge(node1, node2);
					int weight = tempEdge.weight;
					if (weight<bottleneckValue) {
						bottleneckValue = weight;
					}
				}
				//updates graphs
				for (int i=0;i<path.size()-1;i++) {
					Integer node1 = path.get(i);
					Integer node2 = path.get(i+1);
					int rfWeight = rGraph.getEdge(node1, node2).weight - bottleneckValue;
					int rbWeight = rGraph.getEdge(node2, node1).weight + bottleneckValue;
					//if edge does not exist in original graph...
					if (graph.getEdge(node1, node2)==null) {
						int gWeight = graph.getEdge(node2, node1).weight - bottleneckValue;
						graph.getEdge(node2, node1).weight = gWeight;
					} else {
						int gWeight = graph.getEdge(node1, node2).weight + bottleneckValue;
						graph.getEdge(node1, node2).weight = gWeight;
					}
					rGraph.getEdge(node1, node2).weight = rfWeight;
					rGraph.getEdge(node2, node1).weight = rbWeight;
				}
				//updates maxFlow and finds a new path
				maxFlow+=bottleneckValue;
				path = pathDFS(source, destination, rGraph);
				path = pathDFS(source, destination, rGraph);
			}
		}
		answer += maxFlow + "\n" + graph.toString();	
		writeAnswer(filePath+myMcGillID+".txt",answer);
		System.out.println(answer);
	}
	
	
	public static void writeAnswer(String path, String line){
		BufferedReader br = null;
		File file = new File(path);
		// if file doesnt exists, then create it
		
		try {
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line+"\n");	
		bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	 public static void main(String[] args){
		 String file = args[0];
		 File f = new File(file);
		 WGraph g = new WGraph(file);
		 fordfulkerson(g.getSource(),g.getDestination(),g,f.getAbsolutePath().replace(".txt",""));
	 }
	 
	 //Helper method to check for a current node's adjacency list
	 public static ArrayList<Integer> adjacencyList(Integer n, WGraph graph) {
		 ArrayList<Integer> adjList = new ArrayList<Integer>();
		 for (int i=0;i<graph.getNbNodes();i++) {
			 if (graph.getEdge(n,i)!=null&&graph.getEdge(n,i).weight!=0) {
				 adjList.add(i);
			 }
		 }
		 return adjList;
	 }
	 
	 //Helper method to determine if a path is an augmenting path
	 public static boolean isAugmented(ArrayList<Integer> path) {
		 if (path.isEmpty()) {
			 return false;
		 } else {
			 return true;
		 }
	 }
}
