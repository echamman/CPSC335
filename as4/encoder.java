/*
   Author: Ethan Hamman
   StudentID: 10125341
   Collects all characters and handles their counts
   Also encrypts words
*/

public class encoder{

   private probNode head;
   private huffmanTree root;
   private int charCount;


   public encoder(){
      head = null;
      root = null;
      charCount=0;

   }



   //Scans a word and increments the count for every character
   public void scan(String word){
      char c;
      if(head==null){
         head = new probNode(word.charAt(0));
      }
      probNode curr = head;

      for(int i=0; i<word.length(); i++){
         c = word.charAt(i);
         curr = head;
         while(c>curr.getChar() && curr.getNext()!=null)
            curr = curr.getNext();

         if(c==curr.getChar()){     //increment the count on this character
            curr.inc();
         }
         else if(c<curr.getChar()){       //Insert into the list
            probNode insert = new probNode(c, curr, curr.getPrev());
            if(insert.getPrev()!=null)
               insert.getPrev().setNext(insert);
            curr.setPrev(insert);
            if(insert.getPrev()==null)
               head = insert;
            insert.inc();
         }
         else{       //Insert at the end of the list
            probNode insert = new probNode(c, null, curr);
            curr.setNext(insert);
            insert.inc();
         }
         charCount++;
      }
   }

   //Print probabilities for every character (Debugging)
   public void printProbs(){
      probNode curr = head;
      while(curr!=null){
         System.out.println(curr.getChar()+ ": "+curr.getProb(charCount));
         curr = curr.getNext();
      }
   }

   public void makeTree(){
      return;
   }

   //Encodes a word using the probabilities
   public void encode(String word){return;}
}
