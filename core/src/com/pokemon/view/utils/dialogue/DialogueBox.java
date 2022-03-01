package com.pokemon.view.utils.dialogue;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.pokemon.view.Pokemon;

public class DialogueBox extends Table {
    private String text = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private final float WRITING_SPEED = 0.05f;
    private STATE state = STATE.IDLE;

    private Label textLabel;

    private enum STATE {
        ANIMATING, IDLE;
    }

    public DialogueBox(Skin skin) {
        super(skin);

        setBackground("dialogueBox");
        textLabel = new Label("\n", skin);
        add(textLabel).expand().align(Align.left).pad(5f);
    }

    public void write(String s) {
        text = s;
        animationTotalTime = text.length() * WRITING_SPEED;
        state = STATE.ANIMATING;
        animTimer = 0f;
    }

    public boolean isFinished(){
        return state == STATE.IDLE;
    }

    private void setText(String s){
        if (!s.contains("\n")){
            s += "\n";
        }
        textLabel.setText(s);
    }

    public void forceFinish(){
        setText(text);
        state = STATE.IDLE;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer > animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
            }
            String actuallyDisplayedText = "";
            int charactersToDisplay = (int) ((animTimer/animationTotalTime)*text.length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText += text.charAt(i);
            }
            if (!actuallyDisplayedText.equals(textLabel.getText().toString())){
                setText(actuallyDisplayedText);
            }
        }
    }

    @Override
    public float getPrefWidth() {
        return 200f;
    }
}
