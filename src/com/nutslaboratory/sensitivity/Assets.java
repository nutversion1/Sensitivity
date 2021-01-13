package com.nutslaboratory.sensitivity;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class Assets {
	public static SoundPool soundPool; 
	public static MediaPlayer mediaPlayer;
	
	public static int loadSound(String filename) {
		int soundID = 0;
		
		if (soundPool == null) {
			soundPool = new SoundPool(25, AudioManager.STREAM_MUSIC, 0);
		}
		
		try {
			soundID = soundPool.load(MainActivity.assets.openFd(filename), 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return soundID;
		
	}
	
	public static void playSound(int soundID) {
		soundPool.play(soundID, 1, 1, 1, 0, 1);
	}
	
	public static void playMusic(String fileName, boolean looping) {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		
		try {
			AssetFileDescriptor afd = MainActivity.assets.openFd(fileName);
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.setLooping(looping);
			mediaPlayer.start();
		} catch (Exception e) {
			System.out.println("play music error.");
			e.printStackTrace();
		}
	}
	
	
	
	public static void stopMusic(){
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
		
	}
}
