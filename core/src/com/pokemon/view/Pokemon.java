package com.pokemon.view;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.pokemon.view.screens.menu.MainMenuScreen;

public class Pokemon extends Game {
    private SpriteBatch batch;
    private AssetManager assetManager;
    private Skin skin;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.load("atlas/player_sprites.atlas", TextureAtlas.class);
        assetManager.load("atlas/ui.atlas", TextureAtlas.class);
        assetManager.load("fonts/small_letters_font.fnt", BitmapFont.class);
        assetManager.load("atlas/battle.atlas", TextureAtlas.class);
        assetManager.finishLoading();

        initSkin();
        setScreen(new MainMenuScreen(this));
        //setScreen(new MainMenuScreen(this));
    }

    private void initSkin() {
        skin = new Skin();
        TextureAtlas uiAtlas = assetManager.get("atlas/ui.atlas");

        NinePatch dialogueBox = new NinePatch(uiAtlas.findRegion("dialogueBox"), 7, 7, 6, 6);
        skin.add("dialogueBox", dialogueBox);
        NinePatch optionsBox = new NinePatch(uiAtlas.findRegion("optionsBox"), 12, 12, 5, 5);
        skin.add("optionsBox", optionsBox);
        skin.add("greyArrow", uiAtlas.findRegion("greyArrow"), TextureRegion.class);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/pkmnrsi.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.color = new Color(248f / 255f, 248f / 255f, 248f / 255f, 1f);
        parameter.shadowColor = new Color(96f / 255f, 96f / 255f, 96f / 255f, 1f);
        parameter.shadowOffsetX = 1;
        parameter.shadowOffsetY = 1;
        parameter.characters = "!  \"  #  $  %  &  '  (  )  *  +  ,  -  .  /  0  1  2  3  4  5  6  7  8  9  :  ;  <  =  >  ?  @  A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z  [  \\  ]  ^  _  `  a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  {  |  }  ~  \u2190  \u2191  \u2192  \u2193  \u2640  \u2642";

        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();
        font.getData().setLineHeight(16f);
        skin.add("font", font);

        BitmapFont smallFont = assetManager.get("fonts/small_letters_font.fnt", BitmapFont.class);
        skin.add("small_letters_font", smallFont);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = skin.getFont("font");
        skin.add("default", labelStyle);

        Label.LabelStyle labelStyleSmall = new Label.LabelStyle();
        labelStyleSmall.font = skin.getFont("small_letters_font");
        skin.add("smallLabel", labelStyleSmall);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public Skin getSkin() {
        return skin;
    }
}
