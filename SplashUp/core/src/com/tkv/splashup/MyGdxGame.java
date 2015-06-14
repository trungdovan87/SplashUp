package com.tkv.splashup;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.tkv.splashup.screens.PlayScreen;
import com.tkv.splashup.screens.ScreenEnum;
import com.tkv.splashup.screens.ScreenManager;
import com.tkv.splashup.utils.config;

public class MyGdxGame extends Game {
	
	public static final Vector2 VIEWPORT = new Vector2(480* config.sizeMode, 800* config.sizeMode);
	public AssetManager manager = new AssetManager();
	public static HashMap<String, Sound> sounds = new HashMap<String, Sound>();
	IActivityRequestHandler handle;
    public MyGdxGame(IActivityRequestHandler handle){
    	this.handle = handle;
    }
    public void showAds(boolean show){
    	handle.showAds(show);
    }
    public void shareFacebook(){
    	handle.shareFacebook();
    }
    public void shareLink(){
    	handle.shareLink();
    }
    
    public void logon(){
    	handle.logon();
    }
    public void logoff(){
    	handle.logoff();
    }
    public void showExitMsg(){
    	handle.showExitMsg();
    }
	public void submitScore(int score, int gamemode){
		handle.submitScore(score,gamemode);
	}
	public void showLeaderBoard(int maxScore,int gamemode){
		handle.showLeaderBoard(maxScore, gamemode);
	}
	public boolean unlockAchievement(int type, int value){
		return handle.unlockAchievement(type,value);
	}
	public void showAchievement(){
		handle.showAchievement();
	}
	public void create() {
		sounds.put(config.SoundExpose, Gdx.audio.newSound(Gdx.files
				.internal("data/sounds/sfx_expose.mp3")));
		sounds.put(config.SoundScore, Gdx.audio.newSound(Gdx.files
				.internal("data/sounds/sfx_score.mp3")));
		sounds.put(config.SoundGrow, Gdx.audio.newSound(Gdx.files
				.internal("data/sounds/sfx_grow.mp3")));
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().show(ScreenEnum.GAME);
	}

	@Override
	public void resize(int width, int height) {
		 super.resize( width, height );
	        
         if( ScreenManager.getInstance().getCurrentScreen() != null ) {
             setScreen( ScreenManager.getInstance().getCurrentScreen() );
         }
	}

	@Override
	public void render() {
		  if( ScreenManager.getInstance().getCurrentScreen() != null ) {
	              ScreenManager.getInstance().getCurrentScreen().render(Gdx.graphics.getDeltaTime());
	         }
	}

	@Override
	public void pause() {
		super.pause();

	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	@Override
	public void dispose() {
		// giai phong sounds
		for (String key : sounds.keySet()) {
			sounds.get(key).dispose();
		}

		// giai phong texture
		manager.dispose();
		ScreenManager.getInstance().dispose();
		super.dispose();
	}
}
