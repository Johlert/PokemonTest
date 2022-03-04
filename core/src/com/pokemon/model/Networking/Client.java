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
import com.pokemon.model.Events.SplitterEvent;
import com.pokemon.model.Player;
import com.pokemon.view.screens.menu.MainMenuScreen;
import lombok.SneakyThrows;

import java.io.*;

public class Client implements PostOffice {



    public Client(MainMenuScreen mms){
        this.mms = mms;
    }

    ObjectOutputStream objectOutputStream;
    MainMenuScreen mms;
    Socket socket;
    Net net = new LwjglNet(new LwjglApplicationConfiguration());

    public boolean connect(String ip, String playername) {
        socket = net.newClientSocket(Net.Protocol.TCP, ip, 5000, new SocketHints());
        if(socket.isConnected()){
            try {
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            //Player player = (Player) objectInputStream.readObject();
            System.out.println(2);
            //CacheForPoke.getInstance().setLocalP(player);
            Object f;
            File file = null;
            while (!((f = objectInputStream.readObject()) instanceof Event)){
                if(f instanceof File){
                    file = (File) f;
                    if(file.isDirectory()){
                        System.out.println(((File) f).getPath());
                        File testTemp = new File("serverMap/" + file.getPath());
                        System.out.println(testTemp.mkdirs());

                    }
                }else if(f instanceof FileTransferWrapper){
                    FileTransferWrapper fileTransferWrapper = (FileTransferWrapper) f;
                    File testTemp = new File("serverMap/" + fileTransferWrapper.getFile().getPath());
                    testTemp.mkdirs();
                    testTemp.delete();
                    System.out.println(fileTransferWrapper.getContent().length);
                    testTemp.createNewFile();
                    FileOutputStream fos = new FileOutputStream(testTemp);
                    fos.write(fileTransferWrapper.getContent());
                    fos.flush();
                }


            }
            System.out.println("loading maps");
            File ff = new File("serverMap/core/assets/maps/Prämap/maps");
            CacheForPoke.getInstance().loadMaps(ff);
            System.out.println("switching to starting map");
            mms.switchToGameScreen(CacheForPoke.getInstance().getActiveWorld().getMaps().get("PRZCITY").getMap());


            //player.setMap(new TmxMapLoader().load("D:\\Poke\\Praemap\\maps\\PRZCITY.TMX"));
            if(!(f instanceof SplitterEvent)){
                EventQueue.getINSTANCE().addEvent((Event) f);
            }

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
