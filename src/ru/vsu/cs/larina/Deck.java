package ru.vsu.cs.larina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

     protected final List<Card> cards = new ArrayList<>();

     public Deck(){
          init();
     }

     private void init(){
          for (Suit suit: Suit.values()){
               for(Rank rank: Rank.values()){
                    cards.add(new Card(rank,suit));
               }
          }
          Collections.shuffle(cards);
     }

     public Card getNextCard(){
          if(cards.isEmpty()){
               return null;
          }
          return cards.remove(0);
     }

     public Suit trumpCard(){
          if(cards.isEmpty()){
               return null;
          }
          return cards.remove(0).getSuit();
     }

}
