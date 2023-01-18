import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class project4 {	
	public static void main(String[] args) {
		class ShortestCompare implements Comparator <Vertex>{ // This is the comparator of the shortest path. It simply compares the race costs
			@Override // between two vertices (points).
			public int compare(Vertex o1, Vertex o2) {
				// TODO Auto-generated method stub
				return o1.getRaceCost() - o2.getRaceCost();
			}		
		}
		
		class FlagCompare implements Comparator <Vertex>{ // This is the comparator of the flag path. It simply compares the flag costs between 
			@Override // two vertices (points).
			public int compare(Vertex o1, Vertex o2) {
				// TODO Auto-generated method stub
				return o1.getFlagCost() - o2.getFlagCost();
			}		
		}
		try {
			File theFile = new File(args[0]); // The input file is taken as the first argument.
			Scanner theReader = new Scanner (theFile); // It will be read by the scanner.

			int whichLine = 1; // This is the line counter. Operations are done according to the line number.			
			int numberOfPoints = 0; // This is the total number of points in the graph. 
			int numberOfFlags = 0; // This is the number of flagged points in the graph.  
			HashMap<String, Vertex> everyNode = new HashMap<String, Vertex>(); // Nodes are hold in another hashmap.
			Vertex beginPoint = null; // Begin point is marked for any search in the graph.
			Vertex endPoint = null; // Similarly, end point is marked for any search in the graph.
			Vertex flagBegin = null;
			
			while (theReader.hasNextLine()) {
				String data = theReader.nextLine(); // This reveals that the input will be taken line by line.
				String[] splittedData = data.split("\\s+"); // Since the letters in the beginning and numbers between blanks
				// has its specific meaning, the data is splitted and each element between blanks are assigned to its variable.
			
				if (whichLine == 1)  // In the first line, the total number of points is given as a string.
					numberOfPoints = Integer.parseInt(splittedData[0]); // It is converted to an integer.
				
				else if (whichLine == 2) // In the second line, the number of flags is given. It will be necessary in the fourth line.
					numberOfFlags = Integer.parseInt(splittedData[0]);
				
				else if (whichLine == 3) { // In the third line, beginning and ending points are given. Their vertex objects are created and put
					beginPoint = new Vertex(splittedData[0]); // into the hashmap of all vertices.
					endPoint = new Vertex(splittedData[1]);
					everyNode.put(splittedData[0], beginPoint);
					everyNode.put(splittedData[1], endPoint);
				}
				
				else if (whichLine == 4) { // In the fourth line, the flagged points are given. The nodes are created if they have not already created
					for (int i = 0; i < numberOfFlags; i++) { // and their flagged situation is assigned to "true" by the boolean data field.
						Vertex node; // Then, the first flag is chosen as the beginning flag (it doesn't matter where to start).
						if (everyNode.containsKey(splittedData[i]))
							node = everyNode.get(splittedData[i]);
						else {
							node = new Vertex(splittedData[i]);
							everyNode.put(splittedData[i], node);
						}
						node.setFlag(true);
						if (i == 0)
							flagBegin = node;						
					}
				}
				
				else if (whichLine > 4) { // After the fourth line, the lengths between two adjacent nodes are given. The first node is said to be the
					Vertex parentNode; // parent node (imaginary), if its object has already created, it is get from the hashmap. If it is not created,
					if (everyNode.containsKey(splittedData[0])) // a new vertex object is created. Then, the other vertices is searched by a for loop
						parentNode = everyNode.get(splittedData[0]); // indices having odd numbers mean that it is a point name. Similar to the parent
					else {// search, if the hashmap includes the specific vertex object it is get. Otherwise, the object is created. Then, the with the
						parentNode = new Vertex(splittedData[0]); // distances between vertices, they are put in each verices' distance hashmap.
						everyNode.put(splittedData[0], parentNode);
					}
					for (int i = 1; i < splittedData.length ; i = i + 2) {
						Vertex adjacentNode;
						if (everyNode.containsKey(splittedData[i]))
							adjacentNode = everyNode.get(splittedData[i]);
						else {
							adjacentNode = new Vertex(splittedData[i]);
							everyNode.put(splittedData[i], adjacentNode);
						}
						parentNode.getNeighbourNodes().put(adjacentNode, Integer.parseInt(splittedData[i+1]));
						adjacentNode.getNeighbourNodes().put(parentNode, Integer.parseInt(splittedData[i+1]));
					}
				}			
				
				whichLine++; // At each line, this iterator is increased by one.
			}			
			theReader.close(); // After reading everything, the reader is closed.
			
			// This part finds the shortest path by Dijkstra's algorithm. the vertices are put in a priority queue to decrease complexity.
			// All visited nodes are stored in a set so that the program won't go there again.
			// The path length is set to -1 in case there is no path after searching all paths.
			// If the iterating node is the target node, the cost of is set as shortest path length and this part ends.
			// Every neighbouring node of the iterating node is searched, their costs are checked, then reset if necessary if they aren't visited.
			// At the end, all non-visited neighbouring nodes of the iterating node are added to the priority queue.
			PriorityQueue<Vertex> shortestHeap = new PriorityQueue<Vertex>(new ShortestCompare()); 
			HashSet<Vertex> visitedNodes = new HashSet<Vertex>();
			int shortestPathLength = -1;
			beginPoint.setRaceCost(0);
			shortestHeap.add(beginPoint);
			while (visitedNodes.size() < everyNode.size()) {				
				if (shortestHeap.isEmpty()) 
					break;
				Vertex iterNode = shortestHeap.poll();
				if (iterNode.equals(endPoint) && endPoint.getRaceCost() < 2147483647) {
					shortestPathLength = endPoint.getRaceCost();
					break;
				}
				if (visitedNodes.contains(iterNode))
					continue;
				visitedNodes.add(iterNode);
				for (Entry<Vertex, Integer> neighbours: iterNode.getNeighbourNodes().entrySet()) {
					Vertex neighbourNode = neighbours.getKey();
					int distance = neighbours.getValue();
					if (visitedNodes.contains(neighbourNode) == false) {
						int iterCost = iterNode.getRaceCost() + distance;
						if (iterCost < neighbourNode.getRaceCost()) 
							neighbourNode.setRaceCost(iterCost);						
						shortestHeap.add(neighbourNode);						
					}
				}
			}

			// This part is to find the flag path. It is very similar to the part above except some slight differences.
			// There is a counter called "flagPathLength which holds the path length.
			// There is another set holding the visited flags to not visit a flag for another time. 
			// The purpose is that visited nodes' set is cleared if there is a flag to return back to that non-flag node if necessary.
			// If the node is a flag, its cost is added to the counter, and set it to zero to find the shortest path to the other flags.
			// The neighbour search is the same as above.
			int flagPathLength = 0;
			HashSet<Vertex> visitedFlags = new HashSet<Vertex>();
			shortestHeap = new PriorityQueue<Vertex>(new FlagCompare());
			visitedNodes.clear();
			flagBegin.setFlagCost(0);
			shortestHeap.add(flagBegin);
			while (visitedFlags.size() < numberOfFlags) {
				if (shortestHeap.isEmpty()) {
					flagPathLength = -1;
					break;
				}
				Vertex iterNode = shortestHeap.poll();
				if (visitedNodes.contains(iterNode) || visitedFlags.contains(iterNode))
					continue; 
				visitedNodes.add(iterNode);
				if (iterNode.isFlag()) {
					flagPathLength += iterNode.getFlagCost();
					iterNode.setFlagCost(0);
					visitedFlags.add(iterNode);
					visitedNodes.clear();
				}
				for (Entry<Vertex, Integer> neighbours: iterNode.getNeighbourNodes().entrySet()) {
					Vertex neighbourNode = neighbours.getKey();
					int distance = neighbours.getValue();
					if (visitedNodes.contains(neighbourNode) == false) {
						int iterCost = iterNode.getFlagCost() + distance;
						if (iterCost < neighbourNode.getFlagCost()) 
							neighbourNode.setFlagCost(iterCost);
						shortestHeap.add(neighbourNode);					
					}
				}
			}			
			
			FileWriter theWriter = new FileWriter(args[1]); // Then, writer is taken as the second argument.
//			System.out.println(shortestPathLength);
//			System.out.println(flagPathLength);
			theWriter.write(Integer.toString(shortestPathLength) + "\n"); // Shortest path length is written and a new line is put.
			theWriter.write(Integer.toString(flagPathLength)); // Flag path is written and a new line is put.
			theWriter.close(); // After writing, the writer is closed.
		}
		catch (FileNotFoundException e){
			e.printStackTrace();  // This is called if there is a problem in reading. 
		}
		catch (IOException e) {
			e.printStackTrace(); // This is called if there is a problem in writing. 
		}
	}
}
