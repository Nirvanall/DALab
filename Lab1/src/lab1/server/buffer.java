package lab1.server;

import java.util.*;
import lab1.clock.*;

//This is a class used for storing message which is mis-ordered.
public class buffer {

	public String m;
	public HashMap<Integer, VectorClock> S;
	public VectorClock V;
	
	public buffer(String m, HashMap<Integer, VectorClock> S, VectorClock V)
	{
		this.m = m;
		this.S = S;
		this.V = V;
	}
	
}
