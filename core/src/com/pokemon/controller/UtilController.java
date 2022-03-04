package com.pokemon.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pokemon.view.screens.game.GameScreen;

public class UtilController extends InputAdapter {
    private final GameScreen gamescreen;
    private boolean collisionVisible = false;

    public UtilController(GameScreen gameScreen) {
        this.gamescreen = gameScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            gamescreen.getPlayer().setANIM_DUR(0.1f);
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            gamescreen.getPlayer().setANIM_DUR(0.5f);
        }

        if (keycode == Input.Keys.F12) {
            collisionVisible = !collisionVisible;
            gamescreen.getMap().getMap().getLayers().get("Collision Layer").setVisible(collisionVisible);
        }

        return false;
    }
}
