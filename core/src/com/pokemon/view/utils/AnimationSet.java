package com.pokemon.view.utils;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.pokemon.model.Direction;

import java.util.HashMap;
import java.util.Map;

public class AnimationSet {
    private Map<Direction, Animation<TextureAtlas.AtlasRegion>> walking;
    private Map<Direction, TextureRegion> standing;

    public AnimationSet(Animation<TextureAtlas.AtlasRegion> walkNorth,
                        Animation<TextureAtlas.AtlasRegion> walkSouth,
                        Animation<TextureAtlas.AtlasRegion> walkEast,
                        Animation<TextureAtlas.AtlasRegion> walkWest,
                        TextureRegion standNorth,
                        TextureRegion standSouth,
                        TextureRegion standEast,
                        TextureRegion standWest) {
        walking = new HashMap<>();
        walking.put(Direction.UP, walkNorth);
        walking.put(Direction.DOWN, walkSouth);
        walking.put(Direction.RIGHT, walkEast);
        walking.put(Direction.LEFT, walkWest);
        standing = new HashMap<>();
        standing.put(Direction.UP, standNorth);
        standing.put(Direction.DOWN, standSouth);
        standing.put(Direction.RIGHT, standEast);
        standing.put(Direction.LEFT, standWest);
    }

    public Animation<TextureAtlas.AtlasRegion> getWalking(Direction dir) {
        return walking.get(dir);
    }

    public TextureRegion getStanding(Direction dir){
        return standing.get(dir);
    }
}
