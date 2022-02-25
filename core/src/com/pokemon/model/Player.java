package com.pokemon.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Interpolation;
import com.pokemon.controller.UserSettings;
import com.pokemon.model.Events.EventQueue;
import com.pokemon.model.Events.Listener;
import com.pokemon.model.Events.MapJoinEvent;
import com.pokemon.model.Events.MoveEvent;
import com.pokemon.model.Pokemon.Pokemon;
import com.pokemon.model.Pokemon.Trainer;

import java.io.Serializable;
import java.util.LinkedList;

import com.pokemon.view.utils.AnimationSet;
import lombok.*;

public @Data class Player implements Serializable, Trainer, Listener {
    private UserSettings userSettings = new UserSettings();
    private TiledMap map;
    private TextureMapObject tmo;
    private String name;
    private Inventory inventory;
    private Pokemon[] pokemon;
    private Pokemon activePokemon;
    private Location location;
    private int money;
    private LinkedList<Flag> flags;
    private int speedInTilesPerSec = 2;

    private int srcX, srcY, destX, destY;
    private float curAnimDur;
    private final float ANIM_DUR = 0.5f;
    private ACTOR_STATE state;

    private float curWalkDur;
    private boolean moveRequestThisFrame;

    private Direction facing;

    private AnimationSet animationSet;

    public enum ACTOR_STATE {
        WALKING,
        STANDING;
    }

    public int getSpeedInTilesPerSec() {
        return speedInTilesPerSec;
    }

    @Override
    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public void setAnimationSet(AnimationSet animationSet) {
        this.animationSet = animationSet;
    }

    public Player(TiledMap map, TextureMapObject tmo, int x, int y) {
        this.map = map;
        this.tmo = tmo;
        tmo.setX(x* Global.TILE_SIZE);
        tmo.setY(y* Global.TILE_SIZE);
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

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("Collision Layer");

        if (layer.getCell((x + dir.getDx()) / Global.TILE_SIZE, (y + dir.getDy()) / Global.TILE_SIZE) == null) {
            initializeMove(x, y, dir);
            tmo.setX(x+dir.getDx());
            tmo.setY(y+dir.getDy());
        } else {
            System.out.println(layer.getCell((x + dir.getDx()) / Global.TILE_SIZE, (y + dir.getDy()) / Global.TILE_SIZE).getTile().getProperties());
            return false;
        }
        return true;
    }

    private void initializeMove(int x1, int y1, Direction dir){
        this.facing = dir;
        srcX = x1;
        srcY = y1;
        destX = x1 + dir.getDx();
        destY = y1 + dir.getDy();
        curAnimDur = 0;
        state = ACTOR_STATE.WALKING;
    }

    private void finishMove(){
        state = ACTOR_STATE.STANDING;
        tmo.setX(destX);
        tmo.setY(destY);
    }

    public void update(float delta) {
        if (state == ACTOR_STATE.WALKING){
            curAnimDur += delta;
            curWalkDur += delta;
            tmo.setX(Interpolation.linear.apply(srcX, destX, curAnimDur/ANIM_DUR));
            tmo.setY(Interpolation.linear.apply(srcY, destY, curAnimDur/ANIM_DUR));
            if (curAnimDur>ANIM_DUR) {
                float leftOverTime = curAnimDur-ANIM_DUR;
                curWalkDur -= leftOverTime;
                finishMove();
                if (moveRequestThisFrame) {
                    //MoveEvent mv = new MoveEvent(CacheForPoke.getInstance().getLocalP(), facing);
                    //CacheForPoke.getInstance().getPostOffice().broadcast(mv);
                    //EventQueue.getINSTANCE().addEvent(mv);
                    move(facing);
                } else {
                    curWalkDur = 0f;
                }
            }
        }
        moveRequestThisFrame = false;
    }

    @Override
    public void onMove(MoveEvent moveEvent) {
        if(moveEvent.getP().equals(this)){
            move(moveEvent.getDirection());
        } else {
            moveEvent.getP().move(moveEvent.getDirection());
        }
    }

    @Override
    public void onMapJoin(MapJoinEvent mapJoinEvent) {
    }

    String cur = "";
    public TextureRegion getSprite(){
        if (state == ACTOR_STATE.WALKING){
            String temp = animationSet.getWalking(facing).getKeyFrame(curWalkDur).toString();
            if (!temp.equals(cur)){
                System.out.println(temp);
                cur = temp;
            }

            return (TextureRegion) animationSet.getWalking(facing).getKeyFrame(curWalkDur);
        } else if (state == ACTOR_STATE.STANDING) {
            return animationSet.getStanding(facing);
        }
        return animationSet.getStanding(Direction.DOWN);
    }
}
