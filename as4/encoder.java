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

   //Sorts nodes by probabilities, inserts them into the tree
   //There must be nodes! This function does not error check for whether any words have been scanned()
   public void makeTree(){
      int numNodes = 1;             //Count number of nodes
      probNode curr = head;
      while(curr.getNext()!=null){
         numNodes++;
         curr = curr.getNext();
      }

      int[] counts = new int[numNodes];         //Sorting elements into to matching arrays, sorted by probabilities
      String[] characters = new String[numNodes];

      int count;
      char character;
      probNode toInsert = null;
      for(int i=0; i<numNodes;i++){          //Insert all characters and counts into the sorted arrays
         curr = head;
         count = Integer.MAX_VALUE;

         while(curr!=null){                     //Find node with lowest count, then remove it
            if(curr.getCount() < count){
               toInsert = curr;
               count = curr.getCount();
            }
            curr = curr.getNext();
         }
         character = toInsert.getChar();           //Insert into arrays
         characters[i] = ""+character;
         counts[i] = count;
         if(toInsert.getPrev()==null){                //Remove the node, replace head if necessary
            head = toInsert.getNext();
         }else{
            toInsert.getPrev().setNext(toInsert.getNext());
         }
         if(toInsert.getNext()!=null)
            toInsert.getNext().setPrev(toInsert.getPrev());
      }
      //Create tree
      huffmanTree right = new huffmanTree(characters[0], counts[0])
      huffmanTree left = new huffmanTree(characters[1], counts[1])
      counts[1] = counts[0] + counts[1];
      characters[1] = characters[0] + "*" + characters[1];
      counts = shrinkCounts(counts);
      characters = shrinkChars(characters);
      while(counts.length>1){



      }

   }

   private int[] shrinkCounts(int[] counts, int insert){
      int[] temp = new int[counts.length-1];
      for(int i=0; i<counts.length-1;i++){
         temp[i] = counts[i+1];
      }
      return temp;
   }

   private String[] shrinkChars(String[] characters, int insert){
      int[] temp = new int[characters.length-1];
      for(int i=0; i<characters.length-1;i++){
         temp[i] = characters[i+1];
      }
      return temp;
   }

   //Encodes a word using the probabilities
   public void encode(String word){return;}
}
