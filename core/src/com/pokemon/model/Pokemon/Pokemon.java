package com.pokemon.model.Pokemon;

import lombok.Data;

public @Data class Pokemon {
    private int level;
    private int exp;
    private String name;

    private StatusEffect statusEffect;
    private Ability[] abilities;
    private PokemonType pokemonType;

    //stats
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
}
