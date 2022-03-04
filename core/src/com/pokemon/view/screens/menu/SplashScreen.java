package com.pokemon.view.screens.menu;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.tween.SpriteAccessor;

public class SplashScreen implements Screen {
    private final Pokemon pokemon;
    private final SpriteBatch batch;
    private final TweenManager manager;
    private Sprite sprite;

    public SplashScreen(Pokemon pokemon) {
        this.pokemon = pokemon;
        manager = new TweenManager();
        batch = pokemon.getBatch();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    }

    @Override
    public void show() {
        sprite = new Sprite(new Texture(Gdx.files.internal("core/assets/images/pokeball.png")));
        sprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Tween.set(sprite, SpriteAccessor.ALPHA).target(0).start(manager);
        Tween.to(sprite, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1, 2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                pokemon.setScreen(new MainMenuScreen(pokemon));
            }
        }).start(manager);
    }

    @Override
    public void render(float delta) {
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
