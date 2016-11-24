package lab1.client;

import lab1.interf.*;
import lab1.server.InterfImp;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;
import java.rmi.registry.LocateRegistry;

/**
 * This is the client side of our system
 * @author Lu Liu, Lichen Yao
 * There are four scenarios in our test
 *
 */

public class Client {
	
	/* Scenario 1: Normal scenario with no delay
	 *  Process0 sends Message1 to Process1
	 *  Process0 sends Message2 to Process1
	 *  m1 arrives before m2
	 */
	
	@Test
	public void normalTest(){
		InterfImp process0;
		InterfImp process1;
		try{
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			process0 = new InterfImp(0,2);
			process1 = new InterfImp(1,2);
			
			java.rmi.Naming.rebind("rmi://localhost:1099/0", process0);
			java.rmi.Naming.rebind("rmi://localhost:1099/1", process1);
			
			System.out.println("Normal Test Started");
			process0.send("m1", 1, 0);
			Thread.sleep(100); // ensure m1 received
			process0.send("m2", 1, 0);
			Thread.sleep(100); // ensure m2 received
			
			// The messages received at  P0 and P1
			ArrayList<String> messages0 = process0.getReceivedMessages();
			ArrayList<String> messages1 = process1.getReceivedMessages();
			
			// expected received messages at P0 and P1
			ArrayList<String> expected0 = new ArrayList<String>();
			ArrayList<String> expected1 = new ArrayList<String>();
			expected1.add("m1");
			expected1.add("m2");
			
			// to judge whether the messages received is the same the messages expected
			// if they are not same, the test will stop and return wrong message
			assertEquals("Messages at P0 are wrong", expected0, messages0);
			assertEquals("Messages at P1 are wrong", expected1, messages1);
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/* Scenario 2£º
	 * P0 sends M1 to P1
	 * P0 sends M2 to P1
	 * M2 arrives before M1
	 * 
	 */
	@Test
	public void reorderingTest(){
		InterfImp process0;
		InterfImp process1;
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			process0 = new InterfImp(0,2);
			process1 = new InterfImp(1,2);
			
			java.rmi.Naming.rebind("rmi://localhost:1099/0", process0);
			java.rmi.Naming.rebind("rmi://localhost:1099/1", process1);

			System.out.println("Reordering Test started");
			process0.send("m1", 1, 1000); // sends m1 to P1 with 1000 delay
			process0.send("m2", 1, 0);
			Thread.sleep(3000); //ensure all messages are received

			ArrayList<String> messages0 = process0.getReceivedMessages();
			ArrayList<String> messages1 = process1.getReceivedMessages();
			
			ArrayList<String> expected0 = new ArrayList<String>();
			ArrayList<String> expected1 = new ArrayList<String>();
			expected1.add("m1");
			expected1.add("m2");
			
			assertEquals("Messages at P0 are wrong", expected0,messages0);
			assertEquals("Messages at P1 are wrong", expected1,messages1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*Scenario 3:
	 * P0 sends m1 to P1
	 * P0 sends m2 to P2
	 * P2 sends m3 to P1
	 * m3 arrives before m1
	 * 
	 */
	@Test
	public void threePTest(){
		InterfImp process0;
		InterfImp process1;
		InterfImp process2;
		try {
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			
			process0 = new InterfImp(0, 3);
			process1 = new InterfImp(1, 3);
			process2 = new InterfImp(2, 3);
			
			java.rmi.Naming.rebind("rmi://localhost:1099/0", process0);
			java.rmi.Naming.rebind("rmi://localhost:1099/1", process1);
			java.rmi.Naming.rebind("rmi://localhost:1099/2", process2);
			
			System.out.println("Three processes Test started");

			process0.send("m1", 1, 1000); // sends m1 to P1 with 1000 delay
			process0.send("m2", 2, 0);
			process2.send("m3", 1, 0);
			Thread.sleep(3000); //ensure all messages received

			ArrayList<String> messages0 = process0.getReceivedMessages();
			ArrayList<String> messages1 = process1.getReceivedMessages();
			ArrayList<String> messages2 = process2.getReceivedMessages();
			
			ArrayList<String> expected0 = new ArrayList<String>();
			ArrayList<String> expected1 = new ArrayList<String>();
			expected1.add("m1");
			expected1.add("m3");
			ArrayList<String> expected2 = new ArrayList<String>();
			expected2.add("m2");
			
			assertEquals("Messages at P0 are wrong", expected0,messages0);
			assertEquals("Messages at P1 are wrong", expected1,messages1);
			assertEquals("Messages at P2 are wrong", expected2,messages2);
									
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

		/* Scenario 4:
		 * P0 sends m1 to P3
		 * P0 sends n1 to P1
		 * P1 sends m2 to P3
		 * P1 sends n2 to P2
		 * P2 sends m3 to P3
		 * n2 arrives at P2 before sending m3
		 * At P3, m3 arrives first, then m2, and finally m1
		 * 
		 */
		@Test
		public void fourPTest(){
			InterfImp process0;
			InterfImp process1;
			InterfImp process2;
			InterfImp process3;
			try{
				
				java.rmi.registry.LocateRegistry.createRegistry(1099);
				
				process0 = new InterfImp(0, 4);
				process1 = new InterfImp(1, 4);
				process2 = new InterfImp(2, 4);
				process3 = new InterfImp(3, 4);
				
				java.rmi.Naming.rebind("rmi://localhost:1099/0", process0);
				java.rmi.Naming.rebind("rmi://localhost:1099/1", process1);
				java.rmi.Naming.rebind("rmi://localhost:1099/2", process2);
				java.rmi.Naming.rebind("rmi://localhost:1099/3", process3);

				System.out.println("Four Processes Test started");
				
				process0.send("m1", 3, 1000); // sends m1 to P3 with 1000 delay
				process0.send("n1", 1, 0);
				process1.send("m2", 3, 500); // sends m2 to P3 with 500 delay
				process1.send("n2", 2, 0);
				Thread.sleep(1000); //ensure n2 arrives at p2 before sending m3
				process2.send("m3", 3, 0); // sends m3 to P3 without delay
							
				Thread.sleep(3000); //ensure all messages are received

				ArrayList<String> messages0 = process0.getReceivedMessages();
				ArrayList<String> messages1 = process1.getReceivedMessages();
				ArrayList<String> messages2 = process2.getReceivedMessages();
				ArrayList<String> messages3 = process3.getReceivedMessages();
				
				
				ArrayList<String> expected0 = new ArrayList<String>();
				ArrayList<String> expected1 = new ArrayList<String>();
				expected1.add("n1");
				ArrayList<String> expected2 = new ArrayList<String>();
				expected2.add("n1");
				ArrayList<String> expected3 = new ArrayList<String>();
				expected3.add("m1");
				expected3.add("m2");
				expected3.add("m3");
				
				assertEquals("Messages at P0 are wrong", expected0,messages0);
				assertEquals("Messages at P1 are wrong", expected1,messages1);
				assertEquals("Messages at P2 are wrong", expected2,messages2);
				assertEquals("Messages at P3 are wrong", expected3,messages3);

				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
								
}
	
			

