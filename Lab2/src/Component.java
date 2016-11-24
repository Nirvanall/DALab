
import java.rmi.Remote;
import java.rmi.NotBoundException;
import java.rmi.AccessException;
import java.rmi.RemoteException;

/**
 * Interface for Component Implementation
 * @author Lu Liu, Lichen Yao
 *
 */
public interface Component extends Remote{
	
	
	//public void run();
	
	
	/**
	 * 
	 * @param reqNum: requests number
	 * @param oriID: origin ID
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws AccessException
	 */
	public void receiveRequest(int reqNum, int oriID) throws RemoteException, NotBoundException, AccessException;
	
	
	/**
	 * 
	 * @param t
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws AccessException
	 */
	public void receiveToken(Token t)throws RemoteException, NotBoundException, AccessException;
	
	
	/**
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws AccessException
	 */
	public void request() throws RemoteException, NotBoundException, AccessException;
	

}
