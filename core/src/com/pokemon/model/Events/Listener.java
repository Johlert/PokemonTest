package com.pokemon.model.Events;

public interface Listener {
    void onMove(MoveEvent moveEvent);
    void onMapJoin(MapJoinEvent mapJoinEvent);
    void onPlayerFacing(FacingEvent facingEvent);
}
