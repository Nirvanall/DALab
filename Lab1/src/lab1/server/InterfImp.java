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
		vector_clock = new VectorClock(process_number);
		clock_buffer = new HashMap<Integer, VectorClock>();
		message_buffer = new HashMap<VectorClock, String>();
		this.receivedMessages = new ArrayList<String>();
	}
	
	private VectorClock vector_clock;
	HashMap<Integer, VectorClock> clock_buffer;
	HashMap<VectorClock, String> message_buffer;
	private ArrayList<String> receivedMessages;
	
	public ArrayList<String> getReceivedMessages(){
		return receivedMessages;
	}
	
	//Send a message m together with local clock_buffer and local vector_clock
	//to process destination_id with a certain delay (on test purpose).
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
	
	
	//Check if the incoming message is in the proper order.
	//If so, deliver it. Then deliver message in the buffer if any that is suitable for delivery.
	//If not, put it into buffer awaiting further delivery.
	public void pre_delivery_check(String m, Map<Integer, VectorClock> S, VectorClock V)
	{
		try
		{
			if (!S.containsKey(id))
				{
					deliver(m, S, V);
					deliver_buffer(S);
				}
			else if ( S.get(id) != null && vector_clock.isuptodate(S.get(id)) )
				{
					deliver(m, S, V);
					deliver_buffer(S);
				}
			else message_buffer.put(V, m);
			
		}catch (Exception e) {System.out.println(e);}
	}
	
	
	//Deliver the massage.
	//Update clock_buffer and vector_clock.
	public void deliver(String m, Map<Integer, VectorClock> S, VectorClock V)
	{
		try
		{
			System.out.println("Reveiving message: " + m);
			
			//Merge local buffer and buffer coming along with message.
			for (int i: S.keySet())
			{
				if ( clock_buffer.containsKey(i) )
				{
					VectorClock temp_clock = new VectorClock(process_num);
					for (int j=0; j<process_num; j++)
					{
						temp_clock.update_vector( j, Math.max(clock_buffer.get(i).get_clock(j), S.get(i).get_clock(j)) );
					}
					clock_buffer.put(i, temp_clock);
				}
				else clock_buffer.put(i, S.get(i));
			}
			
			//Update knowledge about other processes' time-stamps in local vector_clock
			for (int j=0; j<process_num; j++)
			{
				vector_clock.update_vector( j, Math.max(V.get_clock(j), vector_clock.get_clock(j)) );
			}
			
			vector_clock.increment_clock(id);
		} catch (Exception e) {System.out.println(e);}
	}
	
	
	//Find message that is suitable for delivery in the message_buffer.
	//Deliver any result that is found, then delete it from buffer.
	public void deliver_buffer(Map<Integer, VectorClock> S)
	{
		if (!message_buffer.isEmpty())
		{
			for (VectorClock temp : message_buffer.keySet())
			{
				if ( vector_clock.isuptodate(S.get(id)) )
				{
					deliver(message_buffer.get(temp), S, temp);
					message_buffer.remove(temp);
				}
			}
				
		}
	}
}
