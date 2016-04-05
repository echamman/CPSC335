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

            //Removes special characters, essentially rendering each word as only the word, no spaces or characters
            //Unfortunately I don't filter out periods due to some words actually containing a period and filtering them out would take a lot more work
            //I also don't filter out [ or ] because it is part of the formatting for the replace function
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

         /*//prints out symbols values, use for debugging
         for(int i = 0;i<symbols.length;i++){
            System.out.println(symbols[i]);
         }*/

         //This block inputs all encoded words into one big String so I know how many data bits I'll put into the header
         BufferedReader brEncode = new BufferedReader(new FileReader(args[0]));
         String code = "";

         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            String[] words = line.split(" ");

            for(String word : words){
               word = word.replaceAll("[-:;(),\"?]","");
               word = word.replaceAll("!","");
               word = word.toLowerCase();
               if(!word.trim().equals("")){
                  code+=er.encode(word);
                  int addBits = 8-(code.length()%8);
                  for(int i=0;i<addBits;i++){      //Adding extra zeroes if the word doesn't fill n bytes
                     code+="0";
                  }
               }
            }
            line = brEncode.readLine();
         }
         int nDataBits = code.length();
         brEncode.close();

         //This block writes the header data to the output file
         brEncode = new BufferedReader(new FileReader(args[0]));
         DataOutputStream out = new DataOutputStream(new FileOutputStream("out.txt"));
         LEDataOutputStream leOut = new LEDataOutputStream(out);

         leOut.writeBytes("C335");
         leOut.write(er.getNumChars());
         leOut.write(nDataBits);
         int counter = 0;
         while(counter<symbols.length){     //Parse through symbols until empty
            leOut.writeBytes(symbols[counter++]);    //Write character
            leOut.write(Integer.parseInt(symbols[counter++]+"")); //Write number of bits it has
            int numLoops = symbols[counter].length()/8;
            String bits = symbols[counter++];
            for(int i=0; i<numLoops;i++){                             //If a symbol has more than 8 bits of code, loop
               leOut.write((byte)(Integer.parseInt(bits.substring(0,8),2)));      //Write bits for symbol as int, cast as byte
               bits = bits.substring(8);
            }

         }

         //This block encodes the words using the probabilities
         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            String[] words = line.split(" ");

            for(String word : words){
               word = word.replaceAll("[-:;(),\"?]","");
               word = word.replaceAll("!","");
               word = word.toLowerCase();
               if(!word.trim().equals("")){
                  code = er.encode(word);
                  int addBits = 8-(code.length()%8);
                  for(int i=0;i<addBits;i++){      //Adding extra zeroes if the word doesn't fill n bytes
                     code+="0";
                  }
                  while(!(code.equals(""))){          //Write out bits 8 at a time
                     String temp = code.substring(0,8);
                     byte toWrite = ((byte)Integer.parseInt(temp, 2));
                     leOut.write(toWrite);
                     code = code.substring(8);
                  }
               }
            }
            line = brEncode.readLine();
         }
         brEncode.close();
         out.close();
         leOut.close();

      }catch(IOException e){
         e.printStackTrace();
         System.exit(0);
      }

   }
}
