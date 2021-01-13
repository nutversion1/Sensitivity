package com.nutslaboratory.sensitivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;

public class KeyboardButton extends NutSprite
{
	static final String DEFAULT_STATE = "default_state";
	static final String SELECT_STATE = "select_state";
	
	GameView gameView;
	String name;
	String state;
	Paint paint;
	Boolean hasPressed;
	
	public KeyboardButton(GameView v, String buttonName) 
	{
		super(v);
		
		gameView = v;
		name = buttonName;
		state = DEFAULT_STATE;
		hasPressed = false;
		
		//create paint
		paint = new Paint();
		paint.setTextSize(24);
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.createFromAsset(gameView.context.getAssets(), "fonts/a song for jennifer.ttf"));
		paint.setAntiAlias(true);
		
		//set default image
		this.setImage(gameView, gameView.getResources(), R.drawable.keyboard_button_default);
	}
	
	public void changeState(String newState)
	{
		state = newState;
		
		//set image
		if (state == DEFAULT_STATE)
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.keyboard_button_default);
		}
		else if (state == SELECT_STATE)
		{
			this.setImage(gameView, gameView.getResources(), R.drawable.keyboard_button_select);
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
				if (gameView.mode == GameView.GAMEOVER_MODE)
				{
					if (! hasPressed)
					{
						if (collidePoint(xPosition, yPosition))
						{
							hasPressed = true;
							
							changeState(SELECT_STATE);
							
							//System.out.println("press keyboard button");
							
							//play button sound
							Assets.playSound(gameView.buttonSound);
						}
					}
				}
				
				
				
				break;
		}
	}
	
	@Override
	public void update(Canvas canvas)
	{
		//show button name
		if (gameView.mode == GameView.GAMEOVER_MODE)
		{
			if (name == "OK")
			{
				canvas.drawText(name, x+12, y+37, paint);
			}
			else if (name == "DEL")
			{
				canvas.drawText(name, x+9, y+37, paint);
			}
			else
			{
				canvas.drawText(name, x+21, y+37, paint);
			}
		}
	}

}


