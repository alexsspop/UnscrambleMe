package server;

import java.io.*;
import java.net.*;

/**
 * This class is responsible to receive messages from a single client and redirect it to the server.
 * Each connected client have a instance of UserThread running in server side.
 */
public class UserThread extends Thread {
    // The client socket reference. Used to communicate with the client
    private Socket userSocket;
    // A server reference used to communicate with the server
    private UnscrambleMeServer server;
    // A reference used to send messages to the client
    private PrintWriter userWriter;

    public UserThread(Socket socket, UnscrambleMeServer server) {
        this.userSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            // Retrieving the reference used to send messages to the client
            OutputStream output = userSocket.getOutputStream();
            userWriter = new PrintWriter(output, true);

            // Retrieving the reference used to read messages sent by the client
            InputStream input = userSocket.getInputStream();
            BufferedReader userReader = new BufferedReader(new InputStreamReader(input));

            // Waiting for client messages
            String userName = userReader.readLine();
            server.addUserName(userName);
            printUsers();

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            while (true) {
                // TODO parse client input options
                // Waiting for client message
                clientMessage = userReader.readLine();

                if (clientMessage == null)
                    break;

                if (clientMessage.equals("bye")) {
                    System.err.println("DIGITOU BYE");
                    break;
                }

                serverMessage = "[" + userName + "]: " + clientMessage;
                // Broadcasting the received message
                server.broadcast(serverMessage, this);

            }

            server.removeUser(userName, this);
            userSocket.close();

            serverMessage = userName + " has quited.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            // Failed to retrieve the user message
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    private void printUsers() {
        if (server.hasUsers())
            userWriter.println("Connected users: " + server.getUserNames());
        else
            userWriter.println("No other users connected");
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        userWriter.println(message);
    }
}
