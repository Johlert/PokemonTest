package com.pokemon.model.Events;


import com.pokemon.model.Direction;
import com.pokemon.model.Player;

public class MoveEvent implements Event{

    private Player p;
    private Direction direction;


    @Override
    public EventType getType() {
        return EventType.PLAYERMOVEEVENT;
    }

    public MoveEvent(Player p, Direction direction) {
        this.p = p;
        this.direction = direction;
    }

    public Player getP() {
        return p;
    }

    public void setP(Player p) {
        this.p = p;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
