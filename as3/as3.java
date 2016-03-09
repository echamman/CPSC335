/*
   Author: Ethan Hamman
   StudentID: 10125341
   Main class: reads files, inserts into Trie, and checks spelling
*/
import java.io.*;

public class as3{

   public static void main(String [] args){

      if(args.length != 2){
         System.out.println("Usage: 'as3 dictionary file_to_check'.");
         System.exit(0);
      }

      String dictionary = args[0];
      String spellCheck = args[1];
      trieController tc = new trieController();

      try{
         FileReader fr = new FileReader(dictionary);
         BufferedReader br = new BufferedReader(fr);

         String line = br.readLine();
         while(line != null){
            //Insert dictionary items
            tc.insert(line);
            line = br.readLine();
         }

         br.close();
         fr.close();

         FileReader frs = new FileReader(spellCheck);
         BufferedReader brs = new BufferedReader(frs);

         line = brs.readLine();
         while(line != null){
            //Spell Check all items
            tc.spellCheck(line);
            line = brs.readLine();
         }


         brs.close();
         frs.close();

      }catch(IOException e){
         System.out.println("IOException: "+e);
         System.exit(0);
      }
   }
}
