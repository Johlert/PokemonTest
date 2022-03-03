package com.pokemon.model;

import lombok.Data;

import java.io.Serializable;

public @Data class Position implements Serializable {
    //this a data class in which


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;
    private Map map;


}
