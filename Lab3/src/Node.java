import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Node extends UnicastRemoteObject implements NodeInterf, Runnable{

	/**
	 * 
	 */
	//private static final long serialVersionUID = -5298498477088809710L;
	
	Registry registry;
	
	public int nodeID;
	public int state;
	
	public int fragmentName;
	public int fragmentLevel;
	
	public int merged;
	public int absorbed;
	public int connectMessages;
	public int initiateMessages;
	public int testMessages;
	public int acceptMessages;
	public int rejectMessages;
	public int reportMessages;
	public int changeRootMessages;
	
	public Edge bestEgde;
	public int bestWeight;
		
	private Edge testEdge;
	private Edge inBranch;
	
	public ArrayList<Edge> adjacentEdges;
	public ArrayList<Message> messageQ;
	
	public int findCount;
	
	public Node(Registry registry, int id, ArrayList<Edge> adjEdges) throws RemoteException{
		this.registry = registry;
		this.nodeID = id;
		this.state = NodeStates.SLEEPING;
		this.fragmentName = 0;
		this.adjacentEdges = adjEdges;
		this.messageQ = new ArrayList<Message>();
	}
	
	public void putMessage(Message m){
		messageQ.add(m);	
	}
	
	public void testingInit(){
		merged = 0;
		absorbed = 0;
		connectMessages = 0;
		initiateMessages = 0;
		testMessages = 0;
		acceptMessages = 0;
		rejectMessages = 0;
		reportMessages = 0;
		changeRootMessages = 0;		
	}

	
	public int getNodeID() {
		return nodeID;
	}
		
	public ArrayList<Edge> getAdjacentEdges() {
		return adjacentEdges;
	}
	
	public synchronized void setState(int state){
		this.state = state;
	}
	
	private Edge getEdge(int sender){
		for(Edge e : adjacentEdges){
			if(e.getDesID() == sender){
				return e;
			}
		}
		return null;
	}


	public int getFragmentName() {
		return fragmentName;
	}

	public int getFragmentLevel() {
		return fragmentLevel;
	}

	public int getMerged() {
		return merged;
	}

	public int getAbsorbed() {
		return absorbed;
	}

	public int getConnectMessages() {
		return connectMessages;
	}

	public int getInitiateMessages() {
		return initiateMessages;
	}

	public int getTestMessages() {
		return testMessages;
	}

	public int getAcceptMessages() {
		return acceptMessages;
	}

	public int getRejectMessages() {
		return rejectMessages;
	}

	public int getReportMessages() {
		return reportMessages;
	}

	public int getChangeRootMessages() {
		return changeRootMessages;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public synchronized void checkQ() throws AccessException, RemoteException, NotBoundException {
		//TODO
	}

	@Override
	public synchronized void wakeup() throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveConnect(int otherLevel, int sender) throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveInitate(int otherLevel, int otherName, int otherState, int sender)
			throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void test() throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveTest(int otherLevel, int otherName, int sender)
			throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveReject(int sender) throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveAccept(int sender) throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void report() throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveReport(int otherWeightMOE, int sender)
			throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void changeRoot() throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public synchronized void receiveChangeRoot(int sender) throws AccessException, RemoteException, NotBoundException {
		// TODO Auto-generated method stub
		
	}
	
	

}
