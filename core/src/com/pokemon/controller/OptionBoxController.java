package com.pokemon.controller;

import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.Player;
import com.pokemon.view.screens.game.GameScreen;
import com.pokemon.view.utils.dialogue.OptionBox;

public class OptionBoxController extends InputAdapter {
    private final GameScreen gameScreen;
    private final OptionBox box;
    private final Player player;

    public OptionBoxController(GameScreen gameScreen, OptionBox box, Player player) {
        this.gameScreen = gameScreen;
        this.box = box;
        this.player = player;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == player.getUserSettings().getMoveUp()) {
            box.moveUp();
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            box.moveDown();
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            box.moveLeft();
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            box.moveRight();
        }

        return false;
    }
}
