package com.pokemon.model;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.io.File;

public class Singleton {

    private static Singleton instance;
    private static TextureAtlas textureAtlas;

    private Singleton(){}

    public static Singleton getInstance() {
        if(textureAtlas == null){
            textureAtlas = new TextureAtlas(new FileHandle(new File("src/Asset/Packed/Textures.atlas")));
        }
        return instance;
    }

    public static TextureAtlas getTextureAtlas() {
        return textureAtlas;
    }
}
