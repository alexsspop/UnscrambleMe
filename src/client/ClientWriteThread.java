package client;

import java.io.*;
import java.net.Socket;

/*
* This class is responsible to read messages from the client and send it to the server.
* */
public class ClientWriteThread extends Thread {
    // Reference to write messages to the server
    private PrintWriter writer;
    // Reference used to retrieve the server output stream
    private Socket serverSocket;
    private GameClient gameClient;

    public ClientWriteThread (Socket serverSocket, GameClient client) {
        this.serverSocket = serverSocket;
        this.gameClient = client;

        try {
            // Trying to retrieve the server output stream used to send messages to the sever
            OutputStream output = serverSocket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Thread.sleep(1000);

            System.out.print("Welcome! Enter your username: ");
            String userName = reader.readLine();

            gameClient.setUsername(userName);
            writer.println(userName);
            Thread.sleep(1000);

            String text;

            do {
                System.out.print("[" + userName + "]: ");

                // TODO print options
                text = reader.readLine();
                // TODO parse user input

                writer.println(text);

            } while (!text.equals(""));

            serverSocket.close();
            System.out.println("Exiting...");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
