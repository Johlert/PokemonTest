package com.pokemon.view.utils.dialogue;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;

public class OptionBox extends Table {
    private int selectedRow, selectedColumn;
    private final Label[][] options;
    private final Image[][] arrows;
    private final Table uiContainer;

    public OptionBox(Skin skin, int rows, int columns) {
        super(skin);
        setBackground("optionsBox");
        options = new Label[rows][columns];
        arrows = new Image[rows][columns];

        uiContainer = new Table();
        add(uiContainer).pad(5f);
    }

    public void addOption(String option) {
        Label optionLabel = new Label(option, getSkin());
        Image arrow = new Image(getSkin(), "greyArrow");
        arrow.setScaling(Scaling.none);
        arrow.setVisible(false);

        fillOption(optionLabel, arrow);
        changeArrowVis();
    }

    private boolean fillOption(Label label, Image arrow) {
        for (int row = 0; row < options.length; row++) {
            for (int col = 0; col < options[row].length; col++) {
                if (options[row][col] == null) {
                    options[row][col] = label;
                    arrows[row][col] = arrow;

                    if (col == options[row].length - 1) {
                        uiContainer.add(arrow).expand().align(Align.left).space(5f);
                        uiContainer.add(label).expand().align(Align.left).space(5f).row();
                    } else {
                        uiContainer.add(arrow).expand().align(Align.left).space(5f);
                        uiContainer.add(label).expand().align(Align.left).space(5f);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public void moveUp() {
        selectedRow = Math.max(0, selectedRow - 1);
        changeArrowVis();
    }

    public void moveDown() {
        selectedRow = Math.min(options.length - 1, selectedRow + 1);
        changeArrowVis();
    }

    public void moveRight() {
        selectedColumn = Math.min(options[selectedRow].length - 1, selectedColumn + 1);
        changeArrowVis();
    }

    public void moveLeft() {
        selectedColumn = Math.max(0, selectedColumn - 1);
        changeArrowVis();
    }

    private void changeArrowVis() {
        for (int row = 0; row < options.length; row++) {
            for (int col = 0; col < options[row].length; col++) {
                if (arrows[row][col] != null) {
                    arrows[row][col].setVisible(row == selectedRow && col == selectedColumn);
                }
            }
        }
    }
}
