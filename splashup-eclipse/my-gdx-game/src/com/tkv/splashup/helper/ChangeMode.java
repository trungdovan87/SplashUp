package com.tkv.splashup.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tkv.splashup.screens.PlayScreen;
import com.tkv.splashup.utils.config;

public class ChangeMode extends Group {
	final static int WIDTH = (int)(466* config.sizeMode);
	final static int HEIGHT = (int)(466* config.sizeMode);
	
	private Image frame; //
	final private PlayScreen game;
	Image btnChangeGameMode;
	Label lblChangeGameMode;
	Label lblChangeGameModeValue;
	int gameMode;
	
    public ChangeMode( PlayScreen game1, int _gameMode)
    {
    	super();
    	this.game = game1;
    	this.setPosition(0, 0);
    	this.gameMode = _gameMode;
    	
    	
    	frame = new Image(game.atlas2.findRegion("tablebg"));
    	frame.setPosition(6* config.sizeMode,153* config.sizeMode);
    	this.addActor(frame);
    	

    	LabelStyle textStyle = new LabelStyle();
		textStyle.font = new BitmapFont(
				Gdx.files.internal("data/arial14black.fnt"),
				Gdx.files.internal("data/arial14black.png"), false);

    	btnChangeGameMode = new Image(game.atlas1.findRegion("settingmenu"));
    
    	btnChangeGameMode.setPosition(frame.getX() , frame.getY() + HEIGHT/2 + 40* config.sizeMode);
    	ClickListener boardClick = new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//game.ChangeGameModeMode(4);
				gameMode = gameMode == 1 ?  3 : 1;
				lblChangeGameModeValue.setText(gameMode == 1 ? "Normal" : "For Children");
				super.touchUp(event, x, y, pointer, button);
			}
		};
    	
    	lblChangeGameMode = new Label("Game Mode :",textStyle);
    	lblChangeGameMode.setPosition(frame.getX() + WIDTH/2 -lblChangeGameMode.getWidth(),btnChangeGameMode.getY()+5* config.sizeMode);
    	lblChangeGameModeValue = new Label("Yes",textStyle);
    	lblChangeGameModeValue.setText(gameMode == 1 ? "Normal" : "For Children");
    	lblChangeGameModeValue.setPosition(frame.getX() + WIDTH/2 +5* config.sizeMode,btnChangeGameMode.getY()+5* config.sizeMode);
       	btnChangeGameMode.addListener(boardClick);
       	lblChangeGameMode.addListener(boardClick);
       	lblChangeGameModeValue.addListener(boardClick);
    	
    	
    
    	
    	 Image ok = new Image(game.atlas1.findRegion("ok"));
         
    	 ok.setPosition(frame.getX() + 50* config.sizeMode , frame.getY() +50* config.sizeMode);
    	 ok.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.UpdateGameMode(gameMode);
				super.touchUp(event, x, y, pointer, button);
			}
		});
    	 Image cancel = new Image(game.atlas1.findRegion("cancel"));
         
    	 cancel.setPosition(frame.getX() + WIDTH - 50* config.sizeMode -  cancel.getWidth(), frame.getY() +50* config.sizeMode);
    	 cancel.addListener(new ClickListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.hideChangeGameMode();
				super.touchUp(event, x, y, pointer, button);
			}
		});
    
    
    	 
    	 
		this.addActor(btnChangeGameMode);
		this.addActor(lblChangeGameModeValue);
		this.addActor(lblChangeGameMode);
		this.addActor(ok);
		this.addActor(cancel);
    }
}
