/*
   Author: Ethan Hamman
   StudentID: 10125341
   Main class: Scans words, calls functions from encoder
   Unfortunately, encoding big files is slow
   Included Classes:
   Encoder: Forms both the priority queue and the huffman tree, then encodes the words
   probNode: Probability Node, holds the frequency for every character, then gets sorted into the priority queue by encoder
   huffmanTree: Used to form a huffmanTree, each object is a node
   LEDataOutputStream: Writes the bytes to the output file
*/
import java.io.*;
import java.util.*;

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
            line = line.toLowerCase();
            String[] words = line.split(" ");

            for(String word : words){
               word = word.replaceAll("[-:;(),\"?]","");
               word = word.replaceAll("!","");
               word = word.toLowerCase();
               if(!word.trim().equals(""))
                  er.scan(word);
            }
            line = brScan.readLine();
         }

         brScan.close();

         er.makeTree();          //Create the tree based off the char's stored probabilities
         String[] symbols = er.encodings();  //Create a String containing the Symbol, nBits, and bits for each character

         //This block prints out symbols values
         Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"));
         int i=0;
         while(i<symbols.length){
            wr.write("Character: "+symbols[i]+"\n");
            wr.write("Ascii Value: "+(int)symbols[i++].charAt(0)+"\n");
            wr.write("Frequency: "+symbols[i++]+"\n");
            wr.write("Encoded Bit Representation: "+symbols[i++]+"\n\n");
         }
         wr.write("Encoded Message:\n");

         //This block encodes the words using the probabilities
         BufferedReader brEncode = new BufferedReader(new FileReader(args[0]));
         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            String[] words = line.split(" ");

            for(String word : words){
               word = word.replaceAll("[-:;(),\"?]","");
               word = word.replaceAll("!","");
               word = word.toLowerCase();
               if(!word.trim().equals("")){
                  wr.write(er.encode(word)+"\n");
               }
            }
            line = brEncode.readLine();
         }
         brEncode.close();
         wr.close();

      }catch(IOException e){
         e.printStackTrace();
         System.exit(0);
      }

   }
}
