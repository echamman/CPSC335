import java.util.*;

public class index{

   private int depth;
   private bucket[] buckets;
   private int bucket_size;

   public index(int bucket_size){
      depth = 1;
      this.bucket_size = bucket_size;
      buckets = new bucket[2];
      buckets[0] = new bucket(bucket_size);
      buckets[1] = new bucket(bucket_size);
   }

   public void insert(String word){
      int hash = (7*word.hashCode())+word.hashCode()%11;
      int spot = ((1<<depth)-1)&hash;                  //Finds the last n bits of the hash
      bucket toInsert = buckets[spot];
      int stat = toInsert.insert(word);               //Checks the status that is returned from inserting into the bucket
      int index_size = buckets.length;

      if(stat == -1){

         if(toInsert.get_depth() == depth){           //Add more indexes + split the bucket
            depth++;
            //System.out.println("--"+index_size*2+"--"+depth);
            bucket[] temp = new bucket[index_size*2];

            for(int b=0; b<index_size;b++)            //fill first half of temp
               temp[b] = buckets[b];
            for(int b=0; b<index_size;b++)            //Fill second half with links to matching first half
               temp[b+index_size] = temp[b];
            buckets = temp;                           //make buckets now equal to our filled temp array
         }
                                                            //Split the bucket
         String[] temp_word = buckets[spot].clearList();    //returns a list of all strings in the bucket to be reinserted
         int bucket_depth = buckets[spot].get_depth();
         int temp_spot = ((1<<bucket_depth)-1)&spot;        //Finding the new spot (adding a one to the beginning of the old spot)
         temp_spot = ((1<<(bucket_depth)))|temp_spot;

         buckets[spot].inc_depth();
         buckets[temp_spot] = new bucket(bucket_size);
         buckets[temp_spot].set_depth(bucket_depth+1);

         for(int b=(temp_spot+1); b<buckets.length; b++){          //Link all the buckets to the new bucket
            int b_temp = ((1<<bucket_depth)-1)&b;
            if((b&temp_spot)==temp_spot && (b_temp|temp_spot)==temp_spot)
               buckets[b] = buckets[temp_spot];
         }
         insert(word);                                     //Insert word
         for(String new_word : temp_word){
            insert(new_word);                                  //Insert words taken out of old bucket
         }
      }else
         //System.out.println(spot+": "+word);
         return;                                      //Word inserted correctly
   }

   public int find(String word){
      int hash = (7*word.hashCode())+word.hashCode()%11;
      int spot = ((1<<depth)-1)&hash;
      return buckets[spot].find(word);

   }
/*                                                          //Linear probing search function used for testing
   public int find_linear(String word){
      int hash = (7*word.hashCode())+word.hashCode()%11;
      int spot = ((1<<depth)-1)&hash;
      return buckets[spot].find_linear(word);
   }*/

}
