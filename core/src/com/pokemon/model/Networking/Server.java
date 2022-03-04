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
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.AnimationSet;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
import java.util.HashMap;

public @Data class Server implements PostOffice{

    HashMap<String, Connection> connections = new HashMap<>();
    int counter = 0;
    ServerSocket server;
    Net net = new LwjglNet(new LwjglApplicationConfiguration());

    public Server() {
        server = net.newServerSocket(Net.Protocol.TCP, 5000, new ServerSocketHints());

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


        @SneakyThrows
        public Connection(Socket s) throws Exception{
            socket = s;
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println(2);
            final ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            System.out.println(3);
            String name = (String) objectInputStream.readObject();
            System.out.println(name);
            if(connections.containsKey(name)){
                socket.dispose();
                return;
            }else {
                connections.put(name, this);
                Pokemon pokemon = CacheForPoke.getInstance().getLocalP().getGameScreen().getPokemon();
                String color = "cyan";
                TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
                TextureRegion textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));
                Player player = new Player(null, CacheForPoke.getInstance().getLocalP().getMap().getMap(), new TextureMapObject(textureRegion),55, 9);
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
            }
            System.out.println(2);
            //todo load player from save if required else send new Player

            //player = new Player( null, CacheForPoke.getInstance().getLocalP().getMap(), CacheForPoke.getInstance().getLocalP().getTmo(), 3 , 3);


            //player = new Player(null, null, 3 , 3);
            //send(player);
            System.out.println(4);
            File f = new File("core/assets/maps/Prämap");
            loadMapFiles(f, objectOutputStream);
            //broadcast(new MapJoinEvent(name, new Position(55, 9)));

            send(new MapJoinEvent(CacheForPoke.getInstance().getLocalP().getName(), new Position((int) CacheForPoke.getInstance().getLocalP().getTmo().getX(), (int) CacheForPoke.getInstance().getLocalP().getTmo().getY(), CacheForPoke.getInstance().getLocalP().getMap().getName())));
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
        boolean isListening = true;
        Socket socket;
        Player player;
        ObjectOutputStream objectOutputStream;
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
}
