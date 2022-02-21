package com.pokemon.model;

import com.pokemon.model.Items.ItemStack;

import java.io.Serializable;
import java.util.LinkedList;

public class Inventory implements Serializable {

    private int money;

    private LinkedList<ItemStack> inventory = new LinkedList<>();

    public Inventory(){
        
    }
    //todo
}
