package main.logic;

import main.objects.Card;

public class Actions {

    // returns true if the hand in the input has busted
    public static boolean bust(Card[] hand)
    {
        int sum = sumOfCards(hand);
        return sum > 21;
    }

    //Checks if the hand is soft aka contains and ace
    public static boolean isSoft(Card[] hand)
    {
        boolean soft = false;
        for(Card card : hand)
        {
            if(card != null)
            {
                String[] nameSplit = card.getName().split(" ");
                if(nameSplit[0].equals("Ace"))
                {
                    soft = true;
                }
            } else break;
        }
        return soft;
    }

    
    // Checks if the player's hand is a natural aka a blackjack
    public static boolean isNatural(Card[] hand)
    {
        int cardsInArray=0;
        for(Card card : hand)
        {
            if(card != null)
            {
                cardsInArray++;
            } else break;
        }
        if(cardsInArray>2)
            return false;
        if(bust(hand))
            return false;
        if(!isSoft(hand))
            return false;

        return sumOfCards(hand) == 21;
    }

    //Counts the cards in a hand according if the hand is soft or not
    public static int sumOfCards(Card[] hand)
    {
        int sum = 0;
        for(Card card : hand)
        {
            if(card != null)
            {
                sum+= card.getValue();
            } else break;
        }
        
        // If the hand is soft and busts the Ace is counted as a 1
        if(isSoft(hand) && sum>21)
        {
            sum=0;
            for(Card card : hand)
            {
                if(card != null)
                {
                    if(card.getValue() == 11)
                    {
                        sum+=1;
                    } else if(card.getValue() != 11)
                    sum+= card.getValue();
                } else break;
            }
        }
        return sum;
    }
    
    //Returns true if the first hand is higher than the second hand
    public static boolean higherHand(Card[] hand1, Card[] hand2)
    {
        int sum1 = sumOfCards(hand1);
        int sum2 = sumOfCards(hand2);
        return sum1 > sum2;
    }
}