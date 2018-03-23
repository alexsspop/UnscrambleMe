/*
* This file contains the GameClient class that is responsible to connect to the server and instantiate two threads
* that are responsible to manage the communication between the client and the server.
* */

package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameClient {
    private String serverHostname;
    private int port;
    private String username;

    public GameClient(String serverHostname, int port) {
        this.serverHostname = serverHostname;
        this.port = port;
    }

    public void connect () {
        try {
            Socket clientSocket = new Socket(serverHostname, port);
            Logger.getAnonymousLogger().log(Level.INFO, "Connected to the game server");

            new ClientReadThread(clientSocket, this).start();
            new ClientWriteThread(clientSocket, this).start();

        } catch (UnknownHostException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getAnonymousLogger().log(Level.SEVERE, "I/O Error: " + ex.getMessage());
        }
    }

    public String getUserName() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Invalid arguments.");
            return;
        }

        String serverHostname = args[0];
        int port = Integer.parseInt(args[1]);

        new GameClient(serverHostname, port).connect();
    }

}
