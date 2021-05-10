import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Server {
    private static final ArrayList<ClientHandler> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);
    
    public static void main(String[] args) throws IOException
    {
        ServerSocket listener = new ServerSocket(4999);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");

        try
        {
            while (true)
            {
                Date date = new Date(System.currentTimeMillis());
                String now = formatter.format(date);

                System.out.println("[SERVER] Waiting for new connection...");
                Socket newSocket = listener.accept();
                System.out.println("[SERVER] New client has been connected! "+now+"\n");


                ClientHandler clientThread = new ClientHandler(newSocket);

                clients.add(clientThread);
                pool.execute(clientThread);
                clients.removeIf(client -> !client.isAlive());
            }
        }
        finally{
            listener.close();
        }
        
    }
}
