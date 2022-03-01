package com.pokemon.model.Pokemon;

public class Duel {

    private Trainer trainer1;
    private Ability trainer1Move;
    private boolean hasP1Moved = false;



    private Trainer trainer2;
    private Ability trainer2Move;
    private boolean hasP2Moved = false;

    public void setTrainer1Move(Ability trainer1Move) {
        this.trainer1Move = trainer1Move;
        hasP1Moved = true;
        resolveRound();
    }

    public void setTrainer2Move(Ability trainer2Move) {
        this.trainer2Move = trainer2Move;
        hasP2Moved = true;
        resolveRound();
    }

    private void resolveRound(){
        if(hasP1Moved && hasP2Moved){

            //todo fighting


        }
    }
}
