package com.pokemon.model.Items;

import com.badlogic.gdx.graphics.Texture;

import java.io.Serializable;

public abstract class Item implements Serializable {

    //items are consumeble items

    private int id;
    private String name;
    private String description;

    public Item(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}
