package com.pokemon.model.Events;


import com.pokemon.model.Direction;
import com.pokemon.model.Player;
import com.pokemon.model.Position;
import lombok.Data;

public @Data class MoveEvent implements Event{

    private String name;
    private Direction direction;
    private Position pos;


    @Override
    public EventType getType() {
        return EventType.PLAYERMOVEEVENT;
    }

    public MoveEvent(String name, Direction direction, Position position) {
        this.name = name;
        this.direction = direction;
        pos = position;
    }


    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
