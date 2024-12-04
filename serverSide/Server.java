package serverSide;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server{

   private ServerSocket serversocket;
   private Socket socket;

   private BufferedReader reader;
   private PrintWriter writer;

   public Server(){
      try{
         serversocket = new ServerSocket(8000);
         System.out.println("connecting...");
         socket = serversocket.accept();

         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         System.out.println(reader.readLine()); 

         writer = new PrintWriter(socket.getOutputStream(),true);
         writer.println("Your connected");
      }catch(Exception e){
         System.out.println(e.getStackTrace());
      }
   }

   public static void main(String[] args) {
      Server server = new Server();
   }
}
