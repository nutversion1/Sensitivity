

package com.nutslaboratory.sensitivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.google.android.gms.analytics.HitBuilders;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.MotionEvent;

public class GameView extends NutView 
{
	//constant variables
	public static final int TOTAL_COLUMN = 8;
	public static final int TOTAL_ROW = 10;
	public static final int TOTAL_WRONG = 5;
	public static final String PREFERENCES_NAME = "my_preferecnce";
	public static final int TOTAL_BEST_TIME_USERNAME = 5;
	public static final String MENU_MODE = "menu_mode";
	public static final String HIGHSCORES_MODE = "highscores_mode";
	public static final String CREDITS_MODE = "credits_mode";
	public static final String RULES_MODE = "rules_mode";
	public static final String START_MODE = "start_mode";
	public static final String PLAY_MODE = "play_mode";
	public static final String END_MODE = "end_mode";
	public static final String GAMEOVER_MODE = "gameover_mode";
	public static final String QUIT_MODE = "quit_mode";
	
	//variables
	public Activity mainActivity;
	public boolean gameHasStarted = false;
	public String gameVersion;
	public Paint gameVersionPaint, countTimePaint, faultPaint, orderTotalPaint, recordPaint;
	public String mode;
	public String oldMode;
	public NutSprite background, menuBackground, recordBackground, creditsBackground, rulesBackground, playBackground, endBackground, quitBackground;
	public Button playButton, highscoresButton, creditsButton, menuButton, startButton, replayButton, continueButton, yesButton, noButton;
	public NutSprite countThreeLabel, countTwoLabel, countOneLabel, countGoLabel;
	public ArrayList<ArrayList> ballList;
	public ArrayList<ArrayList> ballGroupList;
	public int totalGroup;
	public int orderTotal;
	public int wrong;
	public int remainingStartTime;
	public float countTime;
	public CountTimeThread countTimeThread;
	public RemainingStartTimeThread remainingStartTimeThread;
	public ArrayList<String> bestTimeUsernameList;
	public ArrayList<Float> bestTimeList;
	public int currentBestTimeUsernameNum;
	public ArrayList<KeyboardButton> keyboardButtonList;
	public NutSprite currentUsernameArrow;
	public NutSprite currentUsernameLine;
	public int buttonSound, touchSound, faultSound, threeSound, twoSound, oneSound, goSound;
	
	
	public GameView(Context context, Activity mainActivity) 
	{
		super(context);
		
		this.mainActivity = mainActivity;

		System.out.println("game view's constructor: Sensitivity");
		
	}
	
	@Override
	public void initGame()
	{
		//get app version
		try
		{
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			gameVersion = info.versionName;
			
			System.out.println("game version:"+gameVersion);
		}
		catch (Exception e)
		{
			
		}
		
		//create background
		background = new NutSprite(this);
		background.setImage(this, getResources(), R.drawable.background);
		background.setPosition(0,0);
		
		//create game version paint
		gameVersionPaint = new Paint();
		gameVersionPaint.setTextSize(30);
		gameVersionPaint.setColor(Color.WHITE);
		gameVersionPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/a song for jennifer.ttf"));
		gameVersionPaint.setAntiAlias(true);
		//create count time paint
		countTimePaint = new Paint();
		countTimePaint.setTextSize(42);
		countTimePaint.setColor(Color.YELLOW);
		countTimePaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/orange juice 2.0.ttf"));
		countTimePaint.setAntiAlias(true);
		//create fault paint
		faultPaint = new Paint();
		faultPaint.setTextSize(42);
		faultPaint.setColor(Color.rgb(255, 165, 0));
		faultPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/orange juice 2.0.ttf"));
		faultPaint.setAntiAlias(true);
		//create order total paint   
		orderTotalPaint = new Paint();
		orderTotalPaint.setTextSize(106);
		orderTotalPaint.setColor(Color.WHITE);
		orderTotalPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/a song for jennifer.ttf"));
		orderTotalPaint.setAntiAlias(true);
		//create record paint   
		recordPaint = new Paint();
		recordPaint.setTextSize(42);
		recordPaint.setColor(Color.WHITE);
		recordPaint.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/orange juice 2.0.ttf"));
		recordPaint.setAntiAlias(true);
		
		//create sound
		buttonSound = Assets.loadSound("button.wav");
		touchSound = Assets.loadSound("touch.wav");
		faultSound = Assets.loadSound("fault.wav");
		threeSound = Assets.loadSound("three.wav");
		twoSound = Assets.loadSound("two.wav");
		oneSound = Assets.loadSound("one.wav");
		goSound = Assets.loadSound("go.wav");
		
		//play "piano loops" music
		Assets.playMusic("piano_loops.mp3", true);
		
		//change mode to menu mode
		changeMode(GameView.MENU_MODE);
		
		//set game has started ot true
		gameHasStarted = true;
	}
	
	public void changeMode(String newMode)
	{	
		//sent tracker's event to google analytics
		MainActivity.tracker.send(new HitBuilders.EventBuilder()
	    .setCategory("Change Mode")
	    .setAction(newMode)
	    .build());
		
		//set mode
		oldMode = mode;
		mode = newMode;
		System.out.println(mode);
		
		if (newMode == GameView.MENU_MODE)
		{	
			if (oldMode == GameView.RULES_MODE)
			{
				////remove rules stage
				this.removeSprite(rulesBackground);
				this.removeSprite(menuButton);
				this.removeSprite(continueButton);
				
			}
			else if (oldMode == GameView.START_MODE)
			{
				this.removeSprite(playBackground);
				this.removeSprite(menuButton);
				this.removeSprite(startButton);
				this.removeSprite(countThreeLabel);
				this.removeSprite(countTwoLabel);
				this.removeSprite(countOneLabel);
				this.removeSprite(countGoLabel);
				
				//remove ball
				for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
				{	
					for (int row = 0; row < GameView.TOTAL_ROW; row++)
					{	
						if (ballList.get(column).get(row) != null)
						{
							BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
						
							this.removeSprite(tempBall);
						}
					}
				}
			}
			else if (oldMode == GameView.PLAY_MODE)
			{
				this.removeSprite(playBackground);
				this.removeSprite(menuButton);
				this.removeSprite(replayButton);
				this.removeSprite(countThreeLabel);
				this.removeSprite(countTwoLabel);
				this.removeSprite(countOneLabel);
				this.removeSprite(countGoLabel);
				
				//remove ball
				for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
				{	
					for (int row = 0; row < GameView.TOTAL_ROW; row++)
					{	
						if (ballList.get(column).get(row) != null)
						{
							BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
						
							this.removeSprite(tempBall);
						}
					}
				}
				
				//stop count time thread
				countTimeThread.keepGoing = false;
				
				//stop "acid loops" music
				Assets.stopMusic();
				//play "piano loops" music
				Assets.playMusic("piano_loops.mp3", true);
			}
			else if (oldMode == GameView.GAMEOVER_MODE)
			{
				////remove gameover stage
				this.removeSprite(recordBackground);
				this.removeSprite(menuButton);
				this.removeSprite(replayButton);
			}
			else if (oldMode == GameView.HIGHSCORES_MODE)
			{
				this.removeSprite(recordBackground);
				this.removeSprite(menuButton);
			}
			else if (oldMode == GameView.CREDITS_MODE)
			{
				this.removeSprite(creditsBackground);
				this.removeSprite(menuButton);
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			////create menu stage
			
			//create menu background
			menuBackground = new NutSprite(this);
			menuBackground.setImage(this, getResources(), R.drawable.menu_background);
			menuBackground.setPosition(0,0);
			
			//create play button
			playButton = new Button(this, "play");
			playButton.setPosition(90, 281);
			
			//create highscores button
			highscoresButton = new Button(this, "record");
			highscoresButton.setPosition(90, 375);
			
			//create credits button
			creditsButton = new Button(this, "credit");
			creditsButton.setPosition(90, 476);
			
			
			
		}
		else if (newMode == GameView.RULES_MODE)
		{
			
			if (oldMode == GameView.MENU_MODE)
			{
				////remove menu stage
				this.removeSprite(menuBackground);
				this.removeSprite(playButton);
				this.removeSprite(highscoresButton);
				this.removeSprite(creditsButton);
				
				
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			
			
			////create rules stage
			//create rules background
			rulesBackground = new NutSprite(this);
			rulesBackground.setImage(this, getResources(), R.drawable.rules_background);
			rulesBackground.setPosition(0,0);
			
			//create menu button
			menuButton = new Button(this, "menu");
			menuButton.setPosition(18, 734);
			
			//create continue button
			continueButton = new Button(this, "continue");
			continueButton.setPosition(327, 734);
		}
		else if (newMode == GameView.START_MODE)
		{
			if (oldMode == GameView.RULES_MODE)
			{
				////remove rules stage
				this.removeSprite(rulesBackground);
				this.removeSprite(menuButton);
				this.removeSprite(continueButton);
			}
			else if (oldMode == GameView.PLAY_MODE)
			{
				////remove play stage
				this.removeSprite(playBackground);
				this.removeSprite(menuButton);
				this.removeSprite(replayButton);
				this.removeSprite(countThreeLabel);
				this.removeSprite(countTwoLabel);
				this.removeSprite(countOneLabel);
				this.removeSprite(countGoLabel);
				
				//remove ball
				for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
				{	
					for (int row = 0; row < GameView.TOTAL_ROW; row++)
					{	
						if (ballList.get(column).get(row) != null)
						{
							BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
						
							this.removeSprite(tempBall);
						}
					}
				}
				
				//stop count time thread
				countTimeThread.keepGoing = false;
				
				//stop "acid loops" music
				Assets.stopMusic();
				//play "piano loops" music
				Assets.playMusic("piano_loops.mp3", true);
				
			}
			else if (oldMode == GameView.GAMEOVER_MODE)
			{
				////remove gameover stage
				this.removeSprite(recordBackground);
				this.removeSprite(menuButton);
				this.removeSprite(replayButton);
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			////create play stage
			
			//set variable
			remainingStartTime = 4;
			countTime = 0;
			currentBestTimeUsernameNum = -1;
			wrong = 0;
			
			//set best time usernames (shared preferences)
			bestTimeUsernameList = new ArrayList<String>();
			
			SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
			for (int i = 0; i < 5; i++)
			{
				bestTimeUsernameList.add(settings.getString("username"+(i+1), ""));
			}
			//set best times (shared preferences)
			bestTimeList = new ArrayList<Float>();
			
			settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
			for (int i = 0; i < 5; i++)
			{
				bestTimeList.add(settings.getFloat("bestTime"+(i+1), 0));
			}
			System.out.println("start");
			System.out.println(bestTimeUsernameList);
			System.out.println(bestTimeList);
			
			
			//create remaining start time thread
			remainingStartTimeThread = new RemainingStartTimeThread(this);
			//create count time thread
			countTimeThread = new CountTimeThread(this);
			
			//create play background
			playBackground = new NutSprite(this);
			playBackground.setImage(this, getResources(), R.drawable.play_background);
			playBackground.setPosition(0,0);
			
			//create balls
			createBalls();
			//set ball's position
			resetBallsPosition();
			//set balls' group
			setBallsGroup();
			//set order total
			setOrderTotal();
			
			//create menu button
			menuButton = new Button(this, "menu");			
			menuButton.setPosition(18, 15);
			
			//create start button
			startButton = new Button(this, "start");
			startButton.setPosition(140, 380);
			
			//create count label
			countThreeLabel = new NutSprite(this);
			countThreeLabel.setImage(this, getResources(), R.drawable.count_three);
			countThreeLabel.setPosition(136, 343);
			countThreeLabel.setVisible(false);
			
			countTwoLabel = new NutSprite(this);
			countTwoLabel.setImage(this, getResources(), R.drawable.count_two);
			countTwoLabel.setPosition(136, 343);
			countTwoLabel.setVisible(false);
			
			countOneLabel = new NutSprite(this);
			countOneLabel.setImage(this, getResources(), R.drawable.count_one);
			countOneLabel.setPosition(136, 343);
			countOneLabel.setVisible(false);
			
			countGoLabel = new NutSprite(this);
			countGoLabel.setImage(this, getResources(), R.drawable.count_go);
			countGoLabel.setPosition(136, 343);
			countGoLabel.setVisible(false);
			
		}
		else if (newMode == GameView.PLAY_MODE)
		{
			if (oldMode == GameView.START_MODE)
			{
				this.removeSprite(startButton);
				
				//stop "piano loops" music
				Assets.stopMusic();
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//if count time thread has not started yet
				if (!countTimeThread.isAlive())
				{
					//resume time "remaining time thread"
					remainingStartTimeThread.resumeTime();
				}
				//if count time thread has started
				else
				{
					//resume time "count time thread"
					countTimeThread.resumeTime();
				}
				
				//hide ads
				MainActivity.hideAds();
				
				return;
				
				
			}
			
			//create replay button
			replayButton = new Button(this, "replay");
			replayButton.setPosition(363, 15);
			
			//hide button
			menuButton.setVisible(false);
			replayButton.setVisible(false);
			
			//start remaining start time thread
			remainingStartTimeThread.start();
		}
		else if (newMode == GameView.END_MODE)
		{
			if (oldMode == GameView.PLAY_MODE)
			{	
				//remove sprite
				this.removeSprite(playBackground);
				this.removeSprite(menuButton);
				this.removeSprite(replayButton);
				this.removeSprite(countThreeLabel);
				this.removeSprite(countTwoLabel);
				this.removeSprite(countOneLabel);
				this.removeSprite(countGoLabel);
				
				//remove ball
				for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
				{	
					for (int row = 0; row < GameView.TOTAL_ROW; row++)
					{	
						if (ballList.get(column).get(row) != null)
						{
							BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
						
							this.removeSprite(tempBall);
						}
					}
				}
				
				//stop "acid loops" music
				Assets.stopMusic();
				//play "piano loops" music
				Assets.playMusic("piano_loops.mp3", true);
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			//stop count time thread
			countTimeThread.keepGoing = false;
			
			//create end background
			endBackground = new NutSprite(this);
			if (wrong < 5)
			{
				endBackground.setImage(this, getResources(), R.drawable.succeed_end_background);
			}
			else
			{
				endBackground.setImage(this, getResources(), R.drawable.fail_end_background);
			}
			endBackground.setPosition(0,0);
			
			//create continues button
			continueButton = new Button(this, "continue");
			continueButton.setPosition(181, 15);
			
		}
		else if (newMode == GameView.GAMEOVER_MODE)
		{
			if (oldMode == GameView.END_MODE)
			{
				//stop count time thread
				countTimeThread.keepGoing = false;
				
				//remove sprite
				this.removeSprite(endBackground);
				this.removeSprite(continueButton);
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			//create record background
			recordBackground = new NutSprite(this);
			recordBackground.setImage(this, getResources(), R.drawable.record_background);
			recordBackground.setPosition(0,0);
			
			//create menu button
			menuButton = new Button(this, "menu");
			menuButton.setPosition(18, 734);
			menuButton.setVisible(false);
			
			//create replay button
			replayButton = new Button(this, "replay");
			replayButton.setPosition(363, 734);
			replayButton.setVisible(false);
			
			//set best time record
			if (wrong < GameView.TOTAL_WRONG)
			{
				for (int i = 0; i < GameView.TOTAL_BEST_TIME_USERNAME; i++)
				{
					//
					if (countTime < bestTimeList.get(i))
					{
						bestTimeUsernameList.add(i, "");
						bestTimeList.add(i, countTime);
					}
					
					//
					if (bestTimeUsernameList.get(i) == "")
					{
						currentBestTimeUsernameNum = i;
						
						bestTimeList.set(i, countTime);
						
						break;
					}
				}
				System.out.println("gameover");
				System.out.println(bestTimeUsernameList);
				System.out.println(bestTimeList);
			}
			
			//player is in highscores
			if (currentBestTimeUsernameNum != -1)
			{
				//create keyboard buttons
				ArrayList<String> keyboadButtonNameList = new ArrayList<String>(Arrays.asList("A", "B", "C", "D", "E", "F", "G",
																							  "H", "I", "J", "K", "L", "M", "N",
																							  "O", "P", "Q", "R", "S", "T", "U",
																							  "V", "W", "X", "Y", "Z", "DEL", "OK"));
				
				keyboardButtonList = new ArrayList<KeyboardButton>();
				int i = 0;
				for (int row = 0; row < 4; row++)
				{
					for (int column = 0; column < 7; column++)
					{
						KeyboardButton newKeyboardButton = new KeyboardButton(this, keyboadButtonNameList.get(i));
						newKeyboardButton.setPosition(22+(column*63), 531+(row*65));
						keyboardButtonList.add(newKeyboardButton);
						
						i++;
					}
				}
				
				//create current username arrow
				currentUsernameArrow = new NutSprite(this);
				currentUsernameArrow.setImage(this, getResources(), R.drawable.arrow);
				currentUsernameArrow.setPosition(18, 203+(currentBestTimeUsernameNum*56));
				//create current username line
				currentUsernameLine = new NutSprite(this);
				currentUsernameLine.setImage(this, getResources(), R.drawable.line);
				currentUsernameLine.setPosition(296, 234+(currentBestTimeUsernameNum*56));
			}
			//player isn't in highscores
			else
			{
				//show menu button
				menuButton.setVisible(true);
				//show replay button
				replayButton.setVisible(true);
				
			}
		}
		else if (newMode == GameView.HIGHSCORES_MODE)
		{
			if (oldMode == GameView.MENU_MODE)
			{
				////remove menu stage
				this.removeSprite(menuBackground);
				this.removeSprite(playButton);
				this.removeSprite(highscoresButton);
				this.removeSprite(creditsButton);
				
				
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			
			
			//set best time usernames (shared preferences)
			bestTimeUsernameList = new ArrayList<String>();
			
			SharedPreferences settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
			for (int i = 0; i < 5; i++)
			{
				bestTimeUsernameList.add(settings.getString("username"+(i+1), ""));
			}
			//set best times (shared preferences)
			bestTimeList = new ArrayList<Float>();
			
			settings = context.getSharedPreferences(PREFERENCES_NAME, 0);
			for (int i = 0; i < 5; i++)
			{
				bestTimeList.add(settings.getFloat("bestTime"+(i+1), 0));
			}
			
			//create record background
			recordBackground = new NutSprite(this);
			recordBackground.setImage(this, getResources(), R.drawable.record_background);
			recordBackground.setPosition(0,0);
			
			//create menu button
			menuButton = new Button(this, "menu");
			menuButton.setPosition(196, 734);
		}
		else if (newMode == GameView.CREDITS_MODE)
		{
			if (oldMode == GameView.MENU_MODE)
			{
				////remove menu stage
				this.removeSprite(menuBackground);
				this.removeSprite(playButton);
				this.removeSprite(highscoresButton);
				this.removeSprite(creditsButton);
				
				
			}
			else if (oldMode == GameView.QUIT_MODE)
			{
				//
				this.removeSprite(quitBackground);
				this.removeSprite(yesButton);
				this.removeSprite(noButton);
				
				//hide ads
				MainActivity.hideAds();
				
				return;
			}
			
			
			
			//create credits background
			creditsBackground = new NutSprite(this);
			creditsBackground.setImage(this, getResources(), R.drawable.credits_background);
			creditsBackground.setPosition(0,0);
			
			//create menu button
			menuButton = new Button(this, "menu");
			menuButton.setPosition(196, 734);
		}
		else if (newMode == GameView.QUIT_MODE)
		{
			if (oldMode == GameView.PLAY_MODE)
			{
				//if count time thread has not started yet
				if (!countTimeThread.isAlive())
				{
					//pause time "count time thread"
					remainingStartTimeThread.pauseTime();
				}
				//if count time thread has started
				else
				{
					//pause time "count time thread"
					countTimeThread.pauseTime();
				}
			}
			
			
			//create quit background
			quitBackground = new NutSprite(this);
			quitBackground.setImage(this, getResources(), R.drawable.quit_background);
			quitBackground.setPosition(0,0);
			
			//create yes button
			yesButton = new Button(this, "yes");
			yesButton.setPosition(90, 406);
			
			//create no button
			noButton = new Button(this, "no");
			noButton.setPosition(272, 406);
			
			//show ads
			MainActivity.showAds();
		}
	}
	
	public void createBalls()
	{
		//create ball name list
		ArrayList<Integer> ballTypeList = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++)
		{
			for (int j = 0; j < 16; j++)
			{
				ballTypeList.add(i+1);
			}
		}
		Collections.shuffle(ballTypeList);
		
		//create balls
		ballList = new ArrayList<ArrayList>();
		
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{
			ArrayList<BallSprite> innerList =  new ArrayList<BallSprite>();
			
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				//BallSprite newBall = new BallSprite(this, new Random().nextInt(5)+1, column, row);
				BallSprite newBall = new BallSprite(this, ballTypeList.remove(0), column, row);
				
				innerList.add(newBall);
			}
			
			ballList.add(innerList);
		}
	}
	
	public void resetBallsPosition()
	{
		
		//move ball's down (if has space below) , set balls' column, row
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{	
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				if (ballList.get(column).get(row) != null)
				{
					BallSprite tempBall = (BallSprite)ballList.get(column).get(row);
					
					for (int i = 0; i < row; i++)
					{
						if (ballList.get(column).get(i) == null)
						{
							ballList.get(column).set(row, null);
							
							ballList.get(column).set(i, tempBall);
							tempBall.column = column;
							tempBall.row = i;
							
							break;
						}
					}
				}
			}
		}
		
		//set balls' position
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{	
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				if (ballList.get(column).get(row) != null)
				{
					BallSprite tempBall = (BallSprite)ballList.get(column).get(row);
					
					tempBall.setPosition(19+(column*56), 656-(row*57));
				}
			}
		}
	}
	
	public void setBallsGroup()
	{	
		//reset ball group
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				if (ballList.get(column).get(row) != null)
				{
					BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
					
					tempBall.group = 0;
				}
			}
		}
		
		
		//set ball's group
		int currentGroup = 0;
		
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				if (ballList.get(column).get(row) == null)
				{
					continue;
				}
				
				BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
				
				//
				if (tempBall.hasRemoved)
				{	
					continue;
				}

				//
				if (tempBall.group != 0)
				{
					continue;
				}
				
				//
				else
				{
					//
					currentGroup++;
					
					//
					tempBall.group = currentGroup;
					
					//
					ArrayList<BallSprite> checkBallList = new ArrayList<BallSprite>();
					checkBallList.add(tempBall);
					
					//
					ArrayList<BallSprite> newCheckBallList = new ArrayList<BallSprite>();
					
					//
					for (int i = 0; i < checkBallList.size(); i++)
					{
						ArrayList<BallSprite> sameAttachBallList = checkBallList.get(i).getSameAttachBalls();
						
						for (int j = 0; j < sameAttachBallList.size(); j++)
						{	
							BallSprite tempSameAttachBall = sameAttachBallList.get(j);
							
							if (tempSameAttachBall.group == 0)
							{
								tempSameAttachBall.group = currentGroup;
								
								newCheckBallList.add(tempSameAttachBall);
							}
						}
						
						if (i == checkBallList.size()-1)
						{
							checkBallList = newCheckBallList;
							newCheckBallList = new ArrayList<BallSprite>();
							i = -1;
						}
					}
					
					//
					totalGroup = currentGroup;
				}
			}
		}
		
		
		
		
		//show balls' group
		System.out.println("SHOW BALLS' GROUP");
		for (int row = GameView.TOTAL_ROW-1; row >= 0; row--)
		{
			//System.out.println();
			
			for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
			{
				System.out.print(" ");
				
				if (ballList.get(column).get(row) != null)
				{
					BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
					
					if (tempBall.group < 10)
					{
						System.out.print(" "+tempBall.group);
					}
					else
					{
						System.out.print(tempBall.group);
					}
				}
				else
				{
					System.out.print("__");
				}
			}
			
			System.out.println();
		}
		
		//System.out.println("total group:"+totalGroup);
	}
	
	public void setOrderTotal()
	{	
		////ball group list
		//set ball group list
		ballGroupList = new ArrayList<ArrayList>();
		
		for (int i = 0; i < totalGroup; i++)
		{	
			int currentGroup = i+1;
			
			ArrayList<BallSprite> innerList = new ArrayList<BallSprite>();
			
			for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
			{
				for (int row = 0; row < GameView.TOTAL_ROW; row++)
				{	
					if (ballList.get(column).get(row) != null)
					{
						BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
						
						if (tempBall.group == currentGroup)
						{
							innerList.add(tempBall);
						}
					}
				}
			}
			
			ballGroupList.add(innerList);
		}
		//show ball group list
		System.out.print("ball group list: ");
		for (int i = 0; i < ballGroupList.size(); i++)
		{
			System.out.print(" "+ballGroupList.get(i).size());
		}
		System.out.println("");
		
		////possible Order Total List
		//set possible Order Total List
		ArrayList<Integer> possibleOrderTotalList = new ArrayList<Integer>();
		for (int i = 0; i < ballGroupList.size(); i++)
		{
			int newPossibleOrderTotal = ballGroupList.get(i).size();
			
			//method 1
			possibleOrderTotalList.add(newPossibleOrderTotal);
			
			/*
			//method 2
			if (!possibleOrderTotalList.contains(newPossibleOrderTotal))
			{
				possibleOrderTotalList.add(newPossibleOrderTotal);
			}
			*/
			
		}
		//show possible Order Total List
		System.out.print("possible order total list: ");
		for (int i = 0; i < possibleOrderTotalList.size(); i++)
		{
			System.out.print(" "+possibleOrderTotalList.get(i));
		}
		System.out.println("");
		
		////set order total
		Random random = new Random();
		int randomNum = random.nextInt(possibleOrderTotalList.size());
		orderTotal = possibleOrderTotalList.get(randomNum);
		
	}
	
	public boolean isGameOver()
	{
		boolean gameIsOver = true;
		
		//1.play's wrong is max
		if (wrong == GameView.TOTAL_WRONG)
		{	
			return gameIsOver;
		}
		
		//2.player can remove all ball
		for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
		{
			for (int row = 0; row < GameView.TOTAL_ROW; row++)
			{	
				if (ballList.get(column).get(row) != null)
				{
					gameIsOver = false;
				}
			}
		}
		
		return gameIsOver;
	}
	
	@Override
	public void doEvents(MotionEvent event)
	{
		int eventaction = event.getAction();
		int xPosition = (int) ((event.getX() / getWidth()) * MainActivity.GAME_WIDTH);
		int yPosition = (int) ((event.getY() / getHeight()) * MainActivity.GAME_HEIGHT);
		
		switch (eventaction)
		{
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_UP:
				
				
				//menu mode
				if (mode == GameView.MENU_MODE)
				{
					//press play button
					if (playButton.collidePoint(xPosition, yPosition) && playButton.hasPressed)
					{
						//
						changeMode(GameView.RULES_MODE);
					}
					//press highscores button
					else if (highscoresButton.collidePoint(xPosition, yPosition) && highscoresButton.hasPressed)
					{
						//
						changeMode(GameView.HIGHSCORES_MODE);
					}
					//press credits button
					else if (creditsButton.collidePoint(xPosition, yPosition) && creditsButton.hasPressed)
					{
						//
						changeMode(GameView.CREDITS_MODE);
					}
				}
				
				//rules mode
				else if (mode == GameView.RULES_MODE)
				{
					//press menu button
					if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
					{
						//
						changeMode(GameView.MENU_MODE);
					}
					
					//press continue button
					else if (continueButton.collidePoint(xPosition, yPosition) && continueButton.hasPressed)
					{	
						//
						changeMode(GameView.START_MODE);
					}
				}
				
				//start mode
				else if (mode == GameView.START_MODE)
				{
					//press menu button
					if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
					{
						//
						changeMode(GameView.MENU_MODE);
					}
					
					//press start button (then start play)
					else if (startButton.collidePoint(xPosition, yPosition) && startButton.hasPressed)
					{	
						//
						changeMode(GameView.PLAY_MODE);
					}
				}
				//play mode
				else if (mode == GameView.PLAY_MODE)
				{
					//
					if (remainingStartTime >= 0)
					{
						return;
					}
					
					
					//press menu button
					if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
					{
						//
						changeMode(GameView.MENU_MODE);
					}
					//press replay button
					else if (replayButton.collidePoint(xPosition, yPosition) && replayButton.hasPressed)
					{
						//
						changeMode(GameView.START_MODE);
					}
				}
				
				//end mode
				else if (mode == GameView.END_MODE)
				{
					//press continue button
					if (continueButton.collidePoint(xPosition, yPosition) && continueButton.hasPressed)
					{
						//change mode to gameover mode
						changeMode(GameView.GAMEOVER_MODE);
					}
				}

				//gameover mode
				else if (mode == GameView.GAMEOVER_MODE)
				{
					
					//if menu button & replay button has shown
					if (menuButton.visible && replayButton.visible)
					{
						//press menu button
						if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
						{	
							//change mode to menu mode
							changeMode(GameView.MENU_MODE);
						}
						//press replay button
						else if (replayButton.collidePoint(xPosition, yPosition) && replayButton.hasPressed)
						{	
							//change mode to start mode
							changeMode(GameView.START_MODE);
						}
					}
					//if menu button & replay button hasn't shown
					else
					{
						//press keyboard button
						for (int i = 0; i < keyboardButtonList.size(); i++)
						{	
							KeyboardButton tempKeyboardButton = keyboardButtonList.get(i);
							
							if (tempKeyboardButton.collidePoint(xPosition, yPosition) && tempKeyboardButton.hasPressed)
							{
								//ok button
								if (tempKeyboardButton.name == "OK")
								{
									//username's size is more than 0
									if (bestTimeUsernameList.get(currentBestTimeUsernameNum).length() > 0)
									{
										//remove all keyboard button
										for (i = 0; i < keyboardButtonList.size(); i++)
										{
											this.removeSprite(keyboardButtonList.get(i));
										}
										
										//clear keyboard button list
										keyboardButtonList.clear();
										
										//remove current username arrow 
										this.removeSprite(currentUsernameArrow);
										//remove current username line 
										this.removeSprite(currentUsernameLine);
										
										//show menu button
										menuButton.setVisible(true);
										//show replay button
										replayButton.setVisible(true);
										
										//save best time record
										SharedPreferences settings = context.getSharedPreferences(GameView.PREFERENCES_NAME, 0);
										SharedPreferences.Editor editor = settings.edit();
										for (i = 0; i < GameView.TOTAL_BEST_TIME_USERNAME; i++)
										{
											editor.putString("username"+(i+1), bestTimeUsernameList.get(i));
											editor.putFloat("bestTime"+(i+1), bestTimeList.get(i));
											editor.commit();
										}
									}
								}
								//delete button
								else if (tempKeyboardButton.name == "DEL")
								{
									//username's size is more than 0
									if (bestTimeUsernameList.get(currentBestTimeUsernameNum).length() > 0)
									{
										//delete last character
										bestTimeUsernameList.set(currentBestTimeUsernameNum, 
																 bestTimeUsernameList.get(currentBestTimeUsernameNum).substring(0, bestTimeUsernameList.get(currentBestTimeUsernameNum).length() - 1));
									}
								}
								//alphabet button
								else
								{
									//username's size is less than 6
									if (bestTimeUsernameList.get(currentBestTimeUsernameNum).length() < 6)
									{
										//add character
										bestTimeUsernameList.set(currentBestTimeUsernameNum, bestTimeUsernameList.get(currentBestTimeUsernameNum) + tempKeyboardButton.name);
									}
								}
								
								break;
							}
						}
					}
				}
				//highscores mode
				else if (mode == GameView.HIGHSCORES_MODE)
				{
					//press menu button
					if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
					{	
						//change mode to menu mode
						changeMode(GameView.MENU_MODE);
					}
				}
				//credits mode
				else if (mode == GameView.CREDITS_MODE)
				{
					//press menu button
					if (menuButton.collidePoint(xPosition, yPosition) && menuButton.hasPressed)
					{	
						//change mode to menu mode
						changeMode(GameView.MENU_MODE);
					}
				}
				//quit mode
				else if (mode == GameView.QUIT_MODE)
				{
					//press yes button
					if (yesButton.collidePoint(xPosition, yPosition) && yesButton.hasPressed)
					{	
						//quit application
						mainActivity.finish();
					}
					//press no button
					else if (noButton.collidePoint(xPosition, yPosition) && noButton.hasPressed)
					{	
						//change mode
						changeMode(oldMode);
					}
				}
				
								
				
				break;
			case MotionEvent.ACTION_DOWN:
				//play mode
				if (mode == GameView.PLAY_MODE)
				{
					//
					if (remainingStartTime >= 0)
					{
						return;
					}
					
					//choose ball
					int selectedGroup = 0;
					
					for (int column = 0; column < GameView.TOTAL_COLUMN; column++)
					{
						for (int row = 0; row < GameView.TOTAL_ROW; row++)
						{
							if (ballList.get(column).get(row) != null)
							{
								BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
								
								//has not selected ball yet
								if (selectedGroup == 0)
								{
									if (tempBall.collidePoint(xPosition, yPosition))
									{
										//correct ball
										if (orderTotal == ballGroupList.get(tempBall.group-1).size())
										{
											selectedGroup = tempBall.group;
											
											//play touch sound
											Assets.playSound(touchSound);
										}
										//wrong ball
										else
										{
											//increase wrong
											wrong++;
											
											//play fault sound
											Assets.playSound(faultSound);
											
											//check is game over
											if (isGameOver())
											{	
												//change mode to end mode
												changeMode(GameView.END_MODE);
												
												return;
											}
											
											return;
										}
									}
								}
							}
						}
					}
							
					//remove ball or group of ball (if player not fail yet, and if select correct ball)
					if (wrong < GameView.TOTAL_WRONG)
					{
						if (selectedGroup != 0)
						{
							for (int column  = 0; column < GameView.TOTAL_COLUMN; column++)
							{
								for (int row  = 0; row < GameView.TOTAL_ROW; row++)
								{
									if (ballList.get(column).get(row) != null)
									{
										BallSprite tempBall = (BallSprite) ballList.get(column).get(row);
										
										if (tempBall.group == selectedGroup)
										{
											tempBall.remove();
										}
									}
								}
							}
							
							////check is game over
							//game is over
							if (isGameOver())
							{
								//change mode to end mode
								changeMode(GameView.END_MODE);
							}
							//game isn't over
							else
							{
								//set ball's position
								resetBallsPosition();
								
								//set balls' group
								setBallsGroup();
								
								//set order total
								setOrderTotal();
							}
						}
					}
				}
				
				break;
		}
	}
	
	@Override
	public void update(Canvas canvas)
	{			
		//System.out.println("update");
		//System.out.println("sprite total: "+this.spriteList.size());
		
		
		if (mode == GameView.MENU_MODE)
		{
			//show game version
			canvas.drawText("Version "+gameVersion, 18, 781, gameVersionPaint);
		}
		else if (mode == GameView.START_MODE)
		{
			canvas.drawText("?", 219, 109, orderTotalPaint);
			
			canvas.drawText("Fault: "+wrong+"/"+ GameView.TOTAL_WRONG, 30, 768, faultPaint);
			canvas.drawText("Time: "+Float.parseFloat(String.format("%.2f", countTime)), 272, 768, countTimePaint);
		}
		else if (mode == GameView.PLAY_MODE)
		{
			
			canvas.drawText("Fault: "+wrong+"/"+ GameView.TOTAL_WRONG, 30, 768, faultPaint);
			canvas.drawText("Time: "+Float.parseFloat(String.format("%.2f", countTime)), 272, 768, countTimePaint);
			
			
			//before count "3"
			if (remainingStartTime == 4)
			{
				canvas.drawText("?", 219, 109, orderTotalPaint);
			}
			//count "3"
			else if (remainingStartTime == 3)
			{
				canvas.drawText("?", 219, 109, orderTotalPaint);
				
				countThreeLabel.setVisible(true);
				countTwoLabel.setVisible(false);
				countOneLabel.setVisible(false);
				countGoLabel.setVisible(false);
			}
			//count "2"
			else if (remainingStartTime == 2)
			{
				canvas.drawText("?", 219, 109, orderTotalPaint);
				
				countThreeLabel.setVisible(false);
				countTwoLabel.setVisible(true);
				countOneLabel.setVisible(false);
				countGoLabel.setVisible(false);
			}
			//count "1"
			else if (remainingStartTime == 1)
			{
				canvas.drawText("?", 219, 109, orderTotalPaint);
				
				countThreeLabel.setVisible(false);
				countTwoLabel.setVisible(false);
				countOneLabel.setVisible(true);
				countGoLabel.setVisible(false);
			}
			//count "go"
			else if (remainingStartTime == 0)
			{
				canvas.drawText("?", 219, 109, orderTotalPaint);
				
				countThreeLabel.setVisible(false);
				countTwoLabel.setVisible(false);
				countOneLabel.setVisible(false);
				countGoLabel.setVisible(true);
			}
			//after count "go"
			else
			{	
				//start count time thread (if this thread hasn't started yet)
				if (!countTimeThread.isAlive())
				{
					//start thread
					countTimeThread.start();
					
				}
				
				//
				canvas.drawText("" +orderTotal, 219, 109, orderTotalPaint);
				
				//
				countThreeLabel.setVisible(false);
				countTwoLabel.setVisible(false);
				countOneLabel.setVisible(false);
				countGoLabel.setVisible(false);
				
				//
				menuButton.setVisible(true);
				replayButton.setVisible(true);
				
				//play "acid loops" music
				Assets.playMusic("acid_loops.mp3", true);
			}

		}
		else if (mode == GameView.END_MODE)
		{
			canvas.drawText("Fault: "+wrong+"/"+ GameView.TOTAL_WRONG, 30, 768, faultPaint);
			canvas.drawText("Time: "+Float.parseFloat(String.format("%.2f", countTime)), 272, 768, countTimePaint);
		}
		else if (mode == GameView.GAMEOVER_MODE)
		{		
			//show best time record
			for (int i = 0; i < GameView.TOTAL_BEST_TIME_USERNAME; i++)
			{	
				canvas.drawText(""+(i+1), 66, 234+(i*56), recordPaint);
				
				if (bestTimeList.get(i) == 0)
				{
					canvas.drawText("", 166, 234+(i*56), recordPaint);
				}
				else
				{
					canvas.drawText(""+bestTimeList.get(i), 166, 234+(i*56), recordPaint);
				}
				
				if (bestTimeUsernameList.get(i).length() == 0)
				{
					canvas.drawText("", 296, 234+(i*56), recordPaint);
				}
				else
				{
					canvas.drawText(""+bestTimeUsernameList.get(i), 296, 234+(i*56), recordPaint);
				}
				
			}
		}
		else if (mode == GameView.HIGHSCORES_MODE)
		{
			//show best time record
			for (int i = 0; i < GameView.TOTAL_BEST_TIME_USERNAME; i++)
			{	
				canvas.drawText(""+(i+1), 66, 234+(i*56), recordPaint);
				
				if (bestTimeList.get(i) == 0)
				{
					canvas.drawText("", 166, 234+(i*56), recordPaint);
				}
				else
				{
					canvas.drawText(""+bestTimeList.get(i), 166, 234+(i*56), recordPaint);
				}
				
				if (bestTimeUsernameList.get(i).length() == 0)
				{
					canvas.drawText("", 296, 234+(i*56), recordPaint);
				}
				else
				{
					canvas.drawText(""+bestTimeUsernameList.get(i), 296, 234+(i*56), recordPaint);
				}
				
			}
		}
	}
}


