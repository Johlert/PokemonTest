package com.pokemon.controller;

import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.Direction;
import com.pokemon.model.Player;
import com.pokemon.view.screens.game.GameScreen;

public class PlayerController extends InputAdapter {
    private final Player player;
    private final GameScreen gameScreen;
    private boolean up, down, left, right;

    public PlayerController(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == player.getUserSettings().getMoveUp()) {
            up = false;
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            down = false;
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            left = false;
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            right = false;
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == player.getUserSettings().getMoveUp()) {
            up = true;
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            down = true;
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            left = true;
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            right = true;
        }

        return false;
    }

    public void update(float delta) {
        if (!player.isInDialogue()) {
            if (up) {
                player.move(Direction.UP);
            } else if (down) {
                player.move(Direction.DOWN);
            } else if (left) {
                player.move(Direction.LEFT);
            } else if (right) {
                player.move(Direction.RIGHT);
            }
        }

        gameScreen.renderMap(delta);
    }
}
