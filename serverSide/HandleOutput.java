package ServerSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class HandleOutput implements Runnable {

    private ArrayList<Socket> clientsSockets;
    private BufferedReader reader;
    private PrintWriter writer;

    public HandleOutput(ArrayList<Socket> clientsSockets, PrintWriter writer, BufferedReader reader) {
        this.reader = reader;
        this.clientsSockets = clientsSockets;
        this.writer = writer;
    }

    public void writeToClient() {
        for (Socket socket : clientsSockets) {
            try {
                writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(reader.readLine());
            } catch (IOException e) {
                System.out.println("Error writing to client: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            while (true) {
                synchronized (clientsSockets) {
                    writeToClient();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error in run method: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

