package lab1.clock;

import java.io.Serializable;


@SuppressWarnings("serial")
public class VectorClock implements Serializable{

	int[] Vector;
	
	
	//Return current integer array of local clock vector.
	public int[] current_clock()
	{
		return Vector;
	}
	
	
	//Return clock for the process indicated by parameter "process_index" in local clock vector.
	public int get_clock(int process_index)
	{
		return Vector[process_index];
	}
	
	
	//Return a copy of current clock vector.
	public VectorClock get_VectorClock()
	{
		VectorClock duplicate = new VectorClock(Vector.length);
		
		for (int i=0; i<Vector.length; i++)
		{
			duplicate.update_vector(i, this.get_clock(i));
		}
		
		return duplicate;
	}
	
	
	public VectorClock(int process_num){
		int i;
		Vector = new int[process_num];
		
		for (i=0; i<process_num; i++)
			Vector[i] = 0;	
	}
	
	
	//Update the value of clock vector's entry which is indicated by parameter "index"
	//with parameter "clock".
	public void update_vector(int index, int clock)
	{
		Vector[index] = clock;

	} 
	
	
	//Increment the clock vector's entry which is indicated by parameter "process_index" by 1.
	public void increment_clock(int process_index){
		Vector[process_index] = Vector[process_index] + 1;
	}
	
	
	//Check if the current clock vector is newer than the input clock vector "input".
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
