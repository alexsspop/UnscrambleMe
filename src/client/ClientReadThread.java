package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ClientReadThread extends Thread {

    private BufferedReader reader;
    private Socket serverSocket;
    private GameClient gameClient;

    public ClientReadThread (Socket serverSocket, GameClient client) {
        this.serverSocket = serverSocket;
        this.gameClient = client;

        try {
            InputStream input = serverSocket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                if (response == null)
                    break;
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                if (gameClient.getUserName() != null) {
                    System.out.print("[" + gameClient.getUserName() + "]: ");
                }
            } catch (SocketException ex) {
                break;
            } catch (IOException ex) {
                System.out.println("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }
}