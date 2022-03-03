package com.pokemon.view.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.controller.DialogueController;
import com.pokemon.controller.OptionBoxController;
import com.pokemon.controller.PlayerController;
import com.pokemon.controller.UtilController;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Events.Listener;
import com.pokemon.model.Events.MapJoinEvent;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Global;
import com.pokemon.model.Networking.Server;
import com.pokemon.model.Player;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.AnimationSet;
import com.pokemon.view.utils.dialogue.DialogueBox;
import com.pokemon.view.utils.dialogue.OptionBox;
import lombok.Data;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public @Data class GameScreen implements Screen {
    //w: 297 h: 167
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 310;
    private final Pokemon pokemon;
    private final TiledMap map;
    private final float overlayScale = 3f;
    private PlayerController playerController;
    private DialogueController dialogueController;
    private OptionBoxController optionBoxController;
    private UtilController utilController;
    private Player player;
    private Viewport gamePort;
    private OrthographicCamera gameCam;
    private OrthogonalTiledMapRenderer renderer;
    private Stage overlayStage;
    private Table overlayTable;
    private DialogueBox dialogueBox;
    private OptionBox optionBox;

    private MapLayer playerLayer;
    private TextureRegion textureRegion;

    public GameScreen(Pokemon pokemon, TiledMap map) {
        this.pokemon = pokemon;
        this.map = map;
        CacheForPoke.getInstance().getHandler().addListener(new ScreenListener());
    }

    @Override
    public void show() {
        initCamAndRenderer();
        initPlayer();
        initUIComponents();
        initControllers();
    }

    private void initPlayer() {
        TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
        String color = "brown";

        playerLayer = map.getLayers().get("Entity Layer");
        textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(0);
        tmo.setY(0);

        playerLayer.getObjects().add(tmo);
        player = new Player(this, map, tmo, 54, 8);
        if(!(CacheForPoke.getInstance().getPostOffice() instanceof Server)){
            player.setName("egal");
        }

        player.setAnimationSet(new AnimationSet(
                new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_north"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_south"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_east"), Animation.PlayMode.LOOP_PINGPONG),
                new Animation<>(player.getANIM_DUR() / 2f, atlas.findRegions(color + "_walk_west"), Animation.PlayMode.LOOP_PINGPONG),
                atlas.findRegion(color + "_stand_north"),
                atlas.findRegion(color + "_stand_south"),
                atlas.findRegion(color + "_stand_east"),
                atlas.findRegion(color + "_stand_west")));

        CacheForPoke.getInstance().setLocalP(player);
        CacheForPoke.getInstance().getHandler().addListener(player);
    }

    private void initCamAndRenderer() {
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH, V_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        renderer = new OrthogonalTiledMapRendererWithSprites(map, pokemon.getBatch());
    }

    private void initControllers() {
        playerController = new PlayerController(this, player);
        dialogueController = new DialogueController(this, dialogueBox, player);
        optionBoxController = new OptionBoxController(this, optionBox, player);
        utilController = new UtilController(this);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(playerController);
        inputMultiplexer.addProcessor(dialogueController);
        inputMultiplexer.addProcessor(optionBoxController);
        inputMultiplexer.addProcessor(utilController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void initUIComponents() {
        overlayStage = new Stage(new ScreenViewport());
        overlayStage.getViewport().update((int) (Gdx.graphics.getWidth() / overlayScale), (int) (Gdx.graphics.getHeight() / overlayScale), true);

        overlayTable = new Table();
        overlayTable.setFillParent(true);
        overlayStage.addActor(overlayTable);

        dialogueBox = new DialogueBox(pokemon.getSkin());
        dialogueBox.setVisible(false);

        overlayTable.add(dialogueBox).expand().align(Align.bottom).pad(8f);

        Table dialogTable = new Table();

        optionBox = new OptionBox(pokemon.getSkin(), 3, 1);
        optionBox.addOption("Thunder Strike");
        optionBox.addOption("Lightning Bolt");
        optionBox.addOption("Dragon Claw");

        dialogTable.add(optionBox);

        //overlayTable.add(dialogTable).expand().align(Align.bottom);
        //overlayStage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        playerController.update(delta);
        player.getTmo().setTextureRegion(player.getSprite());

        for(Player value : CacheForPoke.getInstance().getPlayers().values()){
            if(!value.equals(player)){
                playerLayer.getObjects().add(value.getTmo());
                value.getTmo().setTextureRegion(player.getSprite());

            }

        }

        renderMap(delta);

        overlayStage.draw();
        overlayStage.act(delta);
    }

    public void renderMap(float delta) {
        gamePort.apply();
        player.update(delta);
        gameCam.position.set(player.getX() + Global.TILE_SIZE / 2f, player.getY() + Global.TILE_SIZE / 2f, 0);
        gameCam.update();
        renderer.setView(gameCam);
        renderer.render();
    }

    public Player getPlayer() {
        return player;
    }

    public TiledMap getMap() {
        return map;
    }

    public void initiateDialogue(String s) {
        player.setInDialogue(true);
        System.out.println(player.getState());
        dialogueBox.setVisible(true);
        dialogueBox.write(s);
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
        overlayStage.getViewport().update((int) (width / overlayScale), (int) (height / overlayScale), true);
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

    public @Data class ScreenListener implements Listener {
        @Override
        public void onMove(MoveEvent moveEvent) {
        }

        @Override
        public void onMapJoin(MapJoinEvent mapJoinEvent) {
            String color = "cyan";
            TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
            TextureRegion textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));
            Player player = new Player(null, map, new TextureMapObject(textureRegion),mapJoinEvent.getPosition().getX(), mapJoinEvent.getPosition().getY());
            CacheForPoke.getInstance().getPlayers().put(mapJoinEvent.getName(), player);
            player.setName(mapJoinEvent.getName());
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
    }
}
