package com.pokemon.model.Events;

import com.pokemon.model.Direction;
import lombok.Data;

public @Data class FacingEvent implements Event{

    private Direction direction;
    private String name;

    public FacingEvent(Direction direction, String name) {
        this.direction = direction;
        this.name = name;
    }

    @Override
    public EventType getType() {
        return EventType.PLAYERFACINGEVENT;
    }
}
