/*
   Author: Ethan Hamman
   StudentID: 10125341
   Main class:
*/
import java.io.*;

public class as4{

   public static void main(String [] args){

      if(args.length != 2){
         System.out.println("Usage: as4 source_file output_file");
         System.exit(0);
      }

      try{
         FileReader fr = new FileReader(/*file*/);
         BufferedReader br = new BufferedReader(fr);

         String line = br.readLine();
         while(line != null){
            //Encode
         }

         br.close();
         fr.close();


      }catch(IOException e){
         System.out.println("IOException: "+e);
         System.exit(0);
      }

   }
}
