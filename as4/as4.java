/*
   Author: Ethan Hamman
   StudentID: 10125341
   Main class: Scans words, calls functions from encoder
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
            er.scan(line.toLowerCase());
            line = brScan.readLine();
         }

         brScan.close();

         er.makeTree();          //Create the tree based off the char's stored probabilities
         String[] symbols = er.encodings();  //Create a String containing the Symbol, nBits, and bits for each character
         //This block encodes the words using the probabilities
         for(int i = 0;i<symbols.length;i++){
            System.out.println(symbols[i]);
         }
         BufferedReader brEncode = new BufferedReader(new FileReader(args[0]));


         //This inputs all encoded words into one big byte array so I know how many data bits I'll put into the header
         String code = "";

         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            code+=er.encode(line);
            int addBits = 8-(code.length()%8);
            for(int i=0;i<addBits;i++){      //Adding extra zeroes if the word doesn't fill n bytes
               code+="0";
            }
            line = brEncode.readLine();
         }
         int nDataBits = code.length();
         brEncode.close();

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

         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            code = er.encode(line);
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
