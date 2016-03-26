/*
   Author: Ethan Hamman
   StudentID: 10125341
   Encoder class
*/

public class encoder{

   private probMap head;
   private int charCount;


   public encoder(){
      head = null;
      charCount=0;

   }



   //Scans a word and increments the count for every character
   public void scan(String word){
      char c;
      if(head==null){
         head = new probMap(word.charAt(0));
      }
      probMap curr = head;

      for(int i=0; i<word.length(); i++){
         c = word.charAt(i);
         curr = head;
         while(c>curr.getChar() && curr.getNext()!=null)
            curr = curr.getNext();

         if(c==curr.getChar()){     //increment the count on this character
            curr.inc();
         }
         else if(c<curr.getChar()){       //Insert into the list
            probMap insert = new probMap(c, curr, curr.getPrev());
            if(insert.getPrev()!=null)
               insert.getPrev().setNext(insert);
            curr.setPrev(insert);
            if(insert.getPrev()==null)
               head = insert;
            insert.inc();
         }
         else{       //At the end of the list
            probMap insert = new probMap(c, null, curr);
            curr.setNext(insert);
            insert.inc();
         }
         charCount++;
      }
   }

   //Print probabilities for every character (Debugging)
   public void printProbs(){
      probMap curr = head;
      while(curr!=null){
         System.out.println(curr.getChar()+ ": "+curr.getProb(charCount));
         curr = curr.getNext();
      }
   }

   //Encodes a word using the probabilities
   public void encode(String word){return;}
}
