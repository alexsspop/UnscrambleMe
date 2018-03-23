package server;

import java.io.*;
import java.net.*;

public class UserThread extends Thread {
    private Socket userSocket;
    private UnscrambleMeServer server;
    private PrintWriter userWriter;

    public UserThread(Socket socket, UnscrambleMeServer server) {
        this.userSocket = socket;
        this.server = server;
    }

    public void run() {
        try {
            OutputStream output = userSocket.getOutputStream();
            userWriter = new PrintWriter(output, true);

            InputStream input = userSocket.getInputStream();
            BufferedReader userReader = new BufferedReader(new InputStreamReader(input));

            String userName = userReader.readLine();
            server.addUserName(userName);
            printUsers();

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            while (true) {
                // TODO parse client input options
                clientMessage = userReader.readLine();
                if (clientMessage == null)
                    break;
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            }

            server.removeUser(userName, this);
            userSocket.close();

            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            userWriter.println("Connected users: " + server.getUserNames());
        } else {
            userWriter.println("No other users connected");
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        userWriter.println(message);
    }
}
