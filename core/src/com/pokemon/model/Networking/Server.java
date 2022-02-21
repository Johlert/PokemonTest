package com.pokemon.model.Networking;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.pokemon.model.Events.Event;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Player;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class Server implements PostOffice{

    HashMap<String, Connection> connections = new HashMap<>();
    int counter = 0;
    ServerSocket server;
    Net net = new LwjglNet(new LwjglApplicationConfiguration());

    public Server() {
        server = net.newServerSocket(Net.Protocol.TCP, 5000, new ServerSocketHints());

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = server.accept(new SocketHints());
            }
        });
    }




    public @Data class Connection{


        @SneakyThrows
        public Connection(Socket s) throws Exception{
            socket = s;
            final ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            String name = (String) objectInputStream.readObject();
            if(connections.containsKey(name)){
                socket.dispose();
                return;
            }else {
                connections.put(name, this);
            }
            //todo load player from save if required else send new Player
            send(player);
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
        /**
         * All fields in s must be Serializable and all fields of the fields in s must be Serializable
         * @param s
         * @throws Exception
         */
        public void send(Serializable s) {
            try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
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
