package lab1.server;

public class delay implements Runnable{
	
	private int delay_time;
	
	public delay(int delay)
	{
		delay_time = delay;
	}
	
	@Override
	public void run() {
		try{
			Thread.sleep(delay_time);
		}catch (Exception e) {System.out.println(e);}
	}

}
