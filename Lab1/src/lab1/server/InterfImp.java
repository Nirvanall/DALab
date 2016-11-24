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
		clock_buffer = new HashMap<Integer, VectorClock>();
		message_buffer = new ArrayList<buffer>();
		vector_clock = new VectorClock(process_number);
		receivedMessages = new ArrayList<String>();
	}
	
	private ArrayList<String> receivedMessages;
	public ArrayList<String> getReceivedMessages()
	{
		return receivedMessages;
	}
	
	private VectorClock vector_clock;
	HashMap<Integer, VectorClock> clock_buffer;
	ArrayList<buffer> message_buffer;
	
	
	//Send a message m together with local clock_buffer and local vector_clock
	//to process destination_id with a certain delay (on test purpose).
	public void send(String m, int destination_id, int delay) 
	{
		vector_clock.increment_clock(id); //Increment own clock
		
		try
		{
			SESInterf sendto = (SESInterf) Naming.lookup("rmi://localhost:1099/" + destination_id);
			System.out.print("@(Process " + id + ")---- sending message " + m + " to (Process " + destination_id + 
					") with VectorClock " + Arrays.toString(vector_clock.current_clock()) );
			
			if ( !clock_buffer.isEmpty() )
			{
				for (int i:clock_buffer.keySet())
				{
					System.out.print(" and clock_buffer ");
					System.out.print(i + "-" + Arrays.toString(clock_buffer.get(i).current_clock()) +" ");
				}
				System.out.print("\n");
			}
			else System.out.println(" and null clock_buffer ");
			
			sendto.delay_check(m, clock_buffer, vector_clock, delay);
			clock_buffer.put(destination_id, vector_clock.get_VectorClock());
			
		} catch (Exception e) {System.out.println(e);}
		
	}
	
	
	//Only check if the input message contains a delay.
	//If so, implement the delay, then proceed.
	public void delay_check(String m, HashMap<Integer, VectorClock> S, VectorClock V, int time_delay)
	{
		try
		{
			if (time_delay != 0)
			{
				delay delay_obj = new delay(time_delay, m, S, V, this);
				new Thread(delay_obj).start();
			}
			else pre_delivery_check(m, S, V);
			
		} catch (Exception e) {System.out.println(e);}
	}
	
	
	//Check if the incoming message is in the proper order.
	//If so, deliver it. Then deliver message in the buffer if any that is suitable for delivery.
	//If not, put it into buffer awaiting further delivery.
	public void pre_delivery_check(String m, HashMap<Integer, VectorClock> S, VectorClock V)
	{
		
		try
		{
			if (deliverable(S))
				{
					deliver(m, S, V);
					deliver_buffer();
				}
			else {
				
				buffer buffer_m = new buffer(m, S, V);
				message_buffer.add(buffer_m);
			}
			
		}catch (Exception e) {System.out.println(e);}
	}
	
	
	//Checking criteria of delivery according to protocol.
	public boolean deliverable(HashMap<Integer, VectorClock> S)
	{
		if (!S.containsKey(id))
			return true;
		else if ( S.containsKey(id) && vector_clock.isuptodate(S.get(id)) )
			return true;
		else
			return false;
	}
	
	
	//Deliver the massage.
	//Update clock_buffer and vector_clock.
	public void deliver(String m, HashMap<Integer, VectorClock> S, VectorClock V)
	{
		try
		{
			System.out.println("@(Process " + id + ")---- Reveiving message: " + m);
			
			receivedMessages.add(m);
			
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
			
			//Update knowledge about other processes' time-stamps in local vector_clock.
			for (int j=0; j<process_num; j++)
			{
				vector_clock.update_vector( j, Math.max(V.get_clock(j), vector_clock.get_clock(j)) );
			}
			
			//Update local clock since a new event has finished.
			vector_clock.increment_clock(id);
			
			//Debug purpose.
			//System.out.println("Vector clock of process " + id + " is " + Arrays.toString(vector_clock.current_clock()));
		} catch (Exception e) {System.out.println(e);}
	}
	
	
	//Find message that is suitable for delivery in the message_buffer.
	//Deliver any result that is found, then delete it from buffer.
	public void deliver_buffer()
	{
		int[] vector_delivered= new int[]{0};
		int i = 0;
		
		if (!message_buffer.isEmpty())
		{
			for (int temp=0; temp < message_buffer.size(); temp++)
			{
				if ( deliverable(message_buffer.get(temp).S) )
				{
					deliver( message_buffer.get(temp).m, message_buffer.get(temp).S, message_buffer.get(temp).V );
					vector_delivered[i] = temp;
					i = i+1;
				}
			}

			for (int j=i-1; j>=0; j--)
			{
				message_buffer.remove(j);
			}
			
			vector_delivered = null;
		}
	}
}
