package com.pokemon.view.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.controller.PlayerController;
import com.pokemon.model.Player;
import com.pokemon.model.Settings;
import com.pokemon.view.Pokemon;

public class GameScreen implements Screen {
    public static final int V_WIDTH = 1200;
    public static final int V_HEIGHT = 928;
    private final Pokemon pokemon;
    private SpriteBatch batch;

    private PlayerController controller;

    private Player player;
    private final Texture texture;

    private Viewport gamePort;
    private OrthographicCamera gameCam;

    private TmxMapLoader mapLoader;
    private final TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private MapLayer playerLayer;
    private TextureRegion textureRegion;

    public GameScreen(Pokemon pokemon) {
        this.pokemon = pokemon;
        texture = new Texture("purple_stand_south.png");
        map = mapLoader.load("E:\\Poke\\Prämap\\maps\\PRZCITY.TMX");
    }

    @Override
    public void show() {
        batch = pokemon.getBatch();

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH, V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        mapLoader = new TmxMapLoader();
        renderer = new OrthogonalTiledMapRendererWithSprites(map, batch);

        playerLayer = map.getLayers().get("Entity Layer");
        textureRegion = new TextureRegion(texture, 16, 24);

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(0);
        tmo.setY(0);

        playerLayer.getObjects().add(tmo);
        player = new Player(map, tmo, 0, 0);
        controller = new PlayerController(player);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        gameCam.position.set(player.getX() + Settings.TILE_SIZE / 2f, player.getY() + Settings.TILE_SIZE / 2f, 0);
        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();
    }

    private void handleInput(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            gameCam.viewportHeight += V_HEIGHT * dt;
            gameCam.viewportWidth += V_WIDTH * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            gameCam.viewportHeight -= V_HEIGHT * dt;
            gameCam.viewportWidth -= V_WIDTH * dt;
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
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

    }
}