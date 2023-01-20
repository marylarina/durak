package ru.vsu.cs.larina;

public enum Suit {

    DIAMONDS("♦"),
    HEARTS("♥"),
    SPADES("♠"),
    CLUBS("♣");


    private final String uiString;

    Suit (String uiString){
        this.uiString = uiString;
    }

    public String getUiString(){
        return uiString;
    }
}
