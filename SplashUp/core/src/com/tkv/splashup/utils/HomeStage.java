package com.tkv.splashup.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class HomeStage extends Stage {
    public HomeStage(float width, float height, boolean keepAspecyRatio) {
        super(new FitViewport(width, height));
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {


        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean keyDown(final int keycode) {
        if (keycode == Keys.BACK) {
            // Gdx.app.log("Menu", "Quit");
            Gdx.app.exit();
        }
        return false;
    }
}
