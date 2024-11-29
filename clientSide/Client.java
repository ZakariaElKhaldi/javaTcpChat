package socketChat;

import java.io.*;
import java.net.*;

public class Client {
	public static void main(String[] args) {
		try (
				Socket socket = new Socket("localhost", 8000);
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
			System.out.println("hello");
			System.out.println("client connected");
		} catch (IOException e) {
			System.out.println(e.getStackTrace());
		}
	}
}
