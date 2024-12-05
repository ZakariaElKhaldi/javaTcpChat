package clientSide;

import java.io.*;
import java.net.*;
import java.util.*;


public class MessageReciving implements Runnable{

   private BufferedReader reader;
      
   public MessageReciving(Socket socket){
      try{
         reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      }catch(IOException e){
         System.out.println(e.getStackTrace());
      }
   }

   @Override
   public void run(){
      try{

         while(true){System.out.println(reader.readLine());}

      }catch(IOException e){
         System.out.println(e.getStackTrace());
      }
   }
      
} 
