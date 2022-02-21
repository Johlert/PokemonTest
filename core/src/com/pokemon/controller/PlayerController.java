package com.pokemon.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Direction;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Player;
import com.pokemon.model.Global;

public class PlayerController extends InputAdapter {
    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == CacheForPoke.getInstance().getLocalP().getUserSettings().getMoveForward()){
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.UP);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
            //player.move(0, Global.TILE_SIZE);
        }

        if (keycode == CacheForPoke.getInstance().getLocalP().getUserSettings().getMoveDown()){
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.DOWN);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
            //player.move(0, -Global.TILE_SIZE);
        }

        if (keycode == CacheForPoke.getInstance().getLocalP().getUserSettings().getMoveLeft()){
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.LEFT);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
            //player.move(-Global.TILE_SIZE, 0);
        }

        if (keycode == CacheForPoke.getInstance().getLocalP().getUserSettings().getMoveRight()){
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.RIGHT);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
            //player.move(Global.TILE_SIZE, 0);
        }

        return false;
    }
}
