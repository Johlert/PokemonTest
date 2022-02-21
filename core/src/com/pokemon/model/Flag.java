package com.pokemon.model;

public class Flag {
    //a flag is used to track progress of the player and can have mutiple states

    private String name;
    private int state;

    public Flag(String name, int state) {
        this.name = name;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
