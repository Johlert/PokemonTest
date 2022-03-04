package com.pokemon.model.Pokemon;

import lombok.Data;

public @Data class Ability {
    private int id;
    private String name;
    private int power;
    private int accuracy;
    private Type type;
    private boolean isSpecial;
}
