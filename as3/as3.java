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
            tc.insert(line.toLowerCase());
            line = br.readLine();
         }

         br.close();
         fr.close();

         FileReader frs = new FileReader(spellCheck);
         BufferedReader brs = new BufferedReader(frs);

         line = brs.readLine();
         while(line != null){
            //Spell Check all items
            String[] words = line.split(" ");

            //Removes special characters, essentially rendering each word as only the word, no spaces or characters
            //Unfortunately I don't filter out periods due to some words actually containing a period and filtering them out would take a lot more work
            //I also don't filter out [ or ] because it is part of the formatting for the replace function
            for(String word : words){
               word = word.replaceAll("[-:;(),\"?]","");
               word = word.replaceAll("!","");
               word = word.toLowerCase();
               if(!word.equals(""))
                  tc.spellCheck(word);
            }
            line = brs.readLine();
         }


         brs.close();
         frs.close();

      }catch(IOException e){
         System.out.println("IOException: "+e);
         System.exit(0);
      }

      tc.printStats();
   }
}
