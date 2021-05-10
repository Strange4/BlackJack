package main.ui;

import main.objects.Card;

import java.util.Arrays;

public class Table {

    // returns a formatted string in the middle of the specified width
    public static String center(String input, int width)
    {
        if (input.length() < 1) {
            throw new IllegalStateException("The width of the input must be at least 1");
        }
        if(width < 2){
            throw new IllegalStateException("The width of the space must be at least 2");
        }
        int sideLength = (width - input.length())/2;

        // If the input contains color, the length should be considered as just the character itself
        if(input.contains("["))
        {
            sideLength = (width - (input.length() - Colors.ANSI_LENGTH))/2;
        }
        String format = "%2$"+sideLength+"s"+input+"%2$"+sideLength+"s";
        return String.format(format, input, "");
    }


    // Prints the cards in a pretty way! If the first card is down, it will only print two cards
    public static void printCard(boolean firstCardDown, Card ...cards)
    {
        if(cards.length<1)
            throw new IllegalArgumentException("Wait. How can you print a card if there is no card? Put a card in the method please");

        Card[] copyCards = copyCards(cards);
        if(firstCardDown)
        {
            setNullCards(copyCards);
            copyCards = copyCards(copyCards);
        }
        String[] cardValues = getValues(copyCards);
        String[] cardSuits = getSuits(copyCards);
        String[] lines = new String[7];
        Arrays.fill(lines,"");

        for(int i=0;i<lines.length;i++)
        {
            if(i==0) {
                repeatString(lines, i, "┌─────────┐ ", copyCards.length);
            }
            if(i==1)
                for(int e=0;e<copyCards.length;e++){
                    if(firstCardDown && e==0)
                        lines[i] += "│░░░░░░░░░│ ";
                    else
                        lines[i] += String.format("│%-9s│ ", cardValues[e]);
                }
            if(i==2){
                if(firstCardDown){
                    repeatString(lines, i ,"│░░░░░░░░░│ ", 1);
                    repeatString(lines, i, "│"+center(" ", 10) + "│ ", copyCards.length -1);
                } else {
                    repeatString(lines, i, "│"+center(" ", 10) + "│ ", copyCards.length);
                }
            }
            if(i==3)
                for(int e=0;e<copyCards.length;e++){
                    if(firstCardDown && e==0)
                        lines[i] += "│░░░░░░░░░│ ";
                    else
                        lines[i] += "│"+center(cardSuits[e], 10)+ "│ ";
                }
            if(i==4){
                if(firstCardDown){
                    repeatString(lines, i ,"│░░░░░░░░░│ ", 1);
                    repeatString(lines, i, "│"+center(" ", 10) + "│ ", copyCards.length -1);
                } else {
                    repeatString(lines, i, "│"+center(" ", 10) + "│ ", copyCards.length);
                }
            }
            if(i==5)
                for(int e=0;e<copyCards.length;e++){
                    if(firstCardDown && e==0)
                        lines[i] += "│░░░░░░░░░│ ";
                    else
                        lines[i] += String.format("│%9s│ ", cardValues[e]);
                }
            if(i==6){
                repeatString(lines, i, "└─────────┘ ", copyCards.length);
            }
            lines[i] += "\n";
        }

        for (String line : lines) {
            System.out.print(line);
        }
    }

    // Repeats a string in a specific part of a string array
    private static void repeatString(String[] stringArray, int location, String toRepeat, int numberOfTimes)
    {
        for(int e=0;e<numberOfTimes;e++)
        {
            stringArray[location] += toRepeat;
        }
    }

    // Sets all the cards after the first two cards to null
    private static void setNullCards(Card[] toNull)
    {
        for(int i=0;i<toNull.length;i++)
        {
            if(i>1)
            {
                toNull[i] = null;
            }
        }
    }

    // Creates a new copy of card[] having different references
    private static Card[] copyCards(Card[] cardsToCopy)
    {
        int notNullCounter=0;
        for(int i=0;i<cardsToCopy.length && cardsToCopy[i] != null;i++)
        {
            notNullCounter++;
        }
        Card[] newCopy = new Card[notNullCounter];
        System.arraycopy(cardsToCopy, 0, newCopy, 0, notNullCounter);
        return newCopy;
    }

    // finds the suits of a card[] and returns a string[] width the suits
    private static String[] getSuits(Card[] cards)
    {
        String ace = "♠";
        String hearts = Colors.addColor("♥", Colors.ANSI_RED, null);
        String clubs = "♣";
        String diamonds = Colors.addColor("♦", Colors.ANSI_RED, null);

        String[] suitNames = {"Spades", "Hearts", "Clubs", "Diamonds"};
        String[] cardSuits = new String[cards.length];
        String[] suits = {ace, hearts, clubs, diamonds};

        for(int i=0;i<suitNames.length;i++)
        {
            for(int e=0;e<cards.length && cards[e] != null;e++)
            {
                if(cards[e].getSuit().equals(suitNames[i]))
                {
                    cardSuits[e] = suits[i];
                }
            }
        }
        return cardSuits;
    }

    // Finds the values to be printed on a card in a card[] returns the values in a string[]
    private static String[] getValues(Card[] cards)
    {
        String[] cardNames = {"Jack","Queen","King", "Ace"};
        String[] suitNames = {"Spades", "Hearts", "Clubs", "Diamonds"};
        String[] cardValues = new String[cards.length];

        for(int i=0;i<cardNames.length;i++)
        {
            for(int e=0;e<cards.length && cards[e] != null;e++)
            {
                if(cards[e].getName().split(" ")[0].equals(cardNames[i]))
                {
                    cardValues[e] = cards[e].getName().split(" ")[0].charAt(0)+"";
                }
                else if (cards[e].getName().split(" ")[0].equals(suitNames[i]))
                {
                    cardValues[e] = cards[e].getValue()+"";
                }
            }
        }

        return cardValues;
    }

    // Finds the length of the cards if they were to be printed
    public static int tableLength(Card[] cards)
    {
        final int SIZE_OF_CARDS = 12;
        
        int cardCounter =0;
        for(int i=0;i<cards.length && cards[i] != null;i++)
            cardCounter ++;

        return SIZE_OF_CARDS * cardCounter;
    }
}
