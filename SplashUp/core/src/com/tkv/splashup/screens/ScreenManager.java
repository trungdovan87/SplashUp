package com.tkv.splashup.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.IntMap;

public final class ScreenManager {

    private static ScreenManager instance;

    private Game game;

    private IntMap<Screen> screens;
    private Screen curScreen;

    private ScreenManager() {
        screens = new IntMap<Screen>();
    }

    public static ScreenManager getInstance() {
        if (null == instance) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Screen getCurrentScreen() {
        return curScreen;
    }

    public void show(ScreenEnum screen) {
        if (game == null) return;
        if (!screens.containsKey(screen.ordinal())) {
            screens.put(screen.ordinal(), screen.getScreenInstance());
        }
        curScreen = screens.get(screen.ordinal());
        game.setScreen(curScreen);
    }

    public void dispose(ScreenEnum screen) {
        if (!screens.containsKey(screen.ordinal())) return;
        screens.remove(screen.ordinal()).dispose();
    }

    public void dispose() {
        for (com.badlogic.gdx.Screen screen : screens.values()) {
            screen.dispose();
        }
        screens.clear();
        instance = null;
    }
}