package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible to start the game server, accept new connections and manage connected users.
 */
public class UnscrambleMeServer {
    // Virtual port number
    private int port;
    // Set of connected users
    private Set<String> userNames = new HashSet<>();
    // Set of connected users threads
    private Set<UserThread> userThreads = new HashSet<>();

    public UnscrambleMeServer (int port) {
        this.port = port;
    }

    public void start () {
        // Try to start the server
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            // Connection sucessfull
            System.out.println("Game Server is listening on port " + port);
            boolean done = true;

            while (done) {
                // Wait for new connections
                Socket socket = serverSocket.accept();
                Logger.getAnonymousLogger().log(Level.INFO, "New user connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }

            System.out.println("\nDisconnecting...");

        } catch (IOException ex) {
            // Failed on starting the server
            System.out.println("Error in the server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Delivers a message from one user to others (broadcasting)
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads) {
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconnected, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quitted");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    /**
     * Returns true if there are other users connected (not count the currently connected user)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntax: java ChatServer <port-number>");
            System.exit(0);
        }

        int port = Integer.parseInt(args[0]);

        UnscrambleMeServer server = new UnscrambleMeServer(port);
        server.start();
    }
}
