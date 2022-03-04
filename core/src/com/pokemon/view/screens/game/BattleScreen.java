package com.pokemon.view.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pokemon.controller.DialogueController;
import com.pokemon.controller.OptionBoxController;
import com.pokemon.model.CacheForPoke;
import com.pokemon.model.Player;
import com.pokemon.view.Pokemon;
import com.pokemon.view.utils.dialogue.DialogueBox;
import com.pokemon.view.utils.dialogue.OptionBox;
import javafx.scene.control.ChoiceBox;

public class BattleScreen implements Screen {
    private Pokemon pokemon;

    private final float overlayScale = 3f;

    private Batch batch;
    private Viewport viewport;

    private Stage stage;
    private Table table;

    private TextureRegion background;
    public BattleScreen(Pokemon pokemon) {
        this.pokemon = pokemon;
        batch = pokemon.getBatch();

        TextureAtlas atlas = pokemon.getAssetManager().get("atlas/battle.atlas", TextureAtlas.class);
        background = atlas.findRegion("grass");


        stage = new Stage(new StretchViewport(960, 670));
        stage.getViewport().update((int) (Gdx.graphics.getWidth() / overlayScale), (int) (Gdx.graphics.getHeight() / overlayScale), true);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.setDebug(true);
        stage.setDebugAll(true);

        DialogueBox dialogueBox = new DialogueBox(pokemon.getSkin());
        dialogueBox.write("lfgdmhöldfghml");
        table.add(dialogueBox).expand().align(Align.bottom).fill(1, 0.3f);


        Table fightBox = new Table();
        fightBox.setDebug(true);
        fightBox.setFillParent(true);
        stage.addActor(fightBox);

        OptionBox op = new OptionBox(pokemon.getSkin(), 2, 2);
        op.addOption("Fight");
        op.addOption("Bag");
        op.addOption("Pokemon");
        op.addOption("Run");
        fightBox.add(op).expand().align(Align.bottomRight).fill(0.4f,0.3f);

        DialogueController dialogueController = new DialogueController(dialogueBox, CacheForPoke.getInstance().getLocalP());
        OptionBoxController optionBoxController = new OptionBoxController(op, CacheForPoke.getInstance().getLocalP());
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(dialogueController);
        inputMultiplexer.addProcessor(optionBoxController);
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void show() {
        viewport = new ScreenViewport();
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        batch.begin();
        batch.draw(background, 0, Gdx.graphics.getHeight() * 0.3f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() * 0.7f);
        batch.end();

        stage.draw();
        stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {
        batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        viewport.update(width, height);

        table.setBounds(0, 0, width, height * 0.3f);
        stage.getViewport().update((int) (width / overlayScale), (int) (height / overlayScale), true);
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
