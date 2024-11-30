package socketChat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
	// the clent propertires
	private int id;
	public String userName;

	Socket socket;
	PrintWriter sende;
	BufferedReader receive;
	Scanner scanner;

	Thread sendMessageThread;
	Thread receiveMessageThread;

	// constrector to create the connection
	public Client(String userName, String addres, int port) {
		try {
			socket = new Socket(addres, port);

			sende = new PrintWriter(socket.getOutputStream(), true);
			sende.println(userName);
			this.userName = userName;

			receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.id = receive.read();
			scanner = new Scanner(System.in);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	Runnable sendMessageTask = new Runnable() {
		public void run() {
			while (true) {
				sende.write(userName + "> " + scanner.nextLine());
			}
		}
	};

	Runnable receiveMessageTask = new Runnable() {
		public void run() {
			while (true) {
				try {
					System.out.print(receive.readLine());
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
		}
	};

	public void startConcersation() {
		try {
			sendMessageThread = new Thread(sendMessageTask);
			sendMessageThread.start();

			receiveMessageThread = new Thread(receiveMessageTask);
			sendMessageThread.start();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	public static void main(String[] args) {
		try {
			Scanner scanner = new Scanner(System.in);
			Client client = new Client(scanner.nextLine(), "localhost", 8000);
			client.startConcersation();
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}
