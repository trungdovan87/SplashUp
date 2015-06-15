package com.tkv.splashup.screens;

public enum ScreenEnum {


    HOME_MENU {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
            return new HomeScreen();
        }
    },

    GAME {
        @Override
        protected com.badlogic.gdx.Screen getScreenInstance() {
            return new PlayScreen();
        }
    };

    protected abstract com.badlogic.gdx.Screen getScreenInstance();
}
