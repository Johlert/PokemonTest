package com.pokemon.controller;

import com.badlogic.gdx.Input;

public class UserSettings {

    //todo the settings will be saved here the static settings are a placeholder!
    private int moveUp = Input.Keys.UP;
    private int moveLeft = Input.Keys.LEFT;
    private int moveDown = Input.Keys.DOWN;
    private int moveRight = Input.Keys.RIGHT;



    public int getMoveUp() {
        return moveUp;
    }

    public int getMoveLeft() {
        return moveLeft;
    }

    public int getMoveDown() {
        return moveDown;
    }

    public int getMoveRight() {
        return moveRight;
    }
}
