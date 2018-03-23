package client;

import java.io.*;
import java.net.Socket;

/*
* This class is responsible to read messages from the client and send it to the server.
* */
public class ClientWriteThread extends Thread {
    // object to write messages to the server
    private PrintWriter writer;
    private Socket serverSocket;
    private GameClient gameClient;

    public ClientWriteThread (Socket serverSocket, GameClient client) {
        this.serverSocket = serverSocket;
        this.gameClient = client;

        try {
            OutputStream output = serverSocket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Welcome! Enter your username: ");
        String userName = null;
        try {
            userName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameClient.setUsername(userName);
        writer.println(userName);

        String text = "";

        do {
            // TODO read client input options
            System.out.print("[" + userName + "]: ");
            try {
                text = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer.println(text);

        } while (!text.equals("bye"));

        try {
            serverSocket.close();
        } catch (IOException ex) {

            System.out.println("Error writing to server: " + ex.getMessage());
        } finally {
            System.out.println("\nThank you! See you soon ^-^");
        }
    }
}