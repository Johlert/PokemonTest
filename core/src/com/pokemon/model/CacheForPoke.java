package com.pokemon.model;

import com.pokemon.model.Events.EventHandler;
import com.pokemon.model.Items.Item;
import com.pokemon.model.Networking.PostOffice;
import com.pokemon.model.Pokemon.Gen;
import lombok.Data;
import java.util.HashMap;


/**
 * THis is the cache where the data is loaded into eg all the different info that is required to run
 */
public @Data class CacheForPoke {
    //todo ensure that the local player is added into here
    private Player localP;
    private static CacheForPoke instance;
    private HashMap<String, Gen> gens = new HashMap<>();
    private HashMap<Integer, Item> items = new HashMap<>();
    private HashMap<String, World> worlds = new HashMap<>();
    private World activeWorld;
    private EventHandler handler = new EventHandler();
    private PostOffice postOffice;


    private CacheForPoke(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CacheForPoke.this.load();
            }
        }).start();
        handler.start();
    }

    private void load(){

    }

    public static CacheForPoke getInstance() {

        if(instance == null){
            instance = new CacheForPoke();
        }

        return instance;
    }
}
