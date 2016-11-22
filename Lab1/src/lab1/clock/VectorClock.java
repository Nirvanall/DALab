package lab1.clock;

import java.io.Serializable;


@SuppressWarnings("serial")
public class VectorClock implements Serializable{

	int[] Vector;
	
	public int get_clock(int process_index)
	{
		return Vector[process_index];
	}
	
	public VectorClock(int process_num){
		int i;
		Vector = new int[process_num];
		
		for (i=0; i<process_num; i++)
			Vector[i] = 0;	
	}
	
	public void update_vector(int index, int clock)
	{
		Vector[index] = clock;

	} 
	
	public void increment_clock(int process_index){
		Vector[process_index] = Vector[process_index] + 1;
	}
	
	public boolean isuptodate(VectorClock input)
	{
		for (int i=0; i<Vector.length; i++)
		{
			if ( Vector[i] < input.get_clock(i) )
				return false;
		}
		return true;
	}
	
}
