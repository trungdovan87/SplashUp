package com.tkv.splashup.helper;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tkv.splashup.MyGdxGame;
import com.tkv.splashup.screens.PlayScreen;
import com.tkv.splashup.utils.config;

public class ResultNote extends Group {
	
	public final static int WIN = 0;
	public final static int LOSE = 1;
	public final static int COMP = 2;

	final static int WIDTH = (int)(466* config.sizeMode);
	final static int HEIGHT = (int)(466* config.sizeMode);
	
	private Image frame; 
	final private PlayScreen game;
	
	
    public ResultNote( PlayScreen game1, int score, int best, boolean isnew)
    {
    	super();
    	this.game = game1;
    	this.setPosition(0, 0);
    	
    	
 
       	
    	frame = new Image(game.atlas2.findRegion("tablebg"));
    	frame.setPosition(6* config.sizeMode,153* config.sizeMode);
    	this.addActor(frame);
    	
    	
    	
    	Image gameover = new Image(game.atlas1.findRegion("gameover"));
    	gameover.setPosition(frame.getX() + WIDTH/2 - gameover.getWidth()/2,  
    			frame.getY() + HEIGHT - 65* config.sizeMode - gameover.getHeight());
    	this.addActor(gameover);
    	
    	LabelStyle textStyle = new LabelStyle();
		textStyle.font = new BitmapFont(
				Gdx.files.internal("data/arial14black.fnt"),
				Gdx.files.internal("data/arial14black.png"), false);
		
		LabelStyle scoreStyle = new LabelStyle();
		scoreStyle.font = new BitmapFont(
				Gdx.files.internal("data/arial21Red.fnt"),
				Gdx.files.internal("data/arial21Red.png"), false);
		

    	Label lblScore = new Label("Your score: ",textStyle);
    	
     	Label lblScoreValue = new Label(Integer.toString(score),scoreStyle);
     	lblScoreValue.setColor(Color.RED);
     	lblScoreValue.setText(Integer.toString(score));
    	lblScore.setPosition(frame.getX() +WIDTH/2 - (lblScore.getX() + lblScore.getWidth() + 3* config.sizeMode + lblScoreValue.getWidth() )/2,
    			frame.getY() +HEIGHT/2 +10* config.sizeMode );
    	lblScoreValue.setPosition(lblScore.getX() + lblScore.getWidth() + 3* config.sizeMode, lblScore.getY());
    	
    
    	
    	this.addActor(lblScore);
    	this.addActor(lblScoreValue);
    
    	
    	TextureRegion region;
    	region = game.atlas1.findRegion("tryagain");
    	final Image tryagain= new Image(region);
    
    	tryagain.setPosition(frame.getX() + WIDTH/2 - tryagain.getWidth()/2 , frame.getY() + HEIGHT/2 - 50* config.sizeMode);
    	tryagain.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.resetGame();
				super.touchUp(event, x, y, pointer, button);
			}
		});
    
    	
    	
    	
    	region = game.atlas1.findRegion("rank");
    	final Image rank= new Image(region);
    	rank.setPosition(frame.getX() +WIDTH/2 - rank.getWidth()/2,frame.getY() +40* config.sizeMode);
    
    	rank.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.showLeaderBoard();
				super.touchUp(event, x, y, pointer, button);
			}
		});
    	
    	region = game.atlas1.findRegion("share");
    	final Image share= new Image(region);
    	share.setPosition(frame.getX() +30* config.sizeMode,frame.getY() +40* config.sizeMode);

    	share.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//Gdx.net.openURI("https://play.google.com/store/apps/details?id=com.tkv.stupidbird");
				game.shareFacebook();
				super.touchUp(event, x, y, pointer, button);
			}
		});
    	
    	region = game.atlas1.findRegion("rate");
    	final Image rate= new Image(region);
    	rate.setPosition(frame.getX() + WIDTH - 30* config.sizeMode - rate.getWidth(),frame.getY() +40* config.sizeMode);
    	
    	rate.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.net.openURI("market://details?id=com.tkv.splashup");
				super.touchUp(event, x, y, pointer, button);
			}
		});
    	//LoadActors();
    	this.addAction(	Actions.sequence(  
    			Actions.delay(1f ),
    			new RunnableAction(){ public void run(){
    			ResultNote.this.addActor(tryagain);}},
    			Actions.delay(0.5f ),
    			new RunnableAction(){ public void run(){
    			ResultNote.this.addActor(rate);
    			ResultNote.this.addActor(rank);
    			ResultNote.this.addActor(share);
    			}}
    			));
    }
    

  
    
}
