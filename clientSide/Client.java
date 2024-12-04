package clientSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
   
   private String name;
   private String password;

   private Socket socket;
   private Scanner scanner;

   private BufferedReader reader;
   private PrintWriter writer;

   public Client(){
      try{

         scanner = new Scanner(System.in);

         System.out.println("Inter name :");
         this.name = scanner.nextLine();
         
         System.out.println("Inter password  :");
         this.password = scanner.nextLine();

         socket = new Socket("localhost",8000);

         writer = new PrintWriter(socket.getOutputStream(),true);
         writer.println(this.name + "> connected");

         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         reader.readLine();

      }catch(Exception e){
         System.out.println(e.getStackTrace());
      }
   }
   
   public void messaging(){

      Thread sendThread = new Thread(new MessageSending(socket,this.name));
      sendThread.start();
      
      Thread reciveThread = new Thread(new MessageReciving(socket));
      reciveThread.start();

   }

   public static void main(String[] args){
      Client client = new Client();
      client.messaging();
   }

}
