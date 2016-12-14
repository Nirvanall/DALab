import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeInterf extends Remote{
	
	public void wakeup() throws AccessException, RemoteException, NotBoundException;
	
	public void receiveConnect(int otherLevel, int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void receiveInitate(int otherLevel, int otherName, int otherState, int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void test() throws AccessException, RemoteException, NotBoundException;
	
	public void receiveTest(int otherLevel, int otherName, int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void receiveReject(int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void receiveAccept(int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void report() throws AccessException, RemoteException, NotBoundException;
	
	public void receiveReport(int otherWeightMOE, int sender) throws AccessException, RemoteException, NotBoundException;
	
	public void changeRoot() throws AccessException, RemoteException, NotBoundException;
	
	public void receiveChangeRoot(int sender) throws AccessException, RemoteException, NotBoundException;
	
	

}
