package com.pokemon.view.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Networking.Client;
import com.pokemon.model.Networking.Server;
import com.pokemon.view.Pokemon;
import com.pokemon.view.screens.game.GameScreen;

public class MainMenuScreen implements Screen {
    private final Pokemon pokemon;
    private final Stage stage;
    private final TextureAtlas atlas;
    private final TextButton.TextButtonStyle buttonStyle;
    private final Label.LabelStyle labelStyle;
    private final Skin skin;
    private final Table table;
    private final TextButton hostButton;
    private final TextButton joinButton;
    private final TextButton settingsButton;
    private final TextButton quitButton;
    private final BitmapFont white;
    private final BitmapFont black;
    private final Label heading;

    public MainMenuScreen(final Pokemon pokemon) {
        this.pokemon = pokemon;
        stage = new Stage();
        atlas = new TextureAtlas("atlas/atlas.pack");
        buttonStyle = new TextButton.TextButtonStyle();
        labelStyle = new Label.LabelStyle();
        skin = new Skin(atlas);
        table = new Table();
        white = new BitmapFont(Gdx.files.internal("fonts/white.fnt"), false);
        black = new BitmapFont(Gdx.files.internal("fonts/black.fnt"), false);

        buttonStyle.up = skin.getDrawable("button.up");
        buttonStyle.down = skin.getDrawable("button.down");
        buttonStyle.pressedOffsetX = 1;
        buttonStyle.pressedOffsetY = -1;
        buttonStyle.font = black;

        labelStyle.font = white;
        labelStyle.fontColor = Color.WHITE;

        heading = new Label("POKEMON", labelStyle);

        hostButton = new TextButton("HOST", buttonStyle);
        joinButton = new TextButton("JOIN", buttonStyle);
        settingsButton = new TextButton("SETTINGS", buttonStyle);
        quitButton = new TextButton("QUIT", buttonStyle);

        hostButton.pad(8);
        joinButton.pad(8);
        settingsButton.pad(8);
        quitButton.pad(8);

        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("hosting");
                CacheForPoke.getInstance().setPostOffice(new Server());
                pokemon.setScreen(new GameScreen(pokemon, new TmxMapLoader().load("C:\\Users\\micro\\Desktop\\Prämap\\maps\\PRZCITY.TMX")));
            }
        });
        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("joining");
                Client client = new Client();
                CacheForPoke.getInstance().setPostOffice(client);
                System.out.println("connect: " + client.connect("localhost", "egal"));
                pokemon.setScreen(new GameScreen(pokemon, CacheForPoke.getInstance().getLocalP().getMap()));
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.add(heading);
        table.getCell(heading).spaceBottom(90);
        table.row();
        table.add(hostButton);
        table.getCell(hostButton).spaceBottom(5);
        table.row();
        table.add(joinButton);
        table.getCell(joinButton).spaceBottom(5);
        table.row();
        table.add(settingsButton);
        table.getCell(settingsButton).spaceBottom(5);
        table.row();
        table.add(quitButton);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        atlas.dispose();
        skin.dispose();
        white.dispose();
        black.dispose();
    }
}
