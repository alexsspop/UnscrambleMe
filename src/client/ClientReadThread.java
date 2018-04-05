package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class is responsible to wait messages sent by the server and show it to the client
 */
public class ClientReadThread extends Thread {

    // The reference used to read messages sent by the server
    private BufferedReader reader;
    private GameClient gameClient;

    public ClientReadThread (Socket serverSocket, GameClient client) {
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
                // Wait for server messages
                String response = reader.readLine();
                if (response == null)
                    break;
                System.out.println("\n" + response);

                // prints the username after displaying the server's message
                if (gameClient.getUserName() != null)
                    System.out.print("[" + gameClient.getUserName() + "]: ");
            } catch (IOException ex) {
                break;
            }
        }
    }
}
