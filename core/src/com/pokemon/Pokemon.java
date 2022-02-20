package com.pokemon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokemon.screens.menu.SplashScreen;

public class Pokemon extends Game {
    private SpriteBatch batch;

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
