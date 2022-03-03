package com.pokemon.model.Events;

import com.pokemon.model.Map;
import com.pokemon.model.Player;
import lombok.Data;

public @Data
class MapJoinEvent implements Event{

    private Map map;
    private String name;

    @Override
    public EventType getType() {
        return EventType.PLAYERJOINMAPEVENT;
    }
}
