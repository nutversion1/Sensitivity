//started project at 18/june/2014

package com.nutslaboratory.sensitivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nutslaboratory.sensitivity.GoogleAnalyticsApplication.TrackerName;


import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends Activity 
{
	public static final int GAME_WIDTH = 480;
	public static final int GAME_HEIGHT = 800;
	public static AssetManager assets;
	public static GameView gameView;
	public static AdView adView;
	public static Tracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//set assets
		assets = getAssets();
		
		// Create the layout
        RelativeLayout layout = new RelativeLayout(this);
        
        
        //set game view
		gameView = new GameView(this, this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		gameView.setKeepScreenOn(true);
		
		// Add the game view
        layout.addView(gameView);
		
		// Create and setup the AdMob view
		adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
		
        AdRequest adRequest = new AdRequest.Builder()
        //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
        //.addTestDevice("897AFE5D3A70B640E87D9161E0142122")
        .build();
        adView.loadAd(adRequest);
		
        // Add the AdMob view
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        
        layout.addView(adView, adParams);
        
        // Hook it all up
        setContentView(layout);
        
        //hide ads
      	MainActivity.hideAds();
      	
      	//listen when ad has loaded
      	adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                
                if(gameView.mode != GameView.QUIT_MODE){
                	//hide ads
                	hideAds();
                }
            }
      	});
      	
      	//Create google analytics' tracker
      	tracker = ((GoogleAnalyticsApplication) getApplication()).getTracker(TrackerName.APP_TRACKER);
      	tracker.setScreenName("sensitivity's main activity");
      	tracker.send(new HitBuilders.AppViewBuilder().build());
     
      	
	}
	
	
	
	@Override
	public void onPause()
	{
		
		System.out.println("on pause");
		
		//if all game setup is ready
		if (gameView.gameHasStarted)
		{
			if (gameView.mode == GameView.MENU_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.HIGHSCORES_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.CREDITS_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.RULES_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.START_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.PLAY_MODE)
			{
				//if count time thread has not started yet
				if (!gameView.countTimeThread.isAlive())
				{
					//pause time "remaining time thread"
					gameView.remainingStartTimeThread.pauseTime();
				}
				//if count time thread has started
				else
				{
					//pause time "count time thread"
					gameView.countTimeThread.pauseTime();
					
					//pause music
					if(Assets.mediaPlayer != null){
						Assets.mediaPlayer.pause();
					}
				}
				
			}
			else if (gameView.mode == GameView.END_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.GAMEOVER_MODE)
			{
				//pause music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.pause();
				}
			}
			else if (gameView.mode == GameView.QUIT_MODE)
			{
				if (gameView.oldMode == GameView.PLAY_MODE)
				{
					//if count time thread has not started yet
					if (!gameView.countTimeThread.isAlive())
					{
						
					}
					//if count time thread has started
					else
					{
						//pause music
						if(Assets.mediaPlayer != null){
							Assets.mediaPlayer.pause();
						}
					}
				}
				else
				{
					//pause music
					if(Assets.mediaPlayer != null){
						Assets.mediaPlayer.pause();
					}
				}
			}

		}
		
		//hide ads
		hideAds();
		
		super.onPause();
	}
	
	@Override
	public void onResume()
	{
		System.out.println("on resume");
		
		super.onResume();
		
		
		
		//if all game setup is ready
		if (gameView.gameHasStarted)
		{
			if (gameView.mode == GameView.MENU_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.HIGHSCORES_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.CREDITS_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.RULES_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.START_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.PLAY_MODE)
			{
				//if count time thread has not started yet
				if (!gameView.countTimeThread.isAlive())
				{
					//resume time "remaining time thread"
					gameView.remainingStartTimeThread.resumeTime();
				}
				//if count time thread has started
				else
				{
					//resume time "count time thread"
					gameView.countTimeThread.resumeTime();
					
					//resume music
					if(Assets.mediaPlayer != null){
						Assets.mediaPlayer.start();
					}
				}
				
			}
			else if (gameView.mode == GameView.END_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.GAMEOVER_MODE)
			{
				//resume music
				if(Assets.mediaPlayer != null){
					Assets.mediaPlayer.start();
				}
			}
			else if (gameView.mode == GameView.QUIT_MODE)
			{
				if (gameView.oldMode == GameView.PLAY_MODE)
				{
					//if count time thread has not started yet
					if (!gameView.countTimeThread.isAlive())
					{
						
					}
					//if count time thread has started
					else
					{
						//resume music
						if(Assets.mediaPlayer != null){
							Assets.mediaPlayer.start();
						}
					}
				}
				else
				{
					//resume music
					if(Assets.mediaPlayer != null){
						Assets.mediaPlayer.start();
					}
				}
				
			}
			
		}
		
		if(gameView.mode == GameView.QUIT_MODE){
			//show ads
			showAds();
		}
		
		
	}
	
	@Override
	public void onDestroy()
	{	
		System.out.println("on destroy");
		
		//if all game setup is ready
		if (gameView.gameHasStarted)
		{
			//stop remaining start time thread
			if (gameView.remainingStartTimeThread != null)
			{
				gameView.remainingStartTimeThread.keepGoing = false;
			}
			
			//stop count time thread
			if (gameView.countTimeThread != null)
			{
				gameView.countTimeThread.keepGoing = false;
			}
			
			//release media player
			if(Assets.mediaPlayer != null){
				Assets.stopMusic();
			}
			
			//release sound pool
			if(Assets.soundPool != null){
				Assets.soundPool.release();
				Assets.soundPool = null;
			}
		}
		
		//remove ads
		removeAds();
		
		super.onDestroy();
	}
	
	//back button
	@Override
	public void onBackPressed()
	{
		//change mode to quit mode
		if (gameView.mode != GameView.QUIT_MODE)
		{
			gameView.changeMode(GameView.QUIT_MODE);
		}
	}
	
	public static void showAds(){
		if(adView != null){
		    adView.setVisibility(View.VISIBLE);
		    adView.resume();
		}

	}
	
	public static void hideAds(){
		if(adView != null){
		    adView.setVisibility(View.GONE);
		    adView.pause();
		}

	}
	
	public static void removeAds(){
		if(adView != null){
			adView.destroy();
		}

	}
	
	@Override
	public void onStart(){
		super.onStart();
		GoogleAnalytics.getInstance(MainActivity.this).reportActivityStart(this);
	}
	
	@Override
	public void onStop(){
		super.onStop();
		GoogleAnalytics.getInstance(MainActivity.this).reportActivityStop(this);
	}

	
}
