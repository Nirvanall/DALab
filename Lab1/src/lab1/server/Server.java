package lab1.server;

import java.rmi.Naming;   
import java.rmi.registry.LocateRegistry; 
import lab1.interf.*;

public class Server {

	public static void main(String args[]) throws Exception
	{
		try
		{
			LocateRegistry.createRegistry(1099);
			InterfImp ses = new InterfImp();
		
			Naming.bind("rmi://localhost:1099/01", ses);
		
		} catch(Exception e) {System.out.println(e);}
	}
	
}
