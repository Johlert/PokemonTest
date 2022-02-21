package com.pokemon.model.Pokemon;

import com.badlogic.gdx.graphics.Texture;
import lombok.Data;

import java.util.HashMap;

public @Data class PokemonType {


    private String name;
    private int id;
    private Texture texture;
    private Type type;

    //List of Abilities that can be learned by this pokemon
    private HashMap<Integer, Ability> abilitiesPool;

    //List of evolutions
    private HashMap<Integer, PokemonType> evolutions;

    //base stats
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;



}
