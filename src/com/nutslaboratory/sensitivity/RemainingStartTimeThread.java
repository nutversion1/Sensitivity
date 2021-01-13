package com.nutslaboratory.sensitivity;

public class RemainingStartTimeThread extends Thread
{
	GameView gameView;
	public boolean keepGoing = true;
	public boolean pause = false;
	
	public RemainingStartTimeThread(GameView v)
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
				
				//play 3..2..1..go.. sound
				if (gameView.remainingStartTime == 3)
				{
					Assets.playSound(gameView.threeSound);
				}
				else if (gameView.remainingStartTime == 2)
				{
					Assets.playSound(gameView.twoSound);
				}
				else if (gameView.remainingStartTime == 1)
				{
					Assets.playSound(gameView.oneSound);
				}
				else if (gameView.remainingStartTime == 0)
				{
					Assets.playSound(gameView.goSound);
				}
				
				//delay time
				Thread.sleep(1000);
				
				//decrease remaining start time
				gameView.remainingStartTime -= 1;
			
				//exit thread
				if (gameView.remainingStartTime < 0)
				{
					keepGoing = false;
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
