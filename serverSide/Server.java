package socketChat.serverSide;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
	private static final int PORT = 8000;
	// Thread-safe set of client handlers
	private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
	private static final ExecutorService threadPool = Executors.newCachedThreadPool();

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

	// Inner class to handle individual client connections
	private static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final int clientId;
		private PrintWriter out;
		private BufferedReader in;
		private String userName;

		public ClientHandler(Socket socket, int id) {
			this.clientSocket = socket;
			this.clientId = id;
		}

		@Override
		public void run() {
			try {
				// Set up input and output streams
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new PrintWriter(clientSocket.getOutputStream(), true);

				// Send client ID
				out.println(clientId);

				// Receive username
				userName = in.readLine();
				System.out.println(userName + " has joined the chat (ID: " + clientId + ")");
				broadcastMessage(userName + " has joined the chat");

				// Handle incoming messages
				String message;
				while ((message = in.readLine()) != null) {
					broadcastMessage(message);
				}
			} catch (IOException e) {
				System.out.println("Error handling client " + userName + ": " + e.getMessage());
			} finally {
				// Cleanup when client disconnects
				if (userName != null) {
					broadcastMessage(userName + " has left the chat");
				}
				clients.remove(this);
				try {
					clientSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// Method to broadcast messages to all connected clients
		private void broadcastMessage(String message) {
			synchronized (clients) {
				for (ClientHandler client : clients) {
					client.out.println(message);
				}
			}
		}
	}
}
