package com.nutslaboratory.sensitivity;

import java.util.ArrayList;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BallSprite extends NutSprite
{
	GameView gameView;
	int type;
	int column, row;
	boolean hasRemoved;
	int group;
	
	
	public BallSprite(GameView v, int type, int column, int row)
	{
		super(v);
		
		gameView = v;
		this.type = type;
		this.column = column;
		this.row = row;
		this.group = 0;
		
		//set default image
		if (this.type == 1)
		{
			setImage(gameView, gameView.getResources(), R.drawable.chalk_ball_red);
		}
		else if (this.type == 2)
		{
			setImage(gameView, gameView.getResources(), R.drawable.chalk_ball_blue);
		}
		else if (this.type == 3)
		{
			setImage(gameView, gameView.getResources(), R.drawable.chalk_ball_green);
		}
		else if (this.type == 4)
		{
			setImage(gameView, gameView.getResources(), R.drawable.chalk_ball_yellow);
		}
		else if (this.type == 5)
		{
			setImage(gameView, gameView.getResources(), R.drawable.chalk_ball_pink);
		}
	}
	
	
	public ArrayList<BallSprite> getSameAttachBalls()
	{	
		ArrayList<BallSprite> sameAttachBallList = new ArrayList<BallSprite>();
		
		//left ball
		if (this.column != 0)
		{
			if (gameView.ballList.get(this.column-1).get(this.row) != null)
			{
				BallSprite leftBall = (BallSprite) gameView.ballList.get(this.column-1).get(this.row);
				if (leftBall.type == this.type)
				{
					sameAttachBallList.add(leftBall);
				}
			}
		}
		
		//right ball
		if (this.column != GameView.TOTAL_COLUMN-1)
		{
			if (gameView.ballList.get(this.column+1).get(this.row) != null)
			{
				BallSprite rightBall = (BallSprite) gameView.ballList.get(this.column+1).get(this.row);
				if (rightBall.type == this.type)
				{
					sameAttachBallList.add(rightBall);
				}
			}
		}
		
		//top ball
		if (this.row != GameView.TOTAL_ROW-1)
		{
			if (gameView.ballList.get(this.column).get(this.row+1) != null)
			{
				BallSprite topBall = (BallSprite) gameView.ballList.get(this.column).get(this.row+1);
				if (topBall.type == this.type)
				{
					sameAttachBallList.add(topBall);
				}
			}
		}
		
		//bottom ball
		if (this.row != 0)
		{
			if (gameView.ballList.get(this.column).get(this.row-1) != null)
			{
				BallSprite bottomBall = (BallSprite) gameView.ballList.get(this.column).get(this.row-1);
				if (bottomBall.type == this.type)
				{
					sameAttachBallList.add(bottomBall);
				}
			}
		}
		
		return sameAttachBallList;
	}
	
	public void remove()
	{
		//remove ball sprite from view
		gameView.removeSprite(this);
		//in ball list ,set position at removing ball to null
		gameView.ballList.get(this.column).set(this.row, null);
	}
}


