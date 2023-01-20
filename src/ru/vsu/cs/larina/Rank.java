package ru.vsu.cs.larina;

public enum Rank {
    SIX(6, "6"),
    SEVEN(7,"7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    JACK(11, "J"),
    QUEEN(12, "Q"),
    KING(13, "K"),
    ACE(14, "A");

    private final int value;
    private final String uiString;

    Rank (int value, String uiString){
        this.value = value;
        this.uiString = uiString;
    }

    public int getValue(){
        return value;
    }

    public String getUiString(){
        return uiString;
    }
}
