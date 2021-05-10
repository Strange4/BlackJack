import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable{

    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;
    private final String path = Files.createFile("Passwords", "user-data");

    public ClientHandler(Socket clientSocket) throws IOException
    {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);  
    }
    
    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                String request = in.readLine();
                if(request == null)
                {
                    System.out.println("[SERVER] The connection with the client has been reset\n");
                    break;
                }
                if(checkRequest(request))
                    break;
            }
        } catch (IOException e) {
            if(e.getMessage().equals("Connection reset"))
            {
                System.out.println("[SERVER] The connection with the client has been reset\n");
            }
            else {
                System.out.println("There was an IOException in th client handler");
                e.printStackTrace();
            }
        }
        finally {
            try {
                client.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Goes through all the possible requests and returns true if something went wrong
    private boolean checkRequest(String request)
    {
        if(request.contains("\n") || request.contains("\t") || request.contains("\b") || request.contains("\r") || request.contains("\f"))
        {
            out.println("Your request cannot contain special escaped characters");
            return true;
        }
        
        if(request.contains("Set User: "))
        {
            return setUser(request);
        }
        
        else if(request.contains("Get User: "))
        {
            return getUser(request);
        }
            
        else if(request.contains("Update Money: "))
        {
            return updateMoney(request);
        }
        else{
            out.println("Commands... Set User: [username] [password], Get User: [username] [password]. Update Money: [username] [password] [money]");
            return true;
        }
    }

    // Updates the amount of money a user has
    // Returns true if something went wrong
    private boolean updateMoney(String request)
    {
        String[] inputs = request.split(" ");
        String username = inputs[2];
        String password = inputs[3];
        String money = inputs[4];
        if(inputs.length != 5)
        {
            out.println("The command 'Update Money: ' has too many arguments");
            return true;
        }
        if(Files.updateMoney(path, username+" "+password, money))
        {
            out.println("The money was updated");
            return false;
        } else {
            out.println("The money in the account couldn't be updated. Did you use a non-integer for updating?");
            return true;
        }
    }

    // Outputs the amount of money of a user has
    // returns true if something went wrong
    private boolean getUser(String request)
    {
        String[] inputs = request.split(" ");
        String username = inputs[2];
        String password = inputs[3];
        String userData = null;
        if(inputs.length != 4)
        {
            out.println("The command 'Get User: ' has too many arguments");
            return true;
        }

        try{
            userData = Files.findUser(path, username+" "+password);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        if(userData == null)
        {
            out.println("Your credentials couldn't be confirmed.");
        } else {
            String money = userData.split(" ")[2];
            out.println(money);
        }
        return false;
    }

    // Creates a new user
    // returns true if something went wrong
    private boolean setUser(String request)
    {
        String[] inputs = request.split(" ");
        if(inputs.length != 4)
        {
            out.println("The command 'Set User: ' has too many arguments");
            return true;
        }
        String username = inputs[2];
        String password = inputs[3];
        int DEFAULT_CHIPS = 500;
        Files.writeLine(path, username+" "+password+" "+ DEFAULT_CHIPS);
        out.println(username+" has been added as a user");
        return false;
    }

    // Returns true if this client handler is still connected to the client
    public boolean isAlive()
    {
        return client.isClosed();
    }
}
