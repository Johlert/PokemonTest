package com.pokemon.model.Pokemon;

import lombok.Data;

public @Data class WeaknessChart {

    private String name;
    //private HashMap<Type, Integer> chart;
    private double[][] chart;

    public WeaknessChart(){}

    public double getDamageModifier(Type attack,Type[] pokemon){
        if(pokemon.length == 1){
            return chart[attack.getId()][pokemon[0].getId()];
        }else{

        }
        return 1;
    }

}
