package serverSide;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<Socket> clientSockets;

    public Server() {
        try {
            serverSocket = new ServerSocket(8000);
            clientSockets = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClients() {
        Thread thread = new Thread(new ClientHandling(serverSocket, clientSockets));
        thread.start();
    }

    public void messaging() {
        Thread readerThread = new Thread(new HandleInput(clientSockets, null)); // Pass null for now
        readerThread.start();
        Thread writerThread = new Thread(new HandleOutput(clientSockets, null, null)); // Pass null for now
        writerThread.start();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.handleClients(); // Start handling clients
        server.messaging();    // Start messaging system
    }
}

