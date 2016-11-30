
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

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
		
		try{
			
			while(true){
				Thread.sleep((long) (1000*Math.random()));
				if ( states[proc]== States.OTHER || states[proc]==States.HOLDING )
				{
					request();
				}
			}
			
		}catch (InterruptedException | RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void receiveRequest(int reqNum, int oriID) throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub
		
		this.reqNum[oriID] = reqNum;
		System.out.println("@Thread " + proc + " ---- Receiving request from Thread " + oriID );
		
		switch (states[proc])
		{
			case States.EXECUTING: states[oriID] = States.REQUESTING; break;
			case States.OTHER: states[oriID] = States.REQUESTING; break;
			case States.REQUESTING: {
				if (states[oriID] != States.REQUESTING)
				{
					states[oriID] = States.REQUESTING;
					//reqNum[proc] = 
					System.out.println("@Thread " + proc + " ---- Sending request to Thread " + oriID);
					ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(oriID));
					other.receiveRequest(this.reqNum[proc], proc);
					
				}
				break;}
			case States.HOLDING: {
				states[oriID] = States.REQUESTING;
				states[proc] = States.OTHER;
				t.states[oriID] = States.REQUESTING;
				t.reqNums[proc] = this.reqNum[proc];
				
				ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(oriID));
				System.out.println("@Thread " + proc + " ---- Sending token to Thread " + oriID);
				other.receiveToken(this.t);
				this.t = null;
				
				
				break;} 
		}
		
	}


	@Override
	public void receiveToken(Token t) throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub

		System.out.println("@Thread " + proc + " ---- Receiving a token");
		
		try{
			states[proc] = States.EXECUTING;
			
			System.out.println("@Thread " + proc + " ---- Executing critical session");
			Thread.sleep((long) (1000*Math.random()));
			System.out.println("@Thread " + proc + " ---- Critical session done");
			
			states[proc] = States.OTHER;
			t.states[proc] = States.OTHER;
			
			for (int i=0; i<numComp; i++){
				if ( reqNum[i]>t.reqNums[i] )
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
			
			if (states.equals(States.OTHER)){
				states[proc] = States.HOLDING;
			}
			else {
				for (int i=1; i<numComp; i++){
					int temp = (proc+i) % numComp;
					if (states[temp] == States.REQUESTING){
						System.out.println("@Thread " + proc + " ---- Sending token to Thread " + temp);
						ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(temp));
						other.receiveToken(this.t);
						this.t = null;
						
						break;
					}
					
				}
				
			}
		} catch(InterruptedException e){
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void request() throws RemoteException, NotBoundException, AccessException {
		// TODO Auto-generated method stub
		
		if (states[proc]==States.HOLDING ){
			receiveToken(t);
		}
		
		else{
			states[proc] = States.REQUESTING;
			reqNum[proc] = reqNum[proc] + 1;
			
			for (int i=0; i<numComp; i++)
			{
				if (states[i] == States.REQUESTING && i != proc)
				{
					ComponentImp other = (ComponentImp) registry.lookup(Integer.toString(i));
					other.receiveRequest(reqNum[i], proc);
				}
			}
		}
		
	}

}
