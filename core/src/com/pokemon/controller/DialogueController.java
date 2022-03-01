package com.pokemon.controller;

import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.Player;
import com.pokemon.view.screens.game.GameScreen;
import com.pokemon.view.utils.dialogue.DialogueBox;

public class DialogueController extends InputAdapter {
    private final GameScreen gameScreen;
    private final DialogueBox box;
    private final Player player;

    public DialogueController(GameScreen gameScreen, DialogueBox box, Player player) {
        this.gameScreen = gameScreen;
        this.box = box;
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == player.getUserSettings().getA()) {
            if (!box.isFinished()) {
                box.forceFinish();
            } else if (box.isFinished()) {
                box.setVisible(false);
                player.setInDialogue(false);
            }
        }
        return false;
    }
}
