package com.nutslaboratory.sensitivity;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class NutView extends View 
{
	public Bitmap gameImage;
	public Rect gameImageSrc;
	public Rect gameImageDst;
	public Canvas gameCanvas;
	
	public Context context;
	public int screenWidth, screenHeight;
	public ArrayList<NutSprite> spriteList = new ArrayList<NutSprite>();
	
	Paint paint;

	public NutView(Context context) 
	{
		super(context);
		
		this.context = context;
		
		gameImage = Bitmap.createBitmap(MainActivity.GAME_WIDTH, MainActivity.GAME_HEIGHT, Bitmap.Config.RGB_565);
		gameImageSrc = new Rect(0, 0, gameImage.getWidth(), gameImage.getHeight());
		gameImageDst = new Rect();
		gameCanvas = new Canvas(gameImage);
		
		//set paint
		paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setFilterBitmap(true);
	    paint.setDither(true);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		//initialize game
		initGame();
	}
	
	public void initGame()
	{
		
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{	
		//draw sprites' image
		for (int i = 0; i < spriteList.size(); i++)
		{
			if (spriteList.get(i).visible)
			{   
			    //draw sprite on game image' canvas
			    gameCanvas.drawBitmap(spriteList.get(i).image, spriteList.get(i).x, spriteList.get(i).y, paint);
			}
		}
		
		//view's update
		update(gameCanvas);
		
		//sprites' update
		for (int i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).update(gameCanvas);
		}
		
		//draw game image' canvas to screen's canvas
	    canvas.getClipBounds(gameImageDst);
	    canvas.drawBitmap(gameImage, gameImageSrc, gameImageDst, paint);
		
		//redraw
		invalidate();
	}
	
	public void update(Canvas canvas)
	{
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//view's event
		doEvents(event);
		
		//sprites' event
		for (int i = 0; i < spriteList.size(); i++)
		{
			spriteList.get(i).doEvents(event);
		}
		
		//redraw
		invalidate();
		
		return true;
	}
	
	public void doEvents(MotionEvent event)
	{
		
	}
	
	
	public void addToSpriteList(NutSprite newSprite)
	{
		spriteList.add(newSprite);
	}
	
	public void removeSprite(NutSprite sprite)
	{
		spriteList.remove(sprite);
		
		sprite.image.recycle();
		sprite = null;
	}
	
	public void moveSpriteToTop(NutSprite sprite)
	{
		spriteList.remove(sprite);
		spriteList.add(sprite);
	}
	
	

}
