package com.pokemon.model;

public enum PlayerColor {
    RED("red"), GREEN("green"), YELLOW("yellow"), PURPLE("purple"), PINK("pink"), LIGHT_GREEN("light_green"), BROWN("brown"), CYAN("cyan");

    private final String color;

    PlayerColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
