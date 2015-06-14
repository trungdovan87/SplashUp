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

public class SettingNote extends Group {

    public final static int WIN = 0;
    public final static int LOSE = 1;
    public final static int COMP = 2;

    final static int WIDTH = (int) (466 * config.sizeMode);
    final static int HEIGHT = (int) (466 * config.sizeMode);
    final private PlayScreen game;
    Image btnChangeTable;
    Label lblChangeTable;
    Label lblChangeTableValue;
    Image btnChangeSpeed;
    Label lblChangeSpeed;
    Label lblChangeSpeedValue;
    Image btnChangeMode;
    Image btnRestart;
    boolean isMusic;
    int speedMode;
    private Image frame; //

    public SettingNote(PlayScreen game1, boolean _isMusic, int _speedMode) {
        super();
        this.game = game1;
        this.setPosition(0, 0);
        this.isMusic = _isMusic;
        this.speedMode = _speedMode;


        frame = new Image(game.atlas2.findRegion("tablebg"));
        frame.setPosition(6 * config.sizeMode, 153 * config.sizeMode);
        this.addActor(frame);


        Image changetable = new Image(game.atlas1.findRegion("settingtitle"));
        changetable.setPosition(frame.getX() + WIDTH / 2 - changetable.getWidth() / 2,
                frame.getY() + HEIGHT - 20 * config.sizeMode - changetable.getHeight());
        this.addActor(changetable);

        LabelStyle textStyle = new LabelStyle();
        textStyle.font = new BitmapFont(
                Gdx.files.internal("data/arial14black.fnt"),
                Gdx.files.internal("data/arial14black.png"), false);

        btnChangeTable = new Image(game.atlas1.findRegion("settingmenu"));

        btnChangeTable.setPosition(frame.getX(), frame.getY() + HEIGHT / 2 + 100 * config.sizeMode);
        ClickListener boardClick = new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //game.changeTableMode(4);
                isMusic = !isMusic;
                lblChangeTableValue.setText(isMusic ? "No" : "Yes");
                game.UpdateSetting(isMusic, speedMode);
                super.touchUp(event, x, y, pointer, button);
            }
        };

        lblChangeTable = new Label("Sound :", textStyle);
        lblChangeTable.setPosition(frame.getX() + WIDTH / 2 - lblChangeTable.getWidth(), btnChangeTable.getY() + 5 * config.sizeMode);
        lblChangeTableValue = new Label("Yes", textStyle);
        lblChangeTableValue.setText(isMusic ? "No" : "Yes");
        lblChangeTableValue.setPosition(frame.getX() + WIDTH / 2 + 5 * config.sizeMode, btnChangeTable.getY() + 5 * config.sizeMode);
        btnChangeTable.addListener(boardClick);
        lblChangeTable.addListener(boardClick);
        lblChangeTableValue.addListener(boardClick);


        btnChangeSpeed = new Image(game.atlas1.findRegion("settingmenu"));

        btnChangeSpeed.setPosition(frame.getX(), frame.getY() + HEIGHT / 2 + 40 * config.sizeMode);
        ClickListener moveClick = new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //game.changeTableMode(4);
                if (speedMode < 4) {
                    speedMode++;
                } else {
                    speedMode = 1;
                }
                lblChangeSpeedValue.setText(speedMode == 1 ? "Slow" : speedMode == 2 ? "Normal" : speedMode == 3 ? "Fast" : "Very Fast");
                game.UpdateSetting(isMusic, speedMode);
                super.touchUp(event, x, y, pointer, button);
            }
        };


        lblChangeSpeed = new Label("Speed :", textStyle);
        lblChangeSpeed.setPosition(frame.getX() + WIDTH / 2 - lblChangeSpeed.getWidth(), btnChangeSpeed.getY() + 5 * config.sizeMode);
        lblChangeSpeedValue = new Label("Normal", textStyle);
        lblChangeSpeedValue.setText(speedMode == 1 ? "Slow" : speedMode == 2 ? "Normal" : speedMode == 3 ? "Fast" : "Very Fast");
        lblChangeSpeedValue.setPosition(frame.getX() + WIDTH / 2 + 5 * config.sizeMode, btnChangeSpeed.getY() + 5 * config.sizeMode);

        btnChangeSpeed.addListener(moveClick);
        lblChangeSpeed.addListener(moveClick);
        lblChangeSpeedValue.addListener(moveClick);

        btnChangeMode = new Image(game.atlas1.findRegion("changemode"));
        btnChangeMode.setPosition(frame.getX(), frame.getY() + HEIGHT / 2 - 30 * config.sizeMode);
        btnChangeMode.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.hideSetting();
                game.showChangeGameMode();
                super.touchUp(event, x, y, pointer, button);
            }
        });


        btnRestart = new Image(game.atlas1.findRegion("restart"));
        // btnRestart.setWidth(btnRestart.getWidth() * 0.8f);
        // btnRestart.setHeight(btnRestart.getHeight() * 0.8f);
        btnRestart.setPosition(frame.getX() + frame.getWidth() / 2 - btnRestart.getWidth() / 2, btnChangeMode.getY() - 45 * config.sizeMode - btnRestart.getHeight());
        btnRestart.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.resetGame();
                super.touchUp(event, x, y, pointer, button);
            }
        });


        Image close = new Image(game.atlas1.findRegion("close"));

        close.setPosition(frame.getX() + frame.getWidth() / 2 - close.getWidth() / 2, frame.getY() + 40);
        close.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.hideSetting();
                super.touchUp(event, x, y, pointer, button);
            }
        });


        this.addActor(btnChangeTable);
        this.addActor(lblChangeTableValue);
        this.addActor(lblChangeTable);
        this.addActor(btnChangeSpeed);
        this.addActor(lblChangeSpeedValue);
        this.addActor(lblChangeSpeed);
        this.addActor(btnRestart);
        this.addActor(btnChangeMode);
        this.addActor(close);
    }


}
