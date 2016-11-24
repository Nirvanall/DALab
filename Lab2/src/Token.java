
public class Token {
	
	public int[] reqNums; //request numbers
	public int[] states;
	
	public Token(int numProc){
		reqNums = new int[numProc]; //the number of requests equals to the number of process
		states = new int [numProc]; //the number of states equals to the number of process
		
		// initialize request number and states for every process
		for(int i=0; i < numProc; i++){
			reqNums[i] = 0;
			states[i] = States.OTHER;
		}
		
	}
	
	public int getReqNum(int proc){
		return reqNums[proc];
	}
	
	public void setReqNum(int reqNum, int proc){
		this.reqNums[proc] = reqNum;
	}
	
	public int getState(int proc){
		return states[proc];
	}
	
	public void setState(int state, int proc){
		this.states[proc] = state;
	}

}
