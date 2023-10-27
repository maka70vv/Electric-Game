package com.electric.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.electric.game.ElectricGame;
import com.electric.game.Sprites.Mario;

public class GameOverScreen implements Screen {
    private final ElectricGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    public static boolean gameOver;




    public GameOverScreen(ElectricGame game){
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2);
        gameOver = true;
    }



    @Override
    public void show() {

    }


    public void update(float dt) {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        font.draw(batch, "Game Over!", 300, 300);
        font.draw(batch, "Tap to restart", 300, 250);
        batch.end();
        Mario.marioIsDead = false;
        if (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
            game.setScreen(new MainScreen(game));


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
