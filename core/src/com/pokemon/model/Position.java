package com.pokemon.model;

import lombok.Data;

import java.io.Serializable;

public @Data class Position implements Serializable {
    private int x;
    private int y;
    private String mapPath;
    public Position(int x, int y, String mapPath) {
        this.x = x;
        this.y = y;
        this.mapPath = mapPath;
    }
}
