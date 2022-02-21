package com.pokemon.model.Events;

import com.pokemon.model.CacheForPoke;

import java.util.LinkedList;

public class EventQueue {

    private static EventQueue INSTANCE;
    private volatile LinkedList<Event> events = new LinkedList<>();

    private EventQueue(){
    }

    public static EventQueue getINSTANCE() {
        if(INSTANCE == null){
            INSTANCE = new EventQueue();
        }
        return INSTANCE;
    }

    public void addEvent(Event e){
        events.add(e);
    }

    public Event popEvent(){
        return events.pop();
    }

    public LinkedList<Event> getEvents() {
        return events;
    }

}
