import java.util.*;

public class Vertex {
	private String point; // Every node has its own specific point number which is a string.
	private HashMap<Vertex, Integer> neighbourNodes = new HashMap<Vertex, Integer>(); // All neighbour nodes with their lengths are put into this hashmap.
	private boolean isFlag = false; // This shows whether this node has a flag or not.
	private int raceCost = 2147483647; // This is the cost of a node to find the shortest path.
	private int flagCost = 2147483647; // This is the cost of a node to find the flag path.
	
	public Vertex (String point) { // Nodes are created with their string point values only since the distinguishing feature of these nodes are their 
		this.point = point; // string point values.
	}
	
	// Below here, there are only getters and setters of the data fields since they are private.

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public HashMap<Vertex, Integer> getNeighbourNodes() {
		return neighbourNodes;
	}

	public void setNeighbourNodes(HashMap<Vertex, Integer> neighbourNodes) {
		this.neighbourNodes = neighbourNodes;
	}

	public boolean isFlag() {
		return isFlag;
	}

	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}

	public int getRaceCost() {
		return raceCost;
	}

	public void setRaceCost(int raceCost) {
		this.raceCost = raceCost;
	}
	
	public int getFlagCost() {
		return flagCost;
	}

	public void setFlagCost(int flagCost) {
		this.flagCost = flagCost;
	}
	
}
