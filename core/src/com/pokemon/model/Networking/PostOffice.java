package com.pokemon.model.Networking;

import com.pokemon.model.Events.Event;

import java.io.ObjectOutputStream;
import java.io.Serializable;

public interface PostOffice {
    public void broadcast(Event e);
}
