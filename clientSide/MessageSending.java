package clientSide;

import java.io.*;
import java.net.*;
import java.util.*;

public class MessageSending implements Runnable{

   private PrintWriter writer;
   private Scanner scanner;
   private String senderName;
      
   public MessageSending(Socket socket,String senderName){

      try{

         this.senderName = senderName;

         scanner = new Scanner(System.in); 
         writer = new PrintWriter(socket.getOutputStream(),true);

      }catch(IOException e){
         System.out.println(e.getStackTrace());
      }

   }

   @Override
   public void run(){
      try{

         while(true){writer.println(senderName+"> "+scanner.nextLine());}

      }catch(IOException ex){

         System.out.println(ex.getStackTrace());

      }
   }
      
} 
