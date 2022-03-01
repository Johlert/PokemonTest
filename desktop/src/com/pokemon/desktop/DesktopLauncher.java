package com.pokemon.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pokemon.view.Pokemon;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Pokemon";
        config.height = 670;
        config.width = 960;
        config.forceExit = true;
        config.vSyncEnabled = false;
        config.foregroundFPS = 200;
        new LwjglApplication(new Pokemon(), config);
    }
}
