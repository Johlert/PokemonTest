package com.pokemon.model;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

public @Data class World implements Serializable {

    private String name;
    private HashMap<Integer, Map> maps;
    private HashMap<String, Player> players;


}
