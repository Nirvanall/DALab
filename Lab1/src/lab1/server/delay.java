package lab1.server;

import lab1.clock.VectorClock;
import java.util.*;

public class delay implements Runnable{
	
	private int delay_time;
	private String m;
	private HashMap<Integer, VectorClock> S;
	private VectorClock V;
	private InterfImp process;
	
	public delay(int delay, String msg, HashMap<Integer, VectorClock> Sm, VectorClock Vi, InterfImp process)
	{
		this.delay_time = delay;
		this.m = msg;
		this.S = Sm;
		this.V = Vi;
		this.process = process;
	}
	
	@Override
	public void run() {
		try{
			Thread.sleep(delay_time);
			this.process.pre_delivery_check(m, S, V);
		}catch (Exception e) {System.out.println(e);}
	}

}
