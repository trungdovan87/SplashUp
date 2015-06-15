package com.tkv.splashup.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.tkv.splashup.IActivityRequestHandler;
import com.tkv.splashup.MyGdxGame;

public class HtmlLauncher extends GwtApplication implements IActivityRequestHandler {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 320);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new MyGdxGame(this);
    }

    @Override
    public void showAds(boolean show) {

    }

    @Override
    public void shareFacebook() {

    }

    @Override
    public void shareLink() {

    }

    @Override
    public void logon() {

    }

    @Override
    public void logoff() {

    }

    @Override
    public void showExitMsg() {

    }

    @Override
    public void submitScore(int score, int gamemode) {

    }

    @Override
    public void showLeaderBoard(int maxScore, int gamemode) {

    }

    @Override
    public boolean unlockAchievement(int type, int value) {
        return false;
    }

    @Override
    public void showAchievement() {

    }
}