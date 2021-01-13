package com.nutslaboratory.sensitivity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;

public class Button extends NutSprite
{
	static final String DEFAULT_STATE = "default_state";
	static final String SELECT_STATE = "select_state";
	
	GameView gameView;
	String name;
	String state;
	Paint paint;
	Boolean hasPressed;
	
	public Button(GameView v, String buttonName) 
	{
		super(v);
		
		gameView = v;
		name = buttonName;
		state = DEFAULT_STATE;
		hasPressed = false;
		
		//set default image
		if (name == "play")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.play_button_default);
		}
		else if (name == "record")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.record_button_default);
		}
		else if (name == "credit")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.credit_button_default);
		}
		else if (name == "menu")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.menu_button_default);
		}
		else if (name == "continue")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.continue_button_default);
		}
		else if (name == "start")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.start_button);
		}
		else if (name == "replay")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.replay_button_default);
		}
		else if (name == "yes")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.yes_button_default);
		}
		else if (name == "no")
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.no_button_default);
		}
	}
	
	public void changeState(String newState)
	{
		state = newState;
		
		//set image
		if (state == DEFAULT_STATE)
		{
			if (name == "play")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.play_button_default);
			}
			else if (name == "record")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.record_button_default);
			}
			else if (name == "credit")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.credit_button_default);
			}
			else if (name == "menu")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.menu_button_default);
			}
			else if (name == "continue")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.continue_button_default);
			}
			else if (name == "start")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.start_button);
			}
			else if (name == "replay")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.replay_button_default);
			}
			else if (name == "yes")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.yes_button_default);
			}
			else if (name == "no")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.no_button_default);
			}
		}
		else if (state == SELECT_STATE)
		{
			if (name == "play")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.play_button_select);
			}
			else if (name == "record")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.record_button_select);
			}
			else if (name == "credit")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.credit_button_select);
			}
			else if (name == "menu")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.menu_button_select);
			}
			else if (name == "continue")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.continue_button_select);
			}
			else if (name == "start")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.start_button);
			}
			else if (name == "replay")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.replay_button_select);
			}
			else if (name == "yes")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.yes_button_select);
			}
			else if (name == "no")
			{
				this.setImage(gameView, gameView.getResources(), R.drawable.no_button_select);
			}
		}
	}
	
	@Override
	public void doEvents(MotionEvent event)
	{
		int eventaction = event.getAction();
		int xPosition = (int) ((event.getX() / MainActivity.gameView.getWidth()) * MainActivity.GAME_WIDTH);
		int yPosition = (int) ((event.getY() / MainActivity.gameView.getHeight()) * MainActivity.GAME_HEIGHT);
		
		switch (eventaction)
		{
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				if (hasPressed)
				{
					hasPressed = false;
					this.changeState(DEFAULT_STATE);
				}
				
				break;
			case MotionEvent.ACTION_DOWN:
				if (! hasPressed)
				{
					if (collidePoint(xPosition, yPosition))
					{
						hasPressed = true;
						
						//quit mode
						if (gameView.mode == GameView.QUIT_MODE)
						{
							if (name == "yes" || name == "no")
							{
								if (visible)
								{
									this.changeState(SELECT_STATE);
							
									//play button sound
									Assets.playSound(gameView.buttonSound);
								}
							}
						}
						//other mode
						else
						{
							if (visible)
							{
								{
									this.changeState(SELECT_STATE);
									
									//play button sound
									Assets.playSound(gameView.buttonSound);
								}
							}
						}
					}
				}
				
				break;
		}
	}
}