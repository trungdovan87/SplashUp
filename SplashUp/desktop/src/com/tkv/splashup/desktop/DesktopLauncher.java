package com.tkv.splashup.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tkv.splashup.IActivityRequestHandler;
import com.tkv.splashup.MyGdxGame;

public class DesktopLauncher implements IActivityRequestHandler {
    private static DesktopLauncher application;

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Splash Up";
        cfg.useGL30 = false;
        //cfg.width = 480;
        //cfg.height = 800;
        cfg.width = 1024;
        cfg.height = 1920;

        if (application == null) {
            application = new DesktopLauncher();
        }

        new LwjglApplication(new MyGdxGame(application), cfg);
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
    public void submitScore(int score, int tableWidth) {

    }

    @Override
    public void showLeaderBoard(int maxScore, int tableWidth) {

    }

    @Override
    public void showExitMsg() {

    }

    @Override
    public boolean unlockAchievement(int type, int value) {
        return false;
    }

    @Override
    public void showAchievement() {

    }


}