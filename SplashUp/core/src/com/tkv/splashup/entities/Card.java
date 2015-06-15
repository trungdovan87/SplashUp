package com.tkv.splashup.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.tkv.splashup.MyGdxGame;
import com.tkv.splashup.screens.PlayScreen;
import com.tkv.splashup.utils.config;

public class Card extends Image {
    public int cardValue;
    public int pendingValue;
    public int scoreTimes;
    public int movable;
    public boolean moving;
    public boolean disappearing;
    protected PlayScreen playScreen;
    SequenceAction action;

    public Card(PlayScreen screen, int _cardValue) {
        super(screen.atlas1.findRegion("splash", _cardValue));
        this.setWidth(60 * config.sizeMode);
        this.setHeight(60 * config.sizeMode);
        this.playScreen = screen;
        this.cardValue = _cardValue;
        this.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                if (!playScreen.isMoving() && !playScreen.isLevelup
                        && playScreen.heath >= 1 && !moving) {

                    playScreen.changeHeath(-1);
                    if (playScreen.heath == 0 && cardValue < 4) {
                        playScreen.stopGame();
                    }
                    plusPiece();

                } else if (!playScreen.isMoving() && !playScreen.isLevelup
                        && playScreen.heath == 0 && !moving) {
                    playScreen.stopGame();
                }

                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    public int getValue() {
        return cardValue;
    }

    public void setValue(int value) {
        cardValue = value;
        setDrawable(new SpriteDrawable(new Sprite(playScreen.atlas1.findRegion(
                "splash", value))));
    }

    public void plusPiece() {
        this.removeAction(action);
        float dur = 0.03f;
        dur = playScreen.speedMode == 1 ? dur
                : playScreen.speedMode == 2 ? dur / 1.5f
                : playScreen.speedMode == 3 ? dur / 2f : dur / 3f;

        moving = true;
        if (cardValue < 4) {
            final int curVal = cardValue;

            action = new SequenceAction();
            int maxCount = cardValue == 0 ? 0 : cardValue == 1 ? 18
                    : cardValue == 2 ? 18 : 13;
            cardValue += 1;

            for (int i = 1; i <= maxCount; i++) {
                final int curI = i;
                action.addAction(new RunnableAction() {
                    public void run() {
                        setDrawable(new SpriteDrawable(new Sprite(
                                playScreen.atlas1.findRegion("grow_" + curVal,
                                        curI))));
                    }
                });
                action.addAction(Actions.delay(dur));
            }
            action.addAction(new RunnableAction() {
                public void run() {
                    setDrawable(new SpriteDrawable(new Sprite(playScreen.atlas1
                            .findRegion("splash", curVal + 1))));
                    moving = false;
                    playScreen.saveGameState();
                }
            });

            addAction(action);
            if (!playScreen.isSoundOff)
                MyGdxGame.sounds.get(config.SoundGrow).play();

        } else {

            int maxCount = 7;
            playScreen.exposeSplash();
            disappearing = true;
            action = new SequenceAction();
            for (int i = 1; i <= maxCount; i++) {
                final int curI = i;
                action.addAction(new RunnableAction() {
                    public void run() {
                        setDrawable(new SpriteDrawable(new Sprite(
                                playScreen.atlas1.findRegion("grow_4", curI))));
                        // setPosition(centerX - getWidth()/2 , centerY -
                        // getHeight()/2);
                    }
                });
                action.addAction(Actions.delay(dur));
            }
            action.addAction(new RunnableAction() {
                public void run() {
                    setValue(0);
                    disappearing = false;
                    moving = false;
                    playScreen.addWaterPieces(getX() + getWidth() / 2, getY()
                            + getHeight() / 2);
                }
            });
            addAction(action);
            if (!playScreen.isSoundOff)
                MyGdxGame.sounds.get(config.SoundExpose).play();

        }

    }
}
