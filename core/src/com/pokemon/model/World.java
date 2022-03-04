package com.pokemon.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;


public @Data class World implements Serializable {

    private String name;
    private HashMap<String, Map> maps = new HashMap<>();
    //private HashMap<String, Player> players = new HashMap<>();
    private Map activeMap;


}
