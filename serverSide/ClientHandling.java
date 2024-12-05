package serverSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandling implements Runnable {

    private ServerSocket serversocket;
    private ArrayList<Socket> clientsSocktes;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientHandling(ServerSocket serversocket, ArrayList<Socket> clientsSocktes) {
        this.serversocket = serversocket;
        this.clientsSocktes = clientsSocktes;
    }

    public void run() {
        try {
            while (true) {
                System.out.println("Waiting for connections...");
                Socket socket = serversocket.accept(); // Waits for a connection from the client
                clientsSocktes.add(socket); // Add the new socket to the list of clients

                // Create a reader and writer for communication with the client
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Received from client: " + reader.readLine()); // Read and print message from client

                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("You are connected"); // Send message to client

            }
        } catch (IOException ex) {
            System.out.println("Error in client handling: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

