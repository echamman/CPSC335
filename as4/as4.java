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
         String symbols = er.encodings();  //Create a String containing the Symbol, nBits, and bits for each character

         //This block encodes the words using the probabilities
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
         while(!(symbols.equals(""))){     //Parse through symbols until empty
            leOut.writeChar(symbols.charAt(0));    //Write character
            leOut.write(Integer.parseInt(""+symbols.charAt(1)));  //Write number of bits it has
            symbols=symbols.substring(2);
            String bits = symbols.substring(0,8);
            symbols=symbols.substring(8);
            leOut.write(Byte.parseByte(bits,2));      //Write bits for symbol in hex

         }

         line = brEncode.readLine();
         while(line != null){
            line = line.toLowerCase();
            code = er.encode(line);
            int addBits = 8-(code.length()%8);
            for(int i=0;i<addBits;i++){      //Adding extra zeroes if the word doesn't fill n bytes
               code+="0";
            }
            while(!(code.equals(""))){          //Write out bits 8 at a time in hex
               String temp = code.substring(0,8);
               byte toWrite = Byte.parseByte(temp, 2);
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
