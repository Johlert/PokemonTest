package com.pokemon.model.Pokemon;

import java.util.HashMap;

public class Gen {

    private String name;
    private HashMap<Integer, Pokemon> pokemons = new HashMap<>();
    private WeaknessChart weaknessChart = new WeaknessChart();


    public Gen(String name, String weaknessName){
        this.name = name;
        weaknessChart.setName(weaknessName);
    }

    public String getName() {
        return name;
    }

    public HashMap<Integer, Pokemon> getPokemons() {
        return pokemons;
    }

    public WeaknessChart getWeaknessChart() {
        return weaknessChart;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPokemons(HashMap<Integer, Pokemon> pokemons) {
        this.pokemons = pokemons;
    }

    public void setWeaknessChart(WeaknessChart weaknessChart) {
        this.weaknessChart = weaknessChart;
    }

    public Gen(){
    }
}
