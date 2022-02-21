package com.pokemon.model.Items;

import java.io.Serializable;

public abstract class ItemStack implements Serializable {

    private int amount;
    private Item item;

    public ItemStack(int amount, Item item) {
        this.amount = amount;
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
