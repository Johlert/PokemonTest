package com.pokemon.view.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.controller.PlayerController;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Global;
import com.pokemon.model.Player;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.AnimationSet;

public class GameScreen implements Screen {
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 310;
    private final Pokemon pokemon;
    private SpriteBatch batch;

    private PlayerController controller;

    private Player player;

    private Viewport gamePort;
    private OrthographicCamera gameCam;

    private final TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private MapLayer playerLayer;
    private TextureRegion textureRegion;

    public GameScreen(Pokemon pokemon, TiledMap map) {
        this.pokemon = pokemon;
        this.map = map;
    }

    @Override
    public void show() {
        batch = pokemon.getBatch();

        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH, V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        renderer = new OrthogonalTiledMapRendererWithSprites(map, batch);

        TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
        String color = "brown";

        playerLayer = map.getLayers().get("Entity Layer");
        textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(0);
        tmo.setY(0);

        playerLayer.getObjects().add(tmo);
        player = new Player(map, tmo, 54, 8);

        player.setAnimationSet(new AnimationSet(
                new Animation(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion(color + "_stand_north"),
                atlas.findRegion(color + "_stand_south"),
                atlas.findRegion(color + "_stand_east"),
                atlas.findRegion(color + "_stand_west")));

        CacheForPoke.getInstance().setLocalP(player);
        CacheForPoke.getInstance().getHandler().addListener(player);

        controller = new PlayerController(this, player);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        controller.update(delta);
        player.getTmo().setTextureRegion(player.getSprite());
        renderMap(delta);
    }

    public void renderMap(float delta) {
        player.update(delta);
        gameCam.position.set(player.getX() + Global.TILE_SIZE / 2f, player.getY() + Global.TILE_SIZE / 2f, 0);
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
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){
            player.setANIM_DUR(0.1f);
        } else {
            player.setANIM_DUR(0.5f);
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
