import java.util.*;

public class bucket{

   private int size;
   private ArrayList<Character> bucket;
   private int word_count;
   private int max_words;
   private int local_depth;

   public bucket(int bucket_size){
      size = bucket_size;
      max_words = size/40;                //Figuring out how many words can be stored in the bucket
      word_count = 0;
      bucket = new ArrayList<Character>();
      local_depth = 1;
      }

   public int insert(String word){               //Returning int as a status code

      char[] temp = word.toCharArray();
      if(word_count == max_words){
         return -1;
      }else{                                                //Sorts words based on hash values
         int k=0;
         boolean spot_found = false;
         int hash = (7*word.hashCode())+word.hashCode()%11;
         while(k < word_count && !spot_found){
            String temp_word = getWordAt(k*20);
            int temp_hash = (7*temp_word.hashCode())+temp_word.hashCode()%11;
            if(hash < temp_hash)
               k++;
            else
               spot_found = true;
         }

         insert_into_array(k*20, temp, word);
      }
      return 0;
   }

   //returns the word at index
   private String getWordAt(int index){
      String retWord = "";
      for(int i=index;i<index+20;i++){
         if(bucket.get(i) != ' ')
            retWord += bucket.get(i);
      }
      return retWord;
   }

   //Inserts a string into the bucket
   private void insert_into_array(int index, char[] temp, String word){
      int s = bucket.size();
      boolean spot_full = true;

      //Checking if the spot is full
      try{
         if(bucket.get(index) != null){
            spot_full = true;
         }
      }catch(IndexOutOfBoundsException e){
         spot_full = false;
      }

      /*
         If s=0, the array is empty and add the word to the beginning
         If spot_full is True, we need to move all items past that up a 'spot'
         Else, add the word to the end of the list
      */
      if(s==0){
         for(int i=0; i < word.length(); i++)   //Add all characters in string
            bucket.add(temp[i]);
         for(int i=word.length(); i < 20; i++)  //Add blanks for the rest of the designated 20 characters
            bucket.add(' ');
         word_count++;
      }else if(spot_full == true){
         for(int i=0; i<20; i++)                //Add twenty new spots
            bucket.add((i+s), ' ');
         for(int i=0; i<(s-index); i++)         //Move every item over to the next designated spot
            bucket.set((bucket.size()-i-1), bucket.get(s-i-1));

         for(int i=0; i < word.length(); i++)   //Add all characters in string into the newly opened spot
            bucket.set((index+i), temp[i]);
         for(int i=word.length(); i < 20; i++)  //Add blanks for the rest of the designated 20 characters
            bucket.set((index+i), ' ');
         word_count++;
      }else{
         for(int i=0; i < word.length(); i++)   //Add all characters in string
            bucket.add((index+i), temp[i]);
         for(int i=word.length(); i < 20; i++)  //Add blanks for the rest of the designated 20 characters
            bucket.add((index+i), ' ');
         word_count++;
      }
   }

   //Returns list of words in bucket to be reinserted when bucket is full, empties bucket before returning
   public String[] clearList(){

      String[] list = new String[max_words];
      String temp = "";

      for(int i=0; i<word_count; i++){
         for(int c=i*20; c < (i+1)*20; c++){
            if(bucket.get(c) != ' ')
               temp += bucket.get(c);
         }
         list[i] = temp;
         temp = "";
      }
      bucket.clear();
      word_count=0;
      return list;
   }

   public int find(String word){
      String[] buckArray = new String[word_count];          //Put all words in bucket into string[] for easier searching
      for(int i = 0; i<word_count; i++){
         buckArray[i] = getWordAt(i*20);
      }

      int hash = (7*word.hashCode())+word.hashCode()%11;

      if(word_count==1){return 1;}                          //Binary search based on hash values
      else if(word_count==0){return -1;}
      else{
         int c = 1;
         boolean found = false;
         while(!found){
            if(buckArray.length!=1){
               String temp = buckArray[buckArray.length/2];
               int temp_hash = (7*temp.hashCode())+temp.hashCode()%11;
               if(hash == temp_hash && word == temp)
                  return c;
               else if(hash==temp_hash && word !=temp){                                //Unfortunately I need to do a linear search when I encounter
                  String quickWord =temp;                                              //words with the same hash
                  int quickHash = temp_hash;
                  int spot = (buckArray.length/2)-1;
                  while(word != quickWord && hash == quickHash){
                     c++;
                     try{
                        buckArray[spot] = quickWord;
                        quickHash = (7*quickWord.hashCode())+quickWord.hashCode()%11;
                     }catch(ArrayIndexOutOfBoundsException e){
                        break;
                     }
                     spot--;
                  }
                  if(word == quickWord){
                     return c;
                  }
                  else{
                     String[] temp_array = new String[buckArray.length/2];
                     for(int i =0; i< temp_array.length;i++)
                        temp_array[i] = buckArray[i+buckArray.length/2];
                     buckArray=temp_array;
                  }
               }else if(hash < temp_hash){                                     //Both else statements make a new array that is half the size, then fills it
                  String[] temp_array = new String[buckArray.length/2];       //with half the items in the original array, either the top half or bottom
                  for(int i =0; i< temp_array.length;i++)                     //half depending on if the word hash is greater than or less than the
                     temp_array[i] = buckArray[i];                            //word we are looking for
                  buckArray=temp_array;
                  c++;
               }else{
                  String[] temp_array = new String[buckArray.length/2];
                  for(int i =0; i< temp_array.length;i++)
                     temp_array[i] = buckArray[i+buckArray.length/2];
                  buckArray=temp_array;
                  c++;
               }
            }else{found=true;}
         }
         return c;
      }
   }
/*                                                 //Linear Probing search function
   public int find_linear(String word){
      for(int i = 0; i<word_count; i++){
         if(word.equals(getWordAt(i*20))){return i+1;}
      }
      return -1;

   }*/

   public int get_depth(){
      return local_depth;
   }

   public void set_depth(int depth){
      local_depth = depth;
   }

   public void inc_depth(){
      local_depth++;
   }
}
