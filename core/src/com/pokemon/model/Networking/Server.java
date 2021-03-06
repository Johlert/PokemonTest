package com.pokemon.model.Networking;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.pokemon.model.*;
import com.pokemon.model.Events.Event;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Events.MapJoinEvent;
import com.pokemon.model.Events.SplitterEvent;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.AnimationSet;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.util.HashMap;

public @Data class Server implements PostOffice{
    private HashMap<String, Connection> connections = new HashMap<>();
    private int counter = 0;
    private ServerSocket server;
    private Net net = new LwjglNet(new LwjglApplicationConfiguration());

    public Server() {
        server = net.newServerSocket(Net.Protocol.TCP, 5000, new ServerSocketHints());
        CacheForPoke.getInstance().loadMaps(new File("core/assets/maps/Prämap/maps"));
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    SocketHints sh = new SocketHints();
                    sh.connectTimeout = 1000000000;
                    sh.socketTimeout = 1000000000;

                    try {
                        Socket socket = server.accept(sh);
                        new Connection(socket);
                    } catch (Exception e) {
                    }
                }
            }
        });
        t.start();
    }

    public static void loadMapFiles(File f, ObjectOutputStream objectOutputStream) throws Exception {
        System.out.println("sending map files");
        for(File file : f.listFiles()){

            if(file.isDirectory()){
                objectOutputStream.writeObject(file);
                loadMapFiles(file, objectOutputStream);
            }else {
                FileTransferWrapper fileTransferWrapper = new FileTransferWrapper(file);
                objectOutputStream.writeObject(fileTransferWrapper);
            }
        }
    }
    public @Data class Connection{
        private boolean isListening = true;
        private Socket socket;
        private Player player;
        private ObjectOutputStream objectOutputStream;

        @SneakyThrows
        public Connection(Socket s) {
            socket = s;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(2);
            final ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            System.out.println(3);
            String name = (String) objectInputStream.readObject();
            System.out.println(name);
            if(connections.containsKey(name)){
                socket.dispose();
                System.out.println("socket disposed");
                return;
            }else {
                connections.put(name, this);
                Pokemon pokemon = CacheForPoke.getInstance().getLocalP().getGameScreen().getPokemon();
                String color = "cyan";
                TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
                TextureRegion textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));
                Player player = new Player(null, CacheForPoke.getInstance().getActiveWorld().getActiveMap(), new TextureMapObject(textureRegion),55, 9);
                CacheForPoke.getInstance().getPlayers().put(name, player);
                player.setName(name);

                player.setAnimationSet(new AnimationSet(
                        new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                        new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                        new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                        new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                        atlas.findRegion(color + "_stand_north"),
                        atlas.findRegion(color + "_stand_south"),
                        atlas.findRegion(color + "_stand_east"),
                        atlas.findRegion(color + "_stand_west")));
                System.out.println("finished loading animations");
            }
            System.out.println(2);
            //player = new Player( null, CacheForPoke.getInstance().getLocalP().getMap(), CacheForPoke.getInstance().getLocalP().getTmo(), 3 , 3);

            //player = new Player(null, null, 3 , 3);
            //send(player);
            System.out.println(4);
            File f = new File("core/assets/maps/Prämap");
            loadMapFiles(f, objectOutputStream);
            //broadcast(new MapJoinEvent(name, new Position(55, 9)));
            //send(new SplitterEvent());
            System.out.println(5);

            Player pl = CacheForPoke.getInstance().getLocalP();
            System.out.println("cachemap: " + CacheForPoke.getInstance().getActiveWorld().getActiveMap().getName());
            System.out.println("playermap: " + pl.getMap().getName());
            if(CacheForPoke.getInstance().getActiveWorld().getActiveMap().getName().equals(pl.getMap().getName())){
                send(new MapJoinEvent(pl.getName(), new Position((int) pl.getTmo().getX(), (int)pl.getTmo().getY(), pl.getMap().getName())));
                System.out.println("sending 1 event");
            }
            for(Player p : CacheForPoke.getInstance().getPlayers().values()){
                if(CacheForPoke.getInstance().getActiveWorld().getActiveMap().getName().equals(p.getMap().getName())){
                    if(!p.getName().equals(name)){
                        send(new MapJoinEvent(p.getName(), new Position((int) p.getTmo().getX(), (int)p.getTmo().getY(), p.getMap().getName())));
                    }
                }
            }

            System.out.println(3);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isListening) {
                            Event event = (Event) objectInputStream.readObject();
                            EventQueue.getINSTANCE().addEvent(event);
                        }
                        objectInputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        /**
         * All fields in s must be Serializable and all fields of the fields in s must be Serializable
         * @param s
         * @throws Exception
         */
        public void send(Serializable s) {
            try {

                objectOutputStream.writeObject(s);
                objectOutputStream.flush();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void broadcast(Event e) {
        for(String s : connections.keySet()){
            connections.get(s).send(e);
        }
    }

    public void broadcast(Event e, String name){
        for(String s : connections.keySet()){
            if(!s.equals(name)){
                connections.get(s).send(e);
            }
        }
    }
}
