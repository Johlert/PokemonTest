package com.pokemon.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.Player;
import com.pokemon.model.Settings;

public class PlayerController extends InputAdapter {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.W){
            player.move(0, Settings.TILE_SIZE);
        }

        if (keycode == Input.Keys.S){
            player.move(0, -Settings.TILE_SIZE);
        }

        if (keycode == Input.Keys.A){
            player.move(-Settings.TILE_SIZE, 0);
        }

        if (keycode == Input.Keys.D){
            player.move(Settings.TILE_SIZE, 0);
        }

        return false;
    }
}
