package socketChat.clientSide;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	// Client properties
	private int id;
	private String userName;
	private Socket socket;
	private PrintWriter sender;
	private BufferedReader receiver;
	private Scanner scanner;
	private Thread sendMessageThread;
	private Thread receiveMessageThread;

	// Constructor to create the connection
	public Client(String userName, String address, int port) {
		try {
			socket = new Socket(address, port);

			// Send username first
			sender = new PrintWriter(socket.getOutputStream(), true);
			sender.println(userName);
			this.userName = userName;

			// Receive ID from server
			receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.id = receiver.read();

			scanner = new Scanner(System.in);
		} catch (IOException e) {
			System.out.println("Connection error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private Runnable sendMessageTask = new Runnable() {
		@Override
		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					String message = scanner.nextLine();
					sender.println(userName + "> " + message);
				}
			} catch (Exception e) {
				System.out.println("Send message error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	};

	private Runnable receiveMessageTask = new Runnable() {
		@Override
		public void run() {
			try {
				while (!Thread.currentThread().isInterrupted()) {
					String message = receiver.readLine();
					if (message != null) {
						System.out.println(message);
					}
				}
			} catch (IOException e) {
				System.out.println("Receive message error: " + e.getMessage());
				e.printStackTrace();
			}
		}
	};

	public void startConversation() {
		try {
			sendMessageThread = new Thread(sendMessageTask);
			sendMessageThread.start();

			receiveMessageThread = new Thread(receiveMessageTask);
			receiveMessageThread.start();

			// Wait for threads to complete (which they won't unless interrupted)
			sendMessageThread.join();
			receiveMessageThread.join();
		} catch (InterruptedException e) {
			System.out.println("Conversation interrupted: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			if (socket != null)
				socket.close();
			if (scanner != null)
				scanner.close();
			if (sendMessageThread != null)
				sendMessageThread.interrupt();
			if (receiveMessageThread != null)
				receiveMessageThread.interrupt();
		} catch (IOException e) {
			System.out.println("Error closing connection: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.print("Enter your username: ");
			String userName = scanner.nextLine();

			Client client = new Client(userName, "localhost", 8000);
			client.startConversation();
		} catch (Exception e) {
			System.out.println("Client startup error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
