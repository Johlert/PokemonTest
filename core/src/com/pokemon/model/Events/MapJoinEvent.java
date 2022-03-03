package com.pokemon.model.Events;

import com.pokemon.model.Map;
import com.pokemon.model.Player;
import com.pokemon.model.Position;
import lombok.Data;

public @Data
class MapJoinEvent implements Event{

    public MapJoinEvent(String name, Position position){
        this.name = name;
        this.position = position;
    }

    private String name;
    private Position position;

    @Override
    public EventType getType() {
        return EventType.PLAYERJOINMAPEVENT;
    }
}
