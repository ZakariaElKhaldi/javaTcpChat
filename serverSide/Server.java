package socketChat;

import java.io.*;
import java.net.*;

public class Server {
	public static void main(String[] args) {
		try (
				ServerSocket serversocket = new ServerSocket(8000);
				Socket socket = serversocket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

			System.out.println("client connected");
			while (true) {

			}

		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}
}
