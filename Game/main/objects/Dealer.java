package main.objects;

import main.logic.Actions;

// The dealer creates and distributes the cards for the player and the dealer.
public class Dealer {
    private final Shoe shoe;
    private Card[] playerHand;
    private Card[] dealerHand;


    public Dealer()
    {
        shoe = new Shoe();
        getNewHands();
        
    }

    // Creates and gives a new hand for the player and the dealer.
    public void getNewHands()
    {
        // The maximum hypothetical hand with 4 decks (16 Aces and 3 two's)
        int MAX_HYPOTHETICAL_HAND = 19;
        playerHand = new Card[MAX_HYPOTHETICAL_HAND];
        dealerHand = new Card[MAX_HYPOTHETICAL_HAND];
        //The number of cards dealt first to the player and the dealer in BlackJack.
        int BASE_CARDS = 2;
        for(int i = 0; i< BASE_CARDS; i++)
        {
            playerHand[i] = shoe.getNextCard();
            dealerHand[i] = shoe.getNextCard();
        }
        while(Actions.sumOfCards(dealerHand)<17 || (Actions.isSoft(dealerHand) && Actions.sumOfCards(dealerHand) == 17))
        {
            dealerHit();
        }
    }

    // Gives a new card to the dealer
    private void dealerHit()
    {
        for(int i=0;i<dealerHand.length;i++)
        {
            if(dealerHand[i] == null)
            {
                dealerHand[i] = shoe.getNextCard();
                break;
            }
        }
    }

    // Adds an additional hand to the player
    public Card[] hit()
    {
        if(playerHand[0] == null)
            throw new IllegalArgumentException("This hand is empty.");
        
        for(int i=0;i<playerHand.length;i++)
        {
            if(playerHand[i] == null)
            {
                playerHand[i] = shoe.getNextCard();
                break;
            }
        }
        return playerHand;
    }

    // returns the player hand
    public Card[] getPlayerHand()
    {
        return this.playerHand;
    }

    // returns the dealer hand
    public Card[] getDealerHand()
    {
        return this.dealerHand;
    }

}