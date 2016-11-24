
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@SuppressWarnings("serial")
public class ComponentImp extends UnicastRemoteObject implements Component, Runnable{
	
	int proc; //ID of the process
	int numComp; // the number of components
	int[] reqNum; 
	int[] states;
	Token t;
	Registry registry;
	
	
	public ComponentImp(Registry r, int proc, int numComp)throws RemoteException{
		registry = r;
		this.proc = proc;
		this.numComp = numComp;
		
		this.reqNum = new int[numComp];
		this.states = new int[numComp];
		
		if (proc == 0){
			this.t = new Token(numComp);
		}else {
			this.t = null;
		}
		
		if(proc == 0){
			states[0] = States.HOLDING;
		}
		else{
			for (int i = 0; i < numComp; i++){
				if (i < proc){
					states[i] = States.REQUESTING;
				}else{
					states[i] = States.OTHER;
				}
				reqNum[i] = 0;
			}
		}
		
		
		
		
		
	}


	@Override
	public void run() {
		
		System.out.println("This is process " + proc + ", Thread name: " + Thread.currentThread().getName());
		// TODO Auto-generated method stub
		
	}


	@Override
	public void receiveRequest(int reqNum, int oriID) throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void receiveToken(Token t) throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void request() throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub
		
	}

}
