package com.pokemon.controller;

import com.badlogic.gdx.Input;
import lombok.Data;

public @Data
class UserSettings {
    private int moveUp = Input.Keys.UP;
    private int moveLeft = Input.Keys.LEFT;
    private int moveDown = Input.Keys.DOWN;
    private int moveRight = Input.Keys.RIGHT;
    private int a = Input.Keys.Y;
    private int b = Input.Keys.X;
}
