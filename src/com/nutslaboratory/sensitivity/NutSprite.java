package com.nutslaboratory.sensitivity;

import android.content.res.Resources;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

public class NutSprite 
{
	public NutView view;
	public Bitmap image;
	public int x;
	public int y;
	public boolean visible;
	
	
	public NutSprite(NutView v)
	{
		view = v;
		
		x = 0;
		y = 0;
		visible = true;
		view.addToSpriteList(this);

	}
	
	public void setImage(GameView gameView, Resources res, int id)
	{	
	    image = BitmapFactory.decodeResource(res, id);
	}
	
	public void scale(int width, int height){
		image = Bitmap.createScaledBitmap(image, width, height, false);
	}
	
	public Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight)
	{
		Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);

	    float scaleX = newWidth / (float) bitmap.getWidth();
	    float scaleY = newHeight / (float) bitmap.getHeight();
	    float pivotX = 0;
	    float pivotY = 0;

	    Matrix scaleMatrix = new Matrix();
	    scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY);

	    Canvas canvas = new Canvas(scaledBitmap);
	    canvas.setMatrix(scaleMatrix);
	    canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
	    
	    return scaledBitmap;
	}
	
	public void setPosition(int newX, int newY)
	{
		x = newX;
		y = newY;
	}
	
	public void setVisible(boolean isVisible)
	{
		visible = isVisible;
	}
	
	public boolean collidePoint(int xPosition, int yPosition)
	{
		boolean isCollide = false;
		
		if (xPosition >= x && 
			xPosition <= x + image.getWidth() &&
			yPosition >= y &&
			yPosition <= y + image.getHeight())
		{
			isCollide = true;
		}
		
		return isCollide;
	}
	
	public void doEvents(MotionEvent event)
	{
		
	}
	
	public void update(Canvas canvas)
	{
		
	}
	
}
