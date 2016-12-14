
public class Edge implements Comparable<Edge>{
	

	private int desID;
	
	private int weight;
	
	private int state;
	
	public Edge(int desID, int weight){
		this.desID = desID;
		this.weight = weight;
		this.state = EdgeStates.UNKNOWN_IN_MST;		
	}
	
	public int getDesID() {
		return desID;
	}

	public int getWeight() {
		return weight;
	}

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public Edge smallestEdge(Edge o){
		return (this.weight < o.getWeight())? this : o;
	}
	

	@Override
	public int compareTo(Edge o) {
		return (this.weight < o.getWeight()) ? -1 : 1;
	}

	@Override
	public String toString() {
		return "Edge [desID=" + desID + ", weight=" + weight + ", state=" + state + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Edge))
			return false;
		Edge other = (Edge) obj;
		if (desID != other.desID)
			return false;
		if (state != other.state)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}




	
}
