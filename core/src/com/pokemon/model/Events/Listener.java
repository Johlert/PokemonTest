package com.pokemon.model.Events;

public interface Listener {
    void onMove(MoveEvent moveEvent);
    void onMapJoin(MapJoinEvent mapJoinEvent);
    //todo add all events int the listener
}
