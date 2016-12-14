
public class Message implements Runnable{

	
	private int type;
	
	private Node destination;
	private int sender;
	
	private int otherLevel;
	private int bestWeight;
	private int otherName;
	private int otherState;
	
	
	public int getType() {
		return type;
	}


	public Node getDestination() {
		return destination;
	}


	public int getSender() {
		return sender;
	}


	public int getOtherLevel() {
		return otherLevel;
	}


	public int getBestWeight() {
		return bestWeight;
	}


	public int getOtherName() {
		return otherName;
	}


	public int getOtherState() {
		return otherState;
	}
	
	public Message(int sender, Node destination, int type){
		this.sender = sender;
		this.destination = destination;
		this.type = type;
	}
	
	public Message(int sender, Node destination, int type, int levelOrWeight){
		this.sender = sender;
		this.destination = destination;
		this.type = type;
		switch (type){
		case MessageTypes.CONNECT:
			this.otherLevel = levelOrWeight;
			break;
		case MessageTypes.REPORT:
			this.bestWeight = levelOrWeight;
			break;
		}
	}
	
	public Message(int sender, Node destination, int type, int otherLevel, int otherName){
		this.sender = sender;
		this.destination = destination;
		this.type = type;
		this.otherLevel = otherLevel;
		this.otherName = otherName;
	}
	
	public Message(int sender, Node destination, int type, int otherLevel, int otherName, int otherState){
		this.sender = sender;
		this.destination = destination;
		this.type = type;
		this.otherLevel = otherLevel;
		this.otherName = otherName;
		this.otherState = otherState;
	}


	@Override
	public void run() {
		try {
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	

}
