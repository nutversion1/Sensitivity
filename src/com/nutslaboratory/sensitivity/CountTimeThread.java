package com.nutslaboratory.sensitivity;

public class CountTimeThread extends Thread
{
	GameView gameView;
	public boolean keepGoing = true;
	public boolean pause = false;
	
	public CountTimeThread(GameView v)
	{	
		super();
		
		gameView = v;
	}
	
	public void run()
	{
		while(keepGoing)
		{
			try
			{	
				//if pause then ignore the following code
				if (pause)
				{
					continue;
				}
				
				//delay time
				Thread.sleep(10);
				
				if (keepGoing)
				{
					//increase count time
					gameView.countTime += 0.01;
					gameView.countTime = Float.parseFloat(String.format("%.2f", gameView.countTime));
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void pauseTime()
	{
		pause = true;
	}
	
	public void resumeTime()
	{
		pause = false;
	}
}
