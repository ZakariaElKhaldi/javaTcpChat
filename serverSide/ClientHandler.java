package ServerSide;

import java.io.*;
import java.net.*;
import java.utils.*;

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
