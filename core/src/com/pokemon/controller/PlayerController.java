package com.pokemon.controller;

import com.badlogic.gdx.InputAdapter;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Direction;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Player;
import com.pokemon.view.screens.game.GameScreen;

public class PlayerController extends InputAdapter {
    private GameScreen gameScreen;
    private final Player player;
    private boolean up, down, left, right;

    public PlayerController(GameScreen gameScreen, Player player) {
        this.gameScreen = gameScreen;
        this.player = player;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == player.getUserSettings().getMoveUp()) {
            up = false;
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            down = false;
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            left = false;
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            right = false;
        }

        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == player.getUserSettings().getMoveUp()) {
            up = true;
        }

        if (keycode == player.getUserSettings().getMoveDown()) {
            down = true;
        }

        if (keycode == player.getUserSettings().getMoveLeft()) {
            left = true;
        }

        if (keycode == player.getUserSettings().getMoveRight()) {
            right = true;
        }

        return false;
    }

    public void update(float delta) {
        if (up) {
            //MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.UP);
            //CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            //EventQueue.getINSTANCE().addEvent(mv);
            player.move(Direction.UP);
        } else if (down) {
            //MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.DOWN);
            //CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            //EventQueue.getINSTANCE().addEvent(mv);
            player.move(Direction.DOWN);
        } else if (left) {
            //MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.LEFT);
            //CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            //EventQueue.getINSTANCE().addEvent(mv);
            player.move(Direction.LEFT);
        } else if (right) {
            //MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), Direction.RIGHT);
            //CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            //EventQueue.getINSTANCE().addEvent(mv);
            player.move(Direction.RIGHT);
        }

        gameScreen.renderMap(delta);
    }
}
