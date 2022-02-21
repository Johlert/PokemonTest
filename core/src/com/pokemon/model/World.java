package com.pokemon.model;

import lombok.Data;

import java.util.HashMap;

public @Data class World {

    private String name;
    private HashMap<Integer, Map> maps;
    private HashMap<String, Player> players;


}
