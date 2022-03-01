package com.pokemon.model.Networking;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglNet;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Events.Event;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Map;
import com.pokemon.model.Player;
import lombok.Data;
import lombok.SneakyThrows;

import java.io.*;
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
            }
            System.out.println(2);
            //todo load player from save if required else send new Player
<<<<<<< Updated upstream
            player = new Player( null, CacheForPoke.getInstance().getLocalP().getMap(), CacheForPoke.getInstance().getLocalP().getTmo(), 3 , 3);

=======
            //player = new Player(null, null, 3 , 3);
            //send(player);
            System.out.println(4);
            File f = new File("core/assets/maps/Praemap");
            loadMapFiles(f, objectOutputStream);
>>>>>>> Stashed changes

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
