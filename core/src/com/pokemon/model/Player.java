package com.pokemon.model;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Player {
    private TiledMap map;
    private TextureMapObject tmo;

    public Player(TiledMap map, TextureMapObject tmo, int x, int y) {
        this.map = map;
        this.tmo = tmo;
        tmo.setX(x*Settings.TILE_SIZE);
        tmo.setY(y*Settings.TILE_SIZE);
    }

    public int getX() {
        return (int) tmo.getX();
    }

    public int getY() {
        return (int) tmo.getY();
    }

    public void move(int dx, int dy) {
        float x = tmo.getX();
        float y = tmo.getY();

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Collision Layer");

        if (layer.getCell((int) (x + dx) / Settings.TILE_SIZE, (int) (y + dy) / Settings.TILE_SIZE) == null) {
            tmo.setX(x+dx);
            tmo.setY(y+dy);
        }
    }
}
