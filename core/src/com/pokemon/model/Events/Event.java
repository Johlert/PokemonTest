package com.pokemon.model.Events;

import java.io.Serializable;

public interface Event extends Serializable {
    EventType getType();

    //todo
}
