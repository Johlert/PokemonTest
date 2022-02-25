package com.pokemon.model.Networking;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Events.Event;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Player;
import lombok.SneakyThrows;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Client implements PostOffice {


    Socket socket;
    Net net = new LwjglNet(new LwjglApplicationConfiguration());

    public boolean connect(String ip, String playername) {
        socket = net.newClientSocket(Net.Protocol.TCP, ip, 5000, new SocketHints());
        if(socket.isConnected()){
            new Listener(socket, playername);
        }
        return socket.isConnected();
    }

    /**
     * All fields in s must be Serializable and all fields of the fields in s must be Serializable
     *
     * @param s
     * @throws Exception
     */
    public void send(Serializable s){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(s);
            objectOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public class Listener {
        @SneakyThrows
        public Listener(Socket s, String localPlayer) {
            socket = s;
            send(localPlayer);
            System.out.println(3);
            final ObjectInputStream objectInputStream = new ObjectInputStream(s.getInputStream());
            System.out.println(1);
            Player player = (Player) objectInputStream.readObject();
            System.out.println(2);
            CacheForPoke.getInstance().setLocalP(player);



            player.setMap(new TmxMapLoader().load("D:\\Poke\\Prämap\\maps\\PRZCITY.TMX"));
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
    }

    @Override
    public void broadcast(Event e) {
        send(e);
    }
}
