package lab1.client;

import lab1.interf.*;
import lab1.server.InterfImp;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TestServer {
	
	
	public void runtest(){
		try{
			SESInterf p0 = (SESInterf)Naming.lookup("rmi://localhost:1099/0");
			SESInterf p1 = (SESInterf)Naming.lookup("rmi://localhost:1099/1");
			SESInterf p2 = (SESInterf)Naming.lookup("rmi://localhost:1099/2");
			
			p0.send("m1", 1, 2000);
			System.out.println("@(Process 0)---- sent first message to P1 with 2000 delay");
			p1.send("m2", 2, 0);
			System.out.println("@(Process 0)---- sent second message to P2 without delay");
			p2.send("m3", 1, 0);
			System.out.println("@(Process 2)---- sent third message  to P1 without delay");
			
			
		} catch (MalformedURLException e)  
        {  
            e.printStackTrace();  
        }  
        catch (RemoteException e)  
        {  
            e.printStackTrace();  
        }  
        catch (NotBoundException e)  
        {  
            e.printStackTrace();  
        }
		}




	public static void main(String[] args){
		
		TestServer ts = new TestServer();
		ts.runtest();
		
	
	}
}

