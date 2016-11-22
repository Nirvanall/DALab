package lab1.server;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import lab1.interf.SESInterf;
import lab1.clock.VectorClock;

@SuppressWarnings("serial")
public class InterfImp extends UnicastRemoteObject implements SESInterf{

	public int id, process_num;
	
	public InterfImp(int process_id, int process_number) throws RemoteException
	{
		id = process_id;
		process_num = process_number;
	}
	
	private VectorClock vector_clock;
	Map<Integer, VectorClock> clock_buffer;
	Map<VectorClock, String> message_buffer;
	
	public void send(String m, int destination_id, int delay) 
	{
		vector_clock.increment_clock(id); //Increment own clock
		
		try
		{
			SESInterf sendto = (SESInterf) Naming.lookup("rmi://localhost:1099/" + destination_id);
			System.out.println("Sending message " + m + "to " + destination_id);
			
			sendto.pre_delivery_check(m, clock_buffer, vector_clock);
			clock_buffer.put(destination_id, vector_clock);
			
		} catch (Exception e) {System.out.println(e);}
		
	}
	
	//Only check if the input message contains a delay.
	//If so, implement the delay, then proceed to receive.
	public void delay_check(String m, Map<Integer, VectorClock> S, VectorClock V, int time_delay)
	{
		try
		{
			if (time_delay != 0)
			{
				delay delay_obj = new delay(time_delay);
				new Thread(delay_obj).run();
				pre_delivery_check(m, S, V);
			}
			else pre_delivery_check(m, S, V);
			
		} catch (Exception e) {System.out.println(e);}
	}
	
	public void pre_delivery_check(String m, Map<Integer, VectorClock> S, VectorClock V)
	{
		try
		{
			if (!S.containsKey(id))
				deliver(m, S, V);
			else if (S.get(id) != null && vector_clock_check)
				deliver(m, S, V);
			else message_buffer.put(V, m);
			
		}catch (Exception e) {System.out.println(e);}
	}
	
	//Check the incoming clock buffer S in order to determine whether deliver or not.
	public boolean vector_clock_check(Map<Integer, VectorClock> S)
	{
		if ( .isuptodate(S) )
		{
			
		}
	}
}
