package com.pokemon.model.Events;

import java.util.LinkedList;

public class EventHandler extends Thread{

    private LinkedList<Listener> listeners = new LinkedList<>();
    private boolean isRunning = true;

    @Override
    public void run() {
        System.out.println("Event handler started");
        //here the events are used to call the methods
        while (isRunning){
            if(EventQueue.getINSTANCE().getEvents().size() != 0){

                //if there is an event in the event queue then it gets popped and the listeners get called
                Event event = EventQueue.getINSTANCE().popEvent();

                for(Listener listener : listeners){

                    if(event instanceof MoveEvent){
                        if(listener == null){
                            System.out.println("l == null");
                        }
                        assert listener != null;
                        listener.onMove((MoveEvent) event);
                    }else if(event instanceof  MapJoinEvent){
                        listener.onMapJoin((MapJoinEvent) event);
                    }

                    //todo add event calling for rest of the events

                }

            }

        }
    }

    public void addListener(Listener l){
        listeners.add(l);
    }
}
