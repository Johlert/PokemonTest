package com.pokemon.model.Items.PokeBalls;

import com.badlogic.gdx.graphics.Texture;
import com.pokemon.model.Items.Item;
import lombok.Getter;
import lombok.Setter;

public @Getter @Setter class PokeBall extends Item {
    //pokeballs are used only in combat. They are 1 time use items
    private int captureModifier;

    private Texture texture;

    public PokeBall(String name, String description, int captureModifier, Texture texture, int id) {
        super(name, description, id);
        this.texture = texture;


        this.captureModifier = captureModifier;
    }

    public int getCaptureModifier() {
        return captureModifier;
    }
}
