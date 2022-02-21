package com.pokemon.model;

import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.pokemon.controller.UserSettings;
import com.pokemon.model.Events.Listener;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Pokemon.Pokemon;
import com.pokemon.model.Pokemon.Trainer;

import java.io.Serializable;
import java.util.LinkedList;
import lombok.*;

public @Data class Player implements Serializable, Trainer, Listener {
    private UserSettings userSettings = new UserSettings();
    private TiledMap map;
    private TextureMapObject tmo;
    private String name;
    private Inventory inventory;
    private Pokemon[] pokemon;
    private Pokemon activePokemon;
    private Location location;
    private int money;
    private LinkedList<Flag> flags;
    private int speedInTilesPerSec = 2;

    public int getSpeedInTilesPerSec() {
        return speedInTilesPerSec;
    }

    @Override
    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public Player(TiledMap map, TextureMapObject tmo, int x, int y) {
        this.map = map;
        this.tmo = tmo;
        tmo.setX(x* Global.TILE_SIZE);
        tmo.setY(y* Global.TILE_SIZE);
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

        if (layer.getCell((int) (x + dx) / Global.TILE_SIZE, (int) (y + dy) / Global.TILE_SIZE) == null) {
            tmo.setX(x+dx);
            tmo.setY(y+dy);
        } else {
            System.out.println(layer.getCell((int) (x + dx) / Global.TILE_SIZE, (int) (y + dy) / Global.TILE_SIZE).getTile().getProperties());
        }
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
        if(moveEvent.getP().equals(this)){
            if(moveEvent.getDirection().equals(Direction.UP)){
                move(0, Global.TILE_SIZE);
            }
            if(moveEvent.getDirection().equals(Direction.DOWN)){
                move(0, -Global.TILE_SIZE);
            }
            if(moveEvent.getDirection().equals(Direction.LEFT)){
                move(-Global.TILE_SIZE, 0);
            }
            if(moveEvent.getDirection().equals(Direction.RIGHT)){
                move(Global.TILE_SIZE, 0);
            }
        }else {
            if(moveEvent.getDirection().equals(Direction.UP)){
                moveEvent.getP().move(0, Global.TILE_SIZE);
            }
            if(moveEvent.getDirection().equals(Direction.DOWN)){
                moveEvent.getP().move(0, -Global.TILE_SIZE);
            }
            if(moveEvent.getDirection().equals(Direction.LEFT)){
                moveEvent.getP().move(-Global.TILE_SIZE, 0);
            }
            if(moveEvent.getDirection().equals(Direction.RIGHT)){
                moveEvent.getP().move(Global.TILE_SIZE, 0);
            }

        }

    }
}
