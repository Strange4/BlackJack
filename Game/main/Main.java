package main;

import main.objects.Player;
import main.ui.Colors;

public class Main {

    public static void main(String[] args)
    {
        play();
    }
    
    public static void play() {
        String welcomeMessage = Colors.addColor("Welcome to simplified Blackjack!", Colors.ANSI_YELLOW, null);
        System.out.println(welcomeMessage+"\n");

        String instructions = Colors.addColor("First, connect to your account.", Colors.ANSI_YELLOW, null);

        System.out.println(instructions+"\n");
        Player player = new Player();
        player.play();
    }

}