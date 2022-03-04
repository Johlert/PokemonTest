package com.pokemon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import lombok.Data;

import java.io.Serializable;

public @Data class Map implements Serializable {
    private TiledMap map;
    private String name;
}
