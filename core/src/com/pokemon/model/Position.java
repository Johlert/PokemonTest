package com.pokemon.model;

import lombok.Data;

import java.io.Serializable;

public @Data class Position implements Serializable {
    //this a data class in which


    public Position(int x, int y, String mapPath) {
        this.x = x;
        this.y = y;
        this.mapPath = mapPath;
    }

    private int x;
    private int y;
    private String mapPath;


}
