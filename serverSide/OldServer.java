package serverSide;

import java.io.*;
import java.net.*;
import java.util.*;

// Server class to manage client connections and messages
public class Server {
	private static int clientIdCounter = 1; // To assign unique IDs to clients
	private static final List<ClientHandler> clients = new ArrayList<>(); // List of active clients

	public static void main(String[] args) {
		try (ServerSocket serverSocket = new ServerSocket(8000)) {
			System.out.println("Server is running on port 8000...");
			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(clientSocket, clientIdCounter++);
				clients.add(clientHandler);
				new Thread(clientHandler).start();
			}
		} catch (IOException e) {
			System.err.println("Server error: " + e.getMessage());
		}
	}

	// Broadcast message to all clients
	static synchronized void broadcastMessage(String message, int senderId) {
		for (ClientHandler client : clients) {
			if (client.getClientId() != senderId) { // Avoid sending the message back to the sender
				client.sendMessage(message);
			}
		}
	}

	// Remove a client from the active clients list
	static synchronized void removeClient(ClientHandler clientHandler) {
		clients.remove(clientHandler);
	}
}

// ClientHandler to manage individual client connections
class ClientHandler implements Runnable {
	private final Socket socket;
	private final int clientId;
	private PrintWriter writer;
	private BufferedReader reader;
	private String userName;

	public ClientHandler(Socket socket, int clientId) {
		this.socket = socket;
		this.clientId = clientId;
	}

	public int getClientId() {
		return clientId;
	}

	public void sendMessage(String message) {
		writer.println(message);
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);

			// Send client ID to the client
			writer.println(clientId);

			// Read the client's username
			userName = reader.readLine();
			System.out.println("Client " + clientId + " (" + userName + ") connected.");

			// Notify other clients about the new connection
			Server.broadcastMessage("Server: " + userName + " has joined the chat.", clientId);

			// Listen for client messages and broadcast them
			String message;
			while ((message = reader.readLine()) != null) {
				System.out.println("From Client " + clientId + ": " + message);
				Server.broadcastMessage(message, clientId);
			}
		} catch (IOException e) {
			System.err.println("Client " + clientId + " error: " + e.getMessage());
		} finally {
			try {
				socket.close();
				Server.removeClient(this);
				System.out.println("Client " + clientId + " disconnected.");
				Server.broadcastMessage("Server: " + userName + " has left the chat.", clientId);
			} catch (IOException e) {
				System.err.println("Error closing connection for client " + clientId + ": "
						+ e.getMessage());
			}
		}
	}
}
