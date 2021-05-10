package main.objects;

import java.util.Random;

public class Shoe {
    private Card[] shoe;
    private int cardCounter = -1; //Starts @ -1 to bias for the increment in the getNextCard()

    private final int CARDS_PER_DECK = 52;

    public Shoe()
    {
        createShoe();
    }

    // returns a card from the shoe.
    public Card getNextCard()
    {
        if(cardCounter == shoe.length -1)// Once the deck is over create a new deck
        {
            createShoe();
            cardCounter=-1;
        }

        cardCounter++;
        return shoe[cardCounter];
    }
    
    //Creating a deck of cards an un-shuffled deck of cards
    private Card[] createDeck()
    {
        Card[] deck = new Card[CARDS_PER_DECK];
        
        int suitIndex=0;
        int value = 0;
        for(int i=0;i< CARDS_PER_DECK;i++)
        {
            value++;
            deck[i]= new Card(value, suitIndex);
            
            if(value == 13) // After one suit is over the loop restarts for the next suit
            {
                value = 0;
                suitIndex++;
            }
        }
        return deck;
    }
    
    
    // Creates a shoe. That is a pack of 4 shuffled deck of cards
    
    /* Stores how many cards have been put into the shoe
       It will only put 4 of the same card in the shoe (One for every deck) */
    private void createShoe()
    {
        Card[] deck = createDeck();
        int[] indexOfCardsTaken = new int[deck.length];
        
        Random rand = new Random();
        int NUMBER_OF_DECKS = 4;
        this.shoe = new Card[CARDS_PER_DECK * NUMBER_OF_DECKS];
        
        for(int i=0;i<shoe.length;i++)
        {
            int cardChosen = rand.nextInt(deck.length);

            while(indexOfCardsTaken[cardChosen] == NUMBER_OF_DECKS)
            {
                cardChosen = rand.nextInt(deck.length);
            }
            indexOfCardsTaken[cardChosen] ++;
            
            this.shoe[i] = deck[cardChosen];
        }
    }
}
