package main.objects;

import main.logic.Actions;
import main.ui.Colors;
import main.ui.Table;

import java.util.Scanner;

public class Player {
    private int betAmount;
    private int moneyInAccount;
    private Card[] playerHand;
    private Card[] dealerHand;
    private final Dealer dealer;
    private final Scanner scan = new Scanner(System.in);
    private boolean firstDecision = true;

    public Player()
    {
        this.moneyInAccount = Client.getCredentials();
        this.dealer = new Dealer();
        this.playerHand = dealer.getPlayerHand();
        this.dealerHand = dealer.getDealerHand();
        System.out.println(Colors.addColor("\nYou have "+moneyInAccount+"$ in your account.\n", Colors.ANSI_GREEN, null));
        getBet();
    }

    // Plays the game
    public void play()
    {
        boolean playAgain;
        do
        {
            playRound();
            
            playAgain = playAgain();
            if(playAgain)
            {
                //After the round is over. Create new hands for next round
                dealer.getNewHands();
                this.playerHand = dealer.getPlayerHand();
                this.dealerHand = dealer.getDealerHand();
                this.firstDecision = true;

                System.out.println(Colors.addColor("\n♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠ ♠\n", Colors.ANSI_CYAN, null));
                getBet();
            }

        }while(playAgain);
        System.out.println(Colors.addColor("\nThanks for playing!", Colors.ANSI_CYAN, null));
    }

    // Tries to get a bet from the user until the bet is the right amount and type.
    private void getBet()
    {
        boolean rightInput = false;
        String input;
        while(!rightInput)
        {
            System.out.println(Colors.addColor("Enter the amount of funds you want to bet. Minimum bet is 5$.", Colors.ANSI_YELLOW, null));
            try
            {
                input = scan.nextLine();
                betAmount= Integer.parseInt(input);
                rightInput = true;
                if(moneyInAccount < 5)
                {
                    System.out.println("\n\nYou do not have enough funds to play at this table.");
                    System.out.println("Please stop gambling");
                    System.exit(0);
                }
                if(betAmount < 5)
                {
                    System.out.println("\n\bThat bet is too low.");
                    rightInput = false;
                }
                if(betAmount > moneyInAccount)
                {
                    System.out.println("\n\nYou do not have enough funds to bet that much.");
                    System.out.println("Funds in account: "+moneyInAccount);
                    rightInput = false;
                }
            }
            catch (Exception e)
            {
                System.out.println("\nThat is not a right answer for a bet! Try again with only a number.");
            }

        }
        moneyInAccount -=betAmount;
        Client.updateMoney(moneyInAccount);
    }

    private void playRound()
    {
        boolean roundOver = false;
        if(Actions.isNatural(playerHand))
        {
            if(!Actions.isNatural(dealerHand))
                System.out.println(Colors.addColor("\nYou won with a Natural!", Colors.ANSI_GREEN, null));
            else
                System.out.println(Colors.addColor("\nYou have a natural but so does the dealer.", Colors.ANSI_GREEN, null));
            roundOver = true;
        }
        while(!roundOver)
        {
            printGame(false);
            String choice = getChoice();
            roundOver = doMove(choice);
            firstDecision = false;
        }
        printGame(true);
        printWinner();
        System.out.println();
    }

