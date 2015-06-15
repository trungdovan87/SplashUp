package com.tkv.splashup;

public interface IActivityRequestHandler {
    public void showAds(boolean show);

    public void shareFacebook();

    public void shareLink();

    public void logon();

    public void logoff();

    public void showExitMsg();

    public void submitScore(int score, int gamemode);

    public void showLeaderBoard(int maxScore, int gamemode);

    public boolean unlockAchievement(int type, int value);

    public void showAchievement();
}
