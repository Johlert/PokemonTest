package com.pokemon.controller;

import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Direction;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Player;
import com.pokemon.view.Pokemon;
import com.pokemon.view.screens.game.GameScreen;

public class PlayerController extends InputAdapter {
    private GameScreen gameScreen;
    private final Player player;

    public PlayerController(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == player.getUserSettings().getMoveForward()) {
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.UP);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.DOWN);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.LEFT);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.RIGHT);
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
        }

        gameScreen.render(0.016f);
        return false;
    }
}
