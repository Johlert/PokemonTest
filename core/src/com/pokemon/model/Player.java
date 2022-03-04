package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.pokemon.controller.UserSettings;
import com.pokemon.model.Events.*;
import com.pokemon.model.Pokemon.Pokemon;
import com.pokemon.model.Pokemon.Trainer;
import com.pokemon.view.screens.game.GameScreen;
import com.pokemon.view.utils.AnimationSet;
import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;

public @Data
class Player implements Serializable, Trainer, Listener {
    private float ANIM_DUR = 0.5f;
    private UserSettings userSettings = new UserSettings();
    private Map map;
    private TextureMapObject tmo;
    private String name;
    private Inventory inventory;
    private Pokemon[] pokemon;
    private Pokemon activePokemon;
    private Position position;
    private int money;
    private LinkedList<Flag> flags;
    private int speedInTilesPerSec = 2;
    private int srcX, srcY, destX, destY;
    private float curAnimDur;
    private ACTOR_STATE state;
    private float curWalkDur;
    private boolean moveRequestThisFrame;
    private boolean inDialogue = false;
    public boolean temp;

    private Direction facing;
    private GameScreen gameScreen;
    private AnimationSet animationSet;

    public Player(GameScreen screen, Map map, TextureMapObject tmo, int x, int y) {
        this.gameScreen = screen;
        this.map = map;
        //this.map.setMap(map);
        this.tmo = tmo;
        tmo.setX(x * Global.TILE_SIZE);
        tmo.setY(y * Global.TILE_SIZE);
        state = ACTOR_STATE.STANDING;
        facing = Direction.DOWN;
    }

    public float getX() {
        return tmo.getX();
    }

    public float getY() {
        return tmo.getY();
    }

    public boolean move(Direction dir) {
        int x = (int) tmo.getX();
        int y = (int) tmo.getY();

        if (state == ACTOR_STATE.WALKING) {
            if (facing == dir) {
                moveRequestThisFrame = true;
            }
            return false;
        }

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getMap().getLayers().get("Collision Layer");

        temp = layer.getCell((x + dir.getDx()) / Global.TILE_SIZE, (y + dir.getDy()) / Global.TILE_SIZE) == null;

        if (CacheForPoke.getInstance().getLocalP().equals(this)){
            for (MapObject object : map.getMap().getLayers().get("Objects").getObjects()) {
                if (object instanceof RectangleMapObject) {
                    Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                    if (rectangle.contains(x + dir.getDx(), y + dir.getDy())) {
                        if (object.getName().equals("dialogue")) {
                            gameScreen.initiateDialogue((String) object.getProperties().get("dialogue"));
                        } else {
                            String mapName = (String) object.getProperties().get("map");
                            String facing = (String) object.getProperties().get("facing");
                            int doorId;
                            if (object.getProperties().get("doorid") == null) {
                                doorId = 0;
                            } else {
                                doorId = (int) object.getProperties().get("doorid");
                            }

                            Direction direction = null;

                            switch (facing) {
                                case "north":
                                    direction = Direction.UP;
                                    break;
                                case "south":
                                    direction = Direction.DOWN;
                                    break;
                                case "east":
                                    direction = Direction.RIGHT;
                                    break;
                                case "west":
                                    direction = Direction.LEFT;
                                    break;
                            }

                            if (CacheForPoke.getInstance().getLocalP().equals(this)) {
                                map = gameScreen.setMap(mapName, direction, doorId);
                            }
                        }
                    }
                }
            }
        }

        if (temp){
            initializeMove(x, y, dir);
            tmo.setX(x + dir.getDx());
            tmo.setY(y + dir.getDy());
        }
        return temp;
    }

    private void initializeMove(int x1, int y1, Direction dir) {
        if(CacheForPoke.getInstance().getLocalP().equals(this)){
            MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP().getName(), dir, new Position((int) tmo.getX(), (int) tmo.getY(), map.getName()));
            CacheForPoke.getInstance().getPostOffice().broadcast(mv);
            EventQueue.getINSTANCE().addEvent(mv);
        }
        this.facing = dir;
        srcX = x1;
        srcY = y1;
        destX = x1 + dir.getDx();
        destY = y1 + dir.getDy();
        curAnimDur = 0;
        state = ACTOR_STATE.WALKING;
    }

    private void finishMove() {
        state = ACTOR_STATE.STANDING;
        tmo.setX(destX);
        tmo.setY(destY);
    }

    public void update(float delta) {
        if (state == ACTOR_STATE.WALKING) {
            curAnimDur += delta;
            curWalkDur += delta;
            tmo.setX(Interpolation.linear.apply(srcX, destX, curAnimDur / ANIM_DUR));
            tmo.setY(Interpolation.linear.apply(srcY, destY, curAnimDur / ANIM_DUR));
            if (curAnimDur > ANIM_DUR) {
                float leftOverTime = curAnimDur - ANIM_DUR;
                finishMove();
                if (moveRequestThisFrame) {
                    if (move(facing)) {
                        curAnimDur += leftOverTime;
                        tmo.setX(Interpolation.linear.apply(srcX, destX, curAnimDur / ANIM_DUR));
                        tmo.setY(Interpolation.linear.apply(srcY, destY, curAnimDur / ANIM_DUR));
                    }
                } else {
                    curWalkDur = 0f;
                }
            }
        }
        moveRequestThisFrame = false;
    }

    @Override
    public void onMove(MoveEvent moveEvent) {

        System.out.println("moveEvent name" + moveEvent.getName());
        if (moveEvent.getName().equals(CacheForPoke.getInstance().getLocalP().getName())) {
            move(moveEvent.getDirection());
        } else {
            System.out.println(moveEvent.getName() + ":" + CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).getX() + ":" +CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).getY());
            System.out.println(moveEvent.getDirection());
            CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).setState(ACTOR_STATE.STANDING);
            CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).getTmo().setX(moveEvent.getPos().getX());
            CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).getTmo().setY(moveEvent.getPos().getY());
            CacheForPoke.getInstance().getPlayers().get(moveEvent.getName()).move(moveEvent.getDirection());

        }
    }

    @Override
    public void onPlayerFacing(FacingEvent facingEvent) {
        if(facingEvent.getName().equals(CacheForPoke.getInstance().getLocalP().getName())){
            facing = facingEvent.getDirection();
        }else {
            CacheForPoke.getInstance().getPlayers().get(facingEvent.getName()).setFacing(facingEvent.getDirection());
        }
    }

    @Override
    public void onMapJoin(MapJoinEvent mapJoinEvent) {

    }

    public TextureRegion getSprite() {
        if (state == ACTOR_STATE.WALKING) {
            return animationSet.getWalking(facing).getKeyFrame(curWalkDur);
        } else if (state == ACTOR_STATE.STANDING) {
            return animationSet.getStanding(facing);
        }
        return animationSet.getStanding(Direction.DOWN);
    }

    public enum ACTOR_STATE {
        WALKING, STANDING
    }
}
