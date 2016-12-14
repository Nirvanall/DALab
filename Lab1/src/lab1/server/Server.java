package lab1.server;

import java.rmi.registry.Registry;
/**
 * This is the server side of our system
 * @author Lu Liu , Lichen Yao
 * When testing, the server side should be run first
 */

public class Server {
	
	Registry registry;
	
	public void startServer() {
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			InterfImp process0 = new InterfImp(0,3);
			InterfImp process1 = new InterfImp(1,3);
			InterfImp process2 = new InterfImp(2,3);
			
			java.rmi.Naming.bind("rmi://localhost:1099/0", process0);
			java.rmi.Naming.bind("rmi://localhost:1099/1", process1);
			java.rmi.Naming.bind("rmi://localhost:1099/2", process2);
			
					
		} catch (Exception e){
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args){
		Server s = new Server();
		s.startServer();
		System.out.println("@Server----RMI SES server started and provide service now...");
		
	}

}
