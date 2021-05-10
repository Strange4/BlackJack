package main;

import main.ui.Colors;

import java.net.Socket;
import java.io.*;
import java.util.Scanner;

public class Client {
    private static Socket socket;
    private static PrintWriter out;
    private static BufferedReader in;
    private static final Scanner keyboard = new Scanner(System.in);
    private static String username;
    private static String password;


    private static void open()
    {
        try{
            // Change local host for the ip address of where you are hosting your server files.
            socket = new Socket("localhost", 4999);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Closes the connection with the server
    private static void close()
    {
        try{
            socket.close();
            in.close();
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Gets the the money from a user with his password
    // TO-DO
    public static int getCredentials()
    {
        open();
        while(true)
        {
            System.out.println(("\nEnter your username"));
            System.out.println("Or If you don't have an account type 'new'");
            username = getUserInput();
            if(username.equalsIgnoreCase("new"))
            {
                newUser();
            } 
            else {
                System.out.println("\nEnter your password");
                password = getUserInput();
        
                out.println("Get User: "+username+" "+password);
                String serverResponse = null;
                try{
                    serverResponse = in.readLine();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                if (serverResponse == null)
                {
                    System.out.println(Colors.addColor("The connection with the server was lost", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
                    System.exit(1);
                }
                else if(serverResponse.equals("Your credentials couldn't be comfirmed."))
                {
                    System.out.println(Colors.addColor("______________________________________________", Colors.ANSI_CYAN, null));
                    System.out.println(Colors.addColor("\n"+serverResponse, Colors.ANSI_RED, null));
                }
                else
                {
                    System.out.println(Colors.addColor("______________________________________________", Colors.ANSI_CYAN, null));
                    System.out.println(Colors.addColor("\nWelcome "+username, Colors.ANSI_YELLOW, null));
                    close();
                    return Integer.parseInt(serverResponse);
                }
            }
        }
    }

    
    // updates the money of a user in the server files
    public static void updateMoney(int money)
    {
        if(money < 0){
            throw new IllegalStateException("The amount of money must not be negative");
        }
        open();
        out.println("Update Money: "+username+" "+password+" "+money);
        String serverResponse = null;
        try{
            serverResponse = in.readLine();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        if(serverResponse == null)
        {
            System.out.println(Colors.addColor("The connection with the server has been severed.", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
        } else {
            if(!serverResponse.equals("The money was updated"))
                System.out.println("[SERVER] "+serverResponse);
        }
        close();
    }

    // Creates a new user on the server files
    private static void newUser()
    {
        System.out.println("\nEnter the username you want to use");
        username = getUserInput();

        System.out.println("\nEnter the password you want to use");
        password = getUserInput();

        out.println("Set User: "+username+" "+password);
        String serverResponse = null;
        try{
            serverResponse = in.readLine();
            if(serverResponse == null)
            {
                System.out.println(Colors.addColor("The connection with the server has been severed.", Colors.ANSI_BLACK, Colors.ANSI_BG_RED));
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(Colors.addColor("______________________________________________", Colors.ANSI_CYAN, null));
        System.out.println("[SERVER] "+ serverResponse);
        System.out.println(Colors.addColor("______________________________________________\n", Colors.ANSI_CYAN, null));
    }

    //tries to get a user some input without spaces
    private static String getUserInput()
    {
        String input;
        do{
            System.out.println("It can't contain spaces or be empty\n");
            input = keyboard.nextLine();
        } while (input.equals("") || input.contains(" "));
        return input;
    }
}