    private boolean playAgain()
    {
        String choice;
        do
        {
            System.out.println(Colors.addColor("\nDo you want to play another round? Enter yes or no", Colors.ANSI_YELLOW, null));
            choice = scan.nextLine();
            if(choice.equalsIgnoreCase("yes"))
            {
                return true;
            }
            else if(choice.equalsIgnoreCase("no"))
                return false;
            else
            {
                System.out.println(Colors.addColor("\nPlease enter either yes or no", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
                System.out.println(Colors.addColor("______________________________\n", Colors.ANSI_CYAN, null));
            }
        } while(true);
    }

    private void printWinner()
    {
        if( ( (Actions.higherHand(playerHand, dealerHand) || Actions.bust(dealerHand) ) && !Actions.bust(playerHand)) || Actions.isNatural(playerHand))
        {
            int amountWon;
            final double NATURAL_WIN = 1.5;

            if(Actions.isNatural(playerHand))
            {
                amountWon =  (int) (betAmount*NATURAL_WIN);
            } else {
                amountWon = (betAmount);
            }
            moneyInAccount += amountWon + betAmount;
            Client.updateMoney(moneyInAccount);
            System.out.println(Colors.addColor(Table.center("You won "+amountWon+"$ this round!", 30), Colors.ANSI_GREEN, null));
            System.out.println(Colors.addColor("\nYour total funds in your account are: "+moneyInAccount+"$", Colors.ANSI_GREEN, null));
        }
        else if(Actions.bust(playerHand) || (Actions.higherHand(dealerHand, playerHand) && !Actions.bust(dealerHand)))
        {
            System.out.println(Colors.addColor(Table.center("You lost "+betAmount+"$ this round.", 30), Colors.ANSI_RED, null));
            System.out.println(Colors.addColor("\nYour total funds in your account are: "+moneyInAccount+"$", Colors.ANSI_GREEN, null));
        } else {
            moneyInAccount += betAmount;
            Client.updateMoney(moneyInAccount);
            System.out.println(Colors.addColor("\n\n Your hand is the same value as the dealer. It's a draw.", Colors.ANSI_CYAN, null));
            System.out.println(Colors.addColor("\nYour total funds in your account are: "+moneyInAccount+"$", Colors.ANSI_GREEN, null));
        }
        if(moneyInAccount<5){
            System.out.println(Colors.addColor("\n\nYou don't have enough funds to play again.", Colors.ANSI_RED, null));
            System.out.println(Colors.addColor("Please stop gambling", Colors.ANSI_RED, null));
            System.exit(0);
        }
    }

    // does a move and returns if the round is over by busting or by standing.
    private boolean doMove(String choice)
    {
        if(choice.equals("hit"))
        {
            playerHand = dealer.hit();
            if(Actions.bust(playerHand))
                return true;
        }
        if(choice.equals("stand"))
        {
            return true;
        }
        if(choice.equals("double down"))
        {
            final double DOUBLED_BET = 2;
            moneyInAccount -= betAmount;
            betAmount *= DOUBLED_BET;
            Client.updateMoney(moneyInAccount);
            System.out.println(Colors.addColor("\n________________________", Colors.ANSI_CYAN, null));
            String alert = Colors.addColor("Your new bet: "+betAmount+"$", Colors.ANSI_RED, null);
            System.out.println(Table.center(alert, 10));
            System.out.println(Colors.addColor("________________________", Colors.ANSI_CYAN, null));
        }
        return false;
    }

    //Prints the game using the table class
    private void printGame(boolean roundOver)
    {

        int tableLength = Table.tableLength(playerHand);

        System.out.println(Table.center("══Dealer══", tableLength));
        Table.printCard(!roundOver, dealerHand);
        
        System.out.println(Table.center("══Player══", tableLength));
        
        Table.printCard(false, playerHand);
    }

    private void printChoices()
    {
        String hit = Colors.addColor("Hit", Colors.ANSI_GREEN, null);
        String stand = Colors.addColor("Stand", Colors.ANSI_GREEN, null);
        String doubleDown = Colors.addColor("Double Down", Colors.ANSI_GREEN, null);

        System.out.format("%1$s %3$4s %2$s %3$4s", hit, stand, "");
        if(firstDecision && (moneyInAccount-betAmount) >=0)
            System.out.format(doubleDown+" %4s", "");
        System.out.println();
    }

    private String getChoice()
    {
        String[] choices = {"hit", "stand", "double down"};
        String input = "";
        boolean madeChoice = false;
        
        while(!madeChoice)
        {
            String instruction = Colors.addColor("\n\nPlease make a choice from the available options.\n", Colors.ANSI_YELLOW, null);
            System.out.println(instruction);
            printChoices();
            input = scan.nextLine();
            for(String choice : choices)
            {
                if(choice.equalsIgnoreCase(input))
                {
                    madeChoice = true;
                    break;
                }
            }
            if(input.equalsIgnoreCase("double down") && (!firstDecision || (moneyInAccount-betAmount<0)))
            {
                madeChoice = false;
                if(!firstDecision){
                    System.out.println(Colors.addColor("\nYou can't double down after the first round.", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
                }
                else
                {
                    System.out.println(Colors.addColor("\nYou do not have enough funds to double down", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
                    System.out.println(Colors.addColor("Your total funds in your account are: "+moneyInAccount+"$", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
                    System.out.println(Colors.addColor("_________________________________________________________\n", Colors.ANSI_CYAN, null));
                }
            } 
            else if(!madeChoice)
            {
                System.out.println(Colors.addColor("\n----That's not a valid choice!----",Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
            }
            
        }
        System.out.println(Colors.addColor("________________________\n", Colors.ANSI_CYAN, null));
        return input.toLowerCase();
    }

}