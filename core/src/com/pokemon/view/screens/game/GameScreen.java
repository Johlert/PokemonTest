package com.pokemon.view.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
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
import com.pokemon.model.*;
import com.pokemon.model.Events.Listener;
import com.pokemon.model.Events.MapJoinEvent;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Networking.Server;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.AnimationSet;
import com.pokemon.view.utils.dialogue.DialogueBox;
import com.pokemon.view.utils.dialogue.OptionBox;
import lombok.Data;

public @Data class GameScreen implements Screen {
    //w: 297 h: 167
    public static final int V_WIDTH = 400;
    public static final int V_HEIGHT = 310;
    private final Pokemon pokemon;
    private Map map;
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
        this.map = new Map();
        this.map.setMap(map);
        this.map.setName("PRZCITY");

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

        playerLayer = map.getMap().getLayers().get("Entity Layer");
        textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));

        TextureMapObject tmo = new TextureMapObject(textureRegion);
        tmo.setX(0);
        tmo.setY(0);

        playerLayer.getObjects().add(tmo);

        if(!(CacheForPoke.getInstance().getPostOffice() instanceof Server)){
            player = new Player(this, map, tmo, 55, 9);
            player.setName("egal");
        }else{
            player = new Player(this, map, tmo, 54, 8);
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

        renderer = new OrthogonalTiledMapRendererWithSprites(map.getMap(), pokemon.getBatch());
    }

    public Map setMap(String mapName, Direction facing, int doorId) {
        map = new Map();
        map.setName(mapName);
        map.setMap(CacheForPoke.getInstance().getActiveWorld().getMaps().get(mapName).getMap());
        playerLayer = map.getMap().getLayers().get("Entity Layer");
        renderer = new OrthogonalTiledMapRendererWithSprites(map.getMap(), pokemon.getBatch());
        initControllers();
        
        for (MapObject object : map.getMap().getLayers().get("Objects").getObjects()) {
            if (object instanceof RectangleMapObject) {
                if (object.getName().equals("door_" + doorId)){

                    player.setMap(map);

                    Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                    player.getTmo().setX(rectangle.x);
                    player.getTmo().setY(rectangle.y);
                    player.setFacing(facing);
                    player.setState(Player.ACTOR_STATE.STANDING);
                }
            }
        }

        player.setFacing(facing);
        return map;
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
            if(value.equals(player)){
                playerLayer.getObjects().add(value.getTmo());
                value.getTmo().setTextureRegion(value.getSprite());
                value.update(delta);
            }
        }

        renderMap(delta);

        overlayStage.draw();
        overlayStage.act(delta);

        if (tempx !=  player.getX() || tempy !=  player.getY()) {
            tempx =  player.getX();
            tempy = player.getY();
            System.out.println("Player x: " + player.getX());
            System.out.println("Player y: " + player.getY());
        }
    }

    float tempx, tempy;

    public void renderMap(float delta) {
        Player player = CacheForPoke.getInstance().getLocalP();
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

    public Map getMap() {
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
            if(CacheForPoke.getInstance().getPostOffice() instanceof Server){
                ((Server) CacheForPoke.getInstance().getPostOffice()).broadcast(mapJoinEvent, mapJoinEvent.getName());
            }

            System.out.println("mapjoinevent found " + mapJoinEvent.getName() +" : " + mapJoinEvent.getPosition().getX() + " : " + mapJoinEvent.getPosition().getY());
            String color = "cyan";
            TextureAtlas atlas = pokemon.getAssetManager().get("atlas/player_sprites.atlas", TextureAtlas.class);
            TextureRegion textureRegion = new TextureRegion(atlas.findRegion(color + "_stand_south").getTexture(), Global.TILE_SIZE, (int) (1.5 * Global.TILE_SIZE));
            //maps/Prämap/maps/
            if(CacheForPoke.getInstance().getPlayers().containsKey(mapJoinEvent.getName())){
                Player player = CacheForPoke.getInstance().getPlayers().get(mapJoinEvent.getName());
                player.setMap(CacheForPoke.getInstance().getActiveWorld().getMaps().get(mapJoinEvent.getPosition().getMapPath()));
                player.getTmo().setX(mapJoinEvent.getPosition().getX() / Global.TILE_SIZE);
                player.getTmo().setY(mapJoinEvent.getPosition().getY() / Global.TILE_SIZE);
            }else{
                Player player = new Player(null, map, new TextureMapObject(textureRegion),mapJoinEvent.getPosition().getX() / Global.TILE_SIZE, mapJoinEvent.getPosition().getY()/ Global.TILE_SIZE);
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
}
