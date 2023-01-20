package ru.vsu.cs.larina;

import java.util.*;

public class Game extends Deck{

    private Queue<Player> players;
    private Map<Player, List<Card>> personCards = new HashMap<>();

    protected final Suit trumpCard = trumpCard();

    public int numberOfPlayers(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public void init(int n){
        if (n>6){
            System.out.println("Игроков не может быть больше 6!");
        }else {
            players = new LinkedList<>();
            for (int i = 1; i <= n; i++) {
                players.add(new Player("player" + i));
            }
            for (Player player : players) {
                List<Card> playerCards = new ArrayList<>();
                personCards.put(player, getFirstSixCards(playerCards));
            }
        }
    }

    public void gameProcess(){
        System.out.print("Введите кол-во игроков:");
        init(numberOfPlayers());
        if(personCards.isEmpty()){
            System.out.println("Error");
        }else {

            List<Card> activeCards;
            //List<Card> defendingCards;
            List<Card> finalDefendingCards = new ArrayList<>();
            //int count = 0;
            int k=0;
            int flag = 0; //определяет порядок добавления в очередь, если игрок не может отбиться
            int order = 0; //определяет порядок добавления в очередь при переводе карт
            System.out.print("Козырь: ");
            System.out.println(trumpCard.getUiString());

            while (players.size() > 1) {
                //вывод карт каждого игрока в начале каждого хода
                /*for (Player player : players) {
                    System.out.print(player.getName() + ":");
                    outputPlayerCards(personCards.get(player));
                    System.out.println();
                }

                 */
                outputPlayers();
                //определение игрока, который ходит
                Player activePlayer = players.poll();

                //определение карт для хода
                activeCards = checkAvailableCard(personCards.get(activePlayer));
                remove(personCards.get(activePlayer), activeCards);
                System.out.print("Игрок " + activePlayer.getName() + " ходит на игрока " + players.peek().getName() + ": ");
                outputPlayerCards(activeCards);
                System.out.println();

            /*for(Player player: players){
                int size = personCards.get(players.peek()).size();
                activeCards = addActiveCard(activeCards, personCards.get(player));
                remove(personCards.get(player), activeCards);
                if(personCards.get(players.peek()).size()<size){
                    System.out.println("Игрок " + players.peek().getName() + " перевел ход на следующего после себя игрока");
                    players.add(activePlayer);
                    players.add(players.poll());
                    order++;
                    break;
                }
            }

             */
                reverse(activeCards, activePlayer);
                if(players.contains(activePlayer)){
                    order++;
                }
            System.out.print("После подкидывания карт: ");
            outputPlayerCards(activeCards);
            System.out.println();

                //определение карт, которыми можно отбиться
                finalDefendingCards = defending(activeCards);
                /*if (!(finalDefendingCards.contains(null))&&(finalDefendingCards.size() != 0) && (finalDefendingCards.size() == activeCards.size())) {
                    System.out.print("Игрок " + players.peek().getName() + " отбивается картами: ");
                    outputPlayerCards(finalDefendingCards);
                    System.out.println();
                    remove(personCards.get(players.peek()), finalDefendingCards);
                } else {
                    System.out.println("Нечем отбиться, игрок " + players.peek().getName() + " забирает карты себе");
                    for (Card card : activeCards) {
                        personCards.get(players.peek()).add(card);
                    }
                    flag++;
                }

                 */
                int size2 = personCards.get(players.peek()).size();
                endOfTheStep(activeCards, finalDefendingCards);
                if(personCards.get(players.peek()).size()>size2){
                    flag++;
                }
                finalDefendingCards.clear();

                //конец хода, игроки (по необходимости) добирают карты
                checkNumberOfCards(personCards.get(activePlayer));
                for (Player player : players) {
                    checkNumberOfCards(personCards.get(player));
                }

                if (order == 0) {
                    players.add(activePlayer);
                }
                if (flag == 1) {
                    players.add(players.poll());
                    flag = 0;
                }
                order = 0;

                endOfTheGame();

                System.out.println("------------------------------------------------------------");
                /*if (count == 3) {
                    break;
                }*/
                //count++;
            }
            if(personCards.size()==1){
                System.out.println("Игрок " + players.peek().getName() + " остался в дураках");
            }
        }
    }

    public List<Card> getFirstSixCards(List<Card> player){
        for (int i = 0; i < 6; i++) {
            player.add(getNextCard());
        }
        return player;
    }

    public void outputPlayerCards(List<Card> player){
        for(Card card:player){
            System.out.print(card.getRank().getUiString()+card.getSuit().getUiString());
            System.out.print(" ");
        }
    }

    public void outputPlayers(){
        for (Player player : players) {
            System.out.print(player.getName() + ":");
            outputPlayerCards(personCards.get(player));
            System.out.println();
        }
    }

    public static void remove(List<Card> player, List<Card> activeCards) {
        Collection<Card> collection = new ArrayList<>();
        for (Card card: player) {
            for (Card activeCard : activeCards) {
                if (activeCard == card){
                    collection.add(card);
                }
            }
        }
        player.removeAll(collection);
    }

    public void checkNumberOfCards(List<Card> player){
        while(player.size()<6){
            if (cards.size() != 0){
                player.add(getNextCard());
            }else{
                break;
            }
        }
    }

    public List<Card> checkAvailableCard (List<Card> player){
        List<Card> availableCards = new ArrayList<>();
        int max = 14;
        for (Card card: player){
            if(!(card.getSuit() == trumpCard)&&(card.getRank().getValue()<=max)){
                max=card.getRank().getValue();
            }
        }
        for (Card card: player){
            if(!(card.getSuit() == trumpCard)&&(card.getRank().getValue()==max)&&(availableCards.size()<personCards.get(players.peek()).size())){
                availableCards.add(card);
            }
        }
        if(availableCards.size()==0) {
            for (Card card : player) {
                if (card.getRank().getValue() <= max) {
                    max = card.getRank().getValue();
                }
            }

            for (Card card : player) {
                if ((card.getRank().getValue() == max)&&(availableCards.size()<personCards.get(players.peek()).size())) {
                    availableCards.add(card);
                }
            }
        }
        return availableCards;
    }

    public List<Card> addActiveCard(List<Card> activeCards, List<Card> player){
        for(Card cardPlayer: player){
            if (!(cardPlayer.getSuit() == trumpCard)&&(cardPlayer.getRank().getValue()==activeCards.get(0).getRank().getValue())&&(activeCards.size()<6)&&(activeCards.size()<personCards.get(players.peek()).size())){
                activeCards.add(cardPlayer);
            }
        }
        return activeCards;
    }

    public List<Card> checkStrongerCard(Card card, List<Card> player){
        List<Card> strongerCards = new ArrayList<>();
        for(Card cardPlayer: player){
            if((cardPlayer.getSuit() == trumpCard)&&(cardPlayer.getRank().getValue()>card.getRank().getValue())&&(card.getSuit() == trumpCard)){
                strongerCards.add(cardPlayer);
            }
            if((cardPlayer.getSuit() == card.getSuit())&&(cardPlayer.getRank().getValue()>card.getRank().getValue())&&(card.getSuit()!=trumpCard)){
                strongerCards.add(cardPlayer);
            }
        }
        if(strongerCards.isEmpty()){
            for(Card cardPlayer: player){
                if(cardPlayer.getSuit() == trumpCard){
                    strongerCards.add(cardPlayer);
                }
            }
        }
        return strongerCards;
    }

    public Card finalStrongerCard(List<Card> strongerCards, List<Card> finalDefendinfCards){
        int max = 14;
        remove(strongerCards, finalDefendinfCards);
        if(!(strongerCards.isEmpty())) {
            for (Card card : strongerCards) {
                if (card.getRank().getValue() <= max) {
                    max = card.getRank().getValue();
                }
            }
            for (Card card : strongerCards) {
                if (card.getRank().getValue() == max) {
                    return card;
                }
            }
        }
        return null;
    }

    public List<Card> defending(List<Card> activeCards){
        List<Card> defendingCards;
        List<Card> finalDefendingCards = new ArrayList<>();
        for (Card card : activeCards) {
            defendingCards = checkStrongerCard(card, personCards.get(players.peek()));
            if (defendingCards.size() != 0) {
                finalDefendingCards.add(finalStrongerCard(defendingCards, finalDefendingCards));
            }
        }
        Set<Card> duplicates = new LinkedHashSet<>(finalDefendingCards);
        finalDefendingCards.clear();
        finalDefendingCards.addAll(duplicates);

        return finalDefendingCards;
    }

    public void endOfTheStep(List<Card> activeCards, List<Card> finalDefendingCards){
        if (!(finalDefendingCards.contains(null))&&(finalDefendingCards.size() != 0) && (finalDefendingCards.size() == activeCards.size())) {
            System.out.print("Игрок " + players.peek().getName() + " отбивается картами: ");
            outputPlayerCards(finalDefendingCards);
            System.out.println();
            remove(personCards.get(players.peek()), finalDefendingCards);
        } else {
            System.out.println("Нечем отбиться, игрок " + players.peek().getName() + " забирает карты себе");
            for (Card card : activeCards) {
                personCards.get(players.peek()).add(card);
            }
            //flag++;
        }
    }

    public void reverse(List<Card> activeCards, Player activePlayer){
        for(Player player: players){
            int size = personCards.get(players.peek()).size();
            activeCards = addActiveCard(activeCards, personCards.get(player));
            remove(personCards.get(player), activeCards);
            if(personCards.get(players.peek()).size()<size){
                System.out.println("Игрок " + players.peek().getName() + " перевел ход на следующего после себя игрока");
                players.add(activePlayer);
                players.add(players.poll());
                break;
            }
        }
    }

    public void endOfTheGame(){
        personCards.keySet().removeIf(key -> personCards.get(key).isEmpty());

        Collection<Player> collection = new ArrayList<>();
        for (Player player: players) {
            if (!(personCards.containsKey(player))){
                collection.add(player);
                System.out.println("Игрок " + player.getName() + " выходит из игры, не будучи дураком!");
            }
        }
        players.removeAll(collection);
    }
}
