package lab1.interf;

import java.rmi.*; 
import java.rmi.RemoteException;
import java.util.*;
import lab1.clock.VectorClock;

public interface SESInterf extends Remote{
	
	void send(String m, int destination_id, int delay) throws RemoteException;
	
	void delay_check(String m, Map<Integer, VectorClock> S, VectorClock V, int delay) throws RemoteException;

	void pre_delivery_check(String m, Map<Integer, VectorClock> S, VectorClock V) throws RemoteException;
	
	boolean vector_clock_check(Map<Integer, VectorClock> S) throws RemoteException;
}
