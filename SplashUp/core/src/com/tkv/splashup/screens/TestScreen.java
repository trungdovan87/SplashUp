package com.tkv.splashup.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tkv.splashup.MyGdxGame;
import com.tkv.splashup.utils.MyStage;

/**
 * Created by trungdovan on 14/06/2015.
 */
public class TestScreen implements Screen {
    protected MyStage stage;

    public TestScreen(){
        stage = new MyStage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        //stage = new MyStage(0, 0, true);
        //stage.setPlayScreen(this);
        MyGdxGame game = (MyGdxGame) ScreenManager.getInstance().getGame();
        game.manager.load("data/mymota.txt", TextureAtlas.class);
        game.manager.load("data/mymota1.txt", TextureAtlas.class);

        Texture badlogic = new Texture("badlogic.jpg");

        game.manager.finishLoading();

        TextureAtlas atlas1 = game.manager.get("data/mymota.txt", TextureAtlas.class);
        TextureAtlas atlas2 = game.manager.get("data/mymota1.txt", TextureAtlas.class);
        Image image = new Image(atlas2.findRegion("background1"));
        //Image image = new Image(new TextureRegion(badlogic));
        image.setWidth(200);
        image.setHeight(200);
        stage.addActor(image);
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {
        stage.act(delta);

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // draw the actors
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
