package lab1.interf;

import java.rmi.*; 
import java.rmi.RemoteException;
import java.util.*;
import lab1.clock.VectorClock;

public interface SESInterf extends Remote{
	
	//Send a message to another process.
	//Parameters
	//m: message to be sent;
	//destination_id: the index of the process where the message is sent to;
	//delay: time of delay in seconds.
	void send(String m, int destination_id, int delay) throws RemoteException;
	
	
	//Only check if the incoming message contains a delay.
	//If so, implement the delay then proceed to receive.
	//Parameters
	//m: message the has been received;
	//S: clock_buffer sent with the message;
	//V: vector_clock sent with the message;
	//delay: time of delay in seconds.
	void delay_check(String m, Map<Integer, VectorClock> S, VectorClock V, int delay) throws RemoteException;

	
	//Check if the incoming message is in the proper order.
	//If so, deliver it. Then deliver message in the buffer if any that is suitable for delivery.
	//If not, put it into buffer awaiting further delivery.
	//Parameters
	//m: message the has been received;
	//S: clock_buffer sent with the message;
	//V: vector_clock sent with the message.
	void pre_delivery_check(String m, Map<Integer, VectorClock> S, VectorClock V) throws RemoteException;
	
	
	//Deliver the massage.
	//Update clock_buffer and vector_clock.
	//Parameters
	//m: message the has been received;
	//S: clock_buffer sent with the message;
	//V: vector_clock sent with the message.
	void deliver(String m, Map<Integer, VectorClock> S, VectorClock V) throws RemoteException;
	
	
	//Find message that is suitable for delivery in the message_buffer.
	//Deliver any result that is found, then delete it from buffer.
	//Parameters
	//S: clock_buffer sent with the latest message.
	void deliver_buffer(Map<Integer, VectorClock> S) throws RemoteException;
	
	ArrayList<String> getReceivedMessages() throws RemoteException;
	
}
