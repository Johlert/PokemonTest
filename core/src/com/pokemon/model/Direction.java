package com.pokemon.model;

import java.io.Serializable;

public enum Direction implements Serializable {
    UP(0, Global.TILE_SIZE), LEFT(-Global.TILE_SIZE, 0), DOWN(0, -Global.TILE_SIZE), RIGHT(Global.TILE_SIZE, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
