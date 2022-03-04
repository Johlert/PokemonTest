package com.pokemon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;

public @Data class Map implements Serializable {

    TiledMap map;
    String name;

}
