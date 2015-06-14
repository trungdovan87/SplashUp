package com.tkv.splashup.utils;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.tkv.splashup.screens.PlayScreen;

public class MyStage extends Stage {
    PlayScreen screen;

    public MyStage(float width, float height, boolean keepAspecyRatio) {
        super(new FitViewport(width, height));
    }

    public void setPlayScreen(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        if (screen.step == 0) {
            screen.step = 1;
        }
        return super.touchDown(x, y, pointer, button);
    }


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        return super.touchUp(x, y, pointer, button);
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Keys.BACK) {
            screen.pressBack();
        } else if (keycode == Keys.MENU) {
            screen.showSetting();
        }
        return false;
    }
}
