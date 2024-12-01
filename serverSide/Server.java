package socketChat.serverSide;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
	private static final int PORT = 8000;
	// Thread-safe set of client handlers
		public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.println("Chat Server is running on port " + PORT);

			// Counter to assign unique IDs to clients
			int clientId = 0;

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket);

				// Create a new client handler for each connected client
				ClientHandler clientHandler = new ClientHandler(clientSocket, clientId++);
				clients.add(clientHandler);

				// Start the client handler in a thread pool
				threadPool.execute(clientHandler);
			}
		} catch (IOException e) {
			System.out.println("Server error: " + e.getMessage());
			e.printStackTrace();
		} finally {
			// Shutdown the thread pool
			threadPool.shutdown();
		}
	}

	
