/*
   Author: Ethan Hamman
   StudentID: 10125341
   Main class: Scans words, calls functions from encoder
*/
import java.io.*;

public class as4{

   public static void main(String [] args){

      if(args.length != 2){
         System.out.println("Usage: as4 source_file output_file");
         System.exit(0);
      }

      encoder er = new encoder();

      try{
         //This block scans the file and writes the probabilities of all characters
         BufferedReader brScan = new BufferedReader(new FileReader(args[0]));

         String line = brScan.readLine();
         while(line != null){
            er.scan(line.toLowerCase());
            line = brScan.readLine();
         }

         brScan.close();

         er.makeTree();          //Create the tree based off the char's probabilities

         //This block encodes the words using the probabilities
         BufferedReader brEncode = new BufferedReader(new FileReader(args[0]));

         line = brEncode.readLine();
         while(line != null){
            //Encode
            line = brEncode.readLine();
         }

         brEncode.close();

      }catch(IOException e){
         System.out.println("IOException: "+e);
         System.exit(0);
      }

   }
}
