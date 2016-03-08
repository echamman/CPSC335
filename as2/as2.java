/*
   Author: Ethan Hamman, 10125341
*/

import java.io.*;

public class as2{

   public static void main(String [] args){

      if(args.length != 1){
         System.out.println("Usage: 'as2 file_name'.");
         System.exit(0);
      }

      String filename = args[0];
      bController bc = new bController();

      try{
         FileReader fr = new FileReader(filename);
         BufferedReader br = new BufferedReader(fr);

         String line = br.readLine();
         while(line != null){
            int numInsert = Integer.parseInt(line);
            bc.insert(numInsert);

            line = br.readLine();
         }

         br.close();
         fr.close();

      }catch(IOException e){
         System.out.println("IOException: "+e);
         System.exit(0);
      }

      bc.reportStats();
   }
}
