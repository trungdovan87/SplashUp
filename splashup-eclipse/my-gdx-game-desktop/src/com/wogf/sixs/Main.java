package com.wogf.sixs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tkv.splashup.IActivityRequestHandler;
import com.tkv.splashup.MyGdxGame;

public class Main implements IActivityRequestHandler {
	 private static Main application;
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Splash Up";
		cfg.useGL20 = false;
//		cfg.width = 480;
//		cfg.height = 800;
		cfg.width = 1024;
		cfg.height = 1920;
		  if (application == null) {
	            application = new Main();
	        }
	               
		new LwjglApplication(new MyGdxGame(application), cfg);
	}
	 @Override
	    public void showAds(boolean show) {
	        // TODO Auto-generated method stub
	        
	    }
	 @Override
	 public void shareFacebook() {
	 }
	@Override
	public void shareLink() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void logon() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void logoff() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void submitScore(int score, int tableWidth) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showLeaderBoard(int maxScore, int tableWidth) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void showExitMsg() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean unlockAchievement(int type, int value) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void showAchievement() {
		// TODO Auto-generated method stub
		
	}


	 
}
