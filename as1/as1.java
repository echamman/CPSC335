import java.io.*;

public class as1{
   public static void main(String [] args){

      if(args.length != 3){
         System.out.println("You must enter 3 arguments: file name, number of keys, and the bucket size.");
         System.exit(0);
      }else if(Integer.parseInt(args[1]) < 1){
         System.out.println("There must be words in the file.");
         System.exit(0);
      }else if(Integer.parseInt(args[2]) < 40){
         System.out.println("The bucket size must be greater than 40 bytes.");
         System.exit(0);
      }

      String file_name = args[0];
      int number_keys = Integer.parseInt(args[1]);
      int bucket_size = Integer.parseInt(args[2]);


      String word = null;
      int i = number_keys;
      index indy = new index(bucket_size);
      //Insert all words
      try{
         FileReader fr = new FileReader(file_name);
         BufferedReader br = new BufferedReader(fr);

         while(i > 0 && (word = br.readLine()) != null){
            indy.insert(word);
            i--;
         }
         br.close();

      }catch(FileNotFoundException e){
         System.out.println("File does not exist.");
      }catch(IOException e){
         System.out.println("Error reading file.");
      }
      //Find all words
      i = number_keys;
      float numProbes = 0;
      try{
         FileReader fr = new FileReader(file_name);
         BufferedReader br = new BufferedReader(fr);
         int probe;
         while(i > 0 && (word = br.readLine()) != null){
            probe = indy.find(word);
            if(probe==-1)
               System.out.println("Can't find " + word);
            else{
               numProbes += probe;
            }
            i--;
         }
         numProbes = numProbes/(number_keys-i);
         br.close();
         System.out.println("Average number of probes = " + numProbes);
      }catch(FileNotFoundException e){
         System.out.println("File does not exist.");
      }catch(IOException e){
         System.out.println("Error reading file.");
      }
   }
}
