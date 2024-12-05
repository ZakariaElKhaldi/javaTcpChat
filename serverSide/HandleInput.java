package ServerSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class HandleInput implements Runnable {

    private ArrayList<Socket> clientsSockets;
    private BufferedReader reader;

    public HandleInput(ArrayList<Socket> clientsSockets, BufferedReader reader) {
        this.clientsSockets = clientsSockets;
        this.reader = reader;
    }

    public void readInput() {
        for (Socket socket : clientsSockets) {
            try {
                if (socket.getInputStream().available() > 0) {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String inputLine = reader.readLine();
                    if (inputLine != null) {
                        // Process the input from the client
                        System.out.println("Received: " + inputLine);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            while (true) {
                synchronized (clientsSockets) {
                    readInput();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in run method: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

