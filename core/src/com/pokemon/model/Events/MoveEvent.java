package com.pokemon.model.Events;


import com.pokemon.model.Direction;
import com.pokemon.model.Player;
import lombok.Data;

public @Data class MoveEvent implements Event{

    private String name;
    private Direction direction;


    @Override
    public EventType getType() {
        return EventType.PLAYERMOVEEVENT;
    }

    public MoveEvent(String name, Direction direction) {
        this.name = name;
        this.direction = direction;
    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
