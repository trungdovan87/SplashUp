package com.tkv.splashup.screens;

import com.badlogic.gdx.Screen;

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
    },

    TEST {
        @Override
        protected Screen getScreenInstance() {
            return new TestScreen();
        }
    };

    protected abstract com.badlogic.gdx.Screen getScreenInstance();
}
