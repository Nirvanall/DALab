
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

@SuppressWarnings("serial")
public class ComponentImp extends UnicastRemoteObject implements Component, Runnable{
	
	private int proc; //ID of the process
	private int numComp; // the number of components
	private int[] reqNum; 
	private int[] states;
	private Token t;
	Registry registry;
	
	
	public ComponentImp(Registry r, int proc, int numComp)throws RemoteException{
		registry = r;
		this.proc = proc;
		this.numComp = numComp;
		
		this.reqNum = new int[numComp];
		this.states = new int[numComp];
		
		//Initiate
		if (proc == 0){
			this.t = new Token(numComp);
		}else {
			this.t = null;
		}
		
		for (int i = 0; i < numComp; i++){
				if (i < proc){
					states[i] = States.REQUESTING;
				}else{
					states[i] = States.OTHER;
				}
				reqNum[i] = 0;
			}
		
		if(proc == 0){
			states[0] = States.HOLDING;
		}

	}


	@Override
	public void run() {
		
		System.out.println("This is process " + (proc+1) + ", Thread name: " + Thread.currentThread().getName());
		System.out.println("@Thread " + (proc+1) + " ---- Local states " + Arrays.toString(states) );
		
		try{
			Thread.sleep(1000);
			
			while(true){
				Thread.sleep((long) (1000*Math.random()));
				if ( states[proc]== States.OTHER || states[proc]==States.HOLDING )
				{
					this.request();
				}
			}
			
		}catch (InterruptedException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void receiveRequest(int reqNum, int oriID) throws RemoteException, NotBoundException, AccessException {
		
		this.reqNum[oriID] = reqNum;
		System.out.println("@Thread " + (proc+1) + " ---- Receiving request from Thread " + (oriID+1) );
		
		System.out.println("@Thread " + (proc+1) + " ---- current state " + states[proc]);
		switch (states[proc])
		{
			case States.EXECUTING: {
				states[oriID] = States.REQUESTING;
				System.out.println("@Thread " + (proc+1) + " ---- Local states " + Arrays.toString(states) );
				break;
			}
			case States.OTHER: {
				states[oriID] = States.REQUESTING; 
				System.out.println("@Thread " + (proc+1) + " ---- Local states " + Arrays.toString(states) );
				break;
			}
			case States.REQUESTING: {
				if (states[oriID] != States.REQUESTING)
				{
					states[oriID] = States.REQUESTING;
					System.out.println("@Thread " + (proc+1) + " ---- Local states " + Arrays.toString(states) );
					//reqNum[proc] = 
					System.out.println("@Thread " + (proc+1) + " ---- Sending request to Thread " + (oriID+1));
					ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(oriID));
					other.receiveRequest(this.reqNum[proc], proc);
					
				}
				break;}
			case States.HOLDING: {
				states[oriID] = States.REQUESTING;
				states[proc] = States.OTHER;
				t.states[oriID] = States.REQUESTING;
				t.reqNums[proc] = this.reqNum[proc];
				
				System.out.println("@Thread " + (proc+1) + " ---- Local states " + Arrays.toString(states) );
				
				ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(oriID));
				System.out.println("@Thread " + (proc+1) + " ---- Sending token to Thread " + (oriID+1));
				System.out.println("@Thread " + (proc+1) + " ---- State array in the token is " + Arrays.toString(this.t.states));
				other.receiveToken(this.t);
				this.t = null;
				
				
				break;} 
		}
		
	}


	@Override
	public void receiveToken(Token t) throws RemoteException, NotBoundException, AccessException {

			this.t = t;
			System.out.println("@Thread " + (proc+1) + " ---- Receiving a token");
			states[proc] = States.EXECUTING;
			System.out.println("@Thread " + (proc+1) + " ---- Executing critical section");
			
			synchronized(this) {
				try{	
					Thread.sleep((long) (2000*Math.random()));

				} catch(Exception e){
					e.printStackTrace();
				}
		
				System.out.println("@Thread " + (proc+1) + " ---- Critical section done");
			}
			
			states[proc] = States.OTHER;
			t.states[proc] = States.OTHER;
			
			System.out.println("@Thread " + (proc+1) + " ---- Local state array before: " + Arrays.toString(states));
			for (int i=0; i<numComp; i++){
				if ( reqNum[i]>=t.reqNums[i] )
				{
					t.reqNums[i] = reqNum[i];
					t.states[i] = states[i];
				}
				else 
				{
					reqNum[i] = t.reqNums[i];
					states[i] = t.states[i];
				}
			}
			
			System.out.println("@Thread " + (proc+1) + " ---- Local state array: " + Arrays.toString(states));
			
			boolean allEqual = true;
			for (int j=0; j<numComp; j++){
				if (states[j] != States.OTHER)
					{allEqual = false;
					break;}
			}

			if (allEqual){
				states[proc] = States.HOLDING;
				System.out.println("allEqual");
			}
			else {
				System.out.println("not allEqual");
				for (int i=1; i<numComp; i++){
					int temp = (proc+i) % numComp;
					if (states[temp] == States.REQUESTING){
						System.out.println("@Thread " + (proc+1) + " ---- Sending token to Thread " + (temp+1));
						
						ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(temp));
						other.receiveToken(this.t);
						this.t = null;
						
						break;
					}
					
				}
				
			}
		
		
	}


	@Override
	public void request() throws RemoteException, NotBoundException, AccessException {
		
		if (states[proc]==States.HOLDING ){
			this.receiveToken(t);
		}
		
		else{
			states[proc] = States.REQUESTING;
			reqNum[proc] = reqNum[proc] + 1;
			
			for (int i=0; i<numComp; i++)
			{
				if (states[i] == States.REQUESTING && i != proc)
				{
					System.out.println("@Thread " + (proc+1) + " ---- Sending request to Thread " + (i+1));
					ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(i));
					other.receiveRequest(reqNum[i], proc);
				}
			}
		}
		
	}

}
