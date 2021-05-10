package main.objects;

public class Card {
    private final int cardNumber;
    private final String suit;
    private final String[] names = {"Jack","Queen","King"};

    public Card(int cardNumber, int suitIndex)
    {
        if(cardNumber<1 || cardNumber > 13)
        {
            throw new IllegalArgumentException("The card number of the card is not within range. 1-13");
        }
        if(suitIndex > 3 || suitIndex < 0)
            throw new IllegalArgumentException("The suit index must 0-3 inclusively");

        String[] suits = {"Spades", "Hearts", "Clubs", "Diamonds"};
        this.suit = suits[suitIndex];
        this.cardNumber = cardNumber;
    }

    // Returns the value of the card according to Blackjack rules
    public int getValue()
    {
        if(cardNumber>10)
        {
            return 10;
        }
        if(cardNumber == 1)
        {
            return 11;
        }

        return this.cardNumber;
    }

    // Returns the name of the of the card if it has one
    // returns the suit if it doesn't
    public String getName()
    {
        String name = setName();
        if(name.equals(""))
        {
            return suit;
        }
        return setName()+" "+suit;
    }

    // Returns the name corresponding to their card numbers. If there are no names it returns an empty string
    private String setName()
    {
        if(cardNumber>10)
        {
            return names[cardNumber-11]; 
        }else if(cardNumber == 1){
            return "Ace";
        }
        return "";
    }

    // Prints a compressive view of the card
    // For debugging purposes because it doesn't look pretty
    public String toString()
    {
        if(getValue() == 11)
        {
            return getName()+" Value: 1 or 11";
        }
        return getName()+" Value: "+getValue();
    }

    // Returns the suit
    public String getSuit()
    {
        return this.suit;
    }
}