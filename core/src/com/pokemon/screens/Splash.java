package com.pokemon.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokemon.Pokemon;
import com.pokemon.tween.SpriteAccessor;

public class Splash implements Screen {
    private Pokemon pokemon;
    private SpriteBatch batch;
    private Sprite sprite;
    private TweenManager manager;

    public Splash(Pokemon pokemon) {
        this.pokemon = pokemon;
        manager = new TweenManager();
        batch = pokemon.getBatch();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    }

    @Override
    public void show() {
        sprite = new Sprite(new Texture(Gdx.files.internal("core/assets/badlogic.jpg")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(sprite, SpriteAccessor.ALPHA).target(0).start(manager);
        Tween.to(sprite, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                pokemon.setScreen(new MainMenu(pokemon));
            }
        }).start(manager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        manager.update(delta);

        batch.begin();
        sprite.draw(batch);
        batch.end();
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
        sprite.getTexture().dispose();
    }
}
