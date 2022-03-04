package com.pokemon.model;

import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.pokemon.model.Events.EventHandler;
import com.pokemon.model.Items.Item;
import com.pokemon.model.Networking.PostOffice;
import com.pokemon.model.Pokemon.Gen;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
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
    private HashMap<String, Player> players = new HashMap<>();
    private World activeWorld;
    private EventHandler handler = new EventHandler();
    private PostOffice postOffice;



    private CacheForPoke(){
        handler.start();
    }

    public void loadMaps(File path){
        TmxMapLoader tmx = new TmxMapLoader();
        activeWorld = new World();
        for(File f : path.listFiles()){
            Map map = new Map();
            map.setName(f.getName().substring(0, f.getName().length() - 4));
            map.setMap(tmx.load(f.getPath()));
            activeWorld.getMaps().put(f.getName().substring(0, f.getName().length() - 4), map);
            System.out.println(f.getName().substring(0, f.getName().length() - 4));
        }
        activeWorld.setActiveMap(activeWorld.getMaps().get("PRZCITY"));
    }

    public static CacheForPoke getInstance() {

        if(instance == null){
            instance = new CacheForPoke();
        }

        return instance;
    }
}
