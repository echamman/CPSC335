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

      huffmanTree[] trees = new huffmanTree[numNodes];     //Sorting elements into an array of tree nodes

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
         trees[i] = new huffmanTree(""+character, count);
         if(toInsert.getPrev()==null){                //Remove the node, replace head if necessary
            head = toInsert.getNext();
         }else{
            toInsert.getPrev().setNext(toInsert.getNext());
         }
         if(toInsert.getNext()!=null)
            toInsert.getNext().setPrev(toInsert.getPrev());
      }
      //Create tree
      huffmanTree right = null;
      huffmanTree left = null;
      while(trees.length>2){
         right = trees[0];
         left = trees[1];
         trees = shrinkArray(trees, right, left);
      }
      try{
         int newCount = trees[0].getFreq() + trees[1].getFreq();
         String newString = trees[0].getData().replace("*","") + "*" + trees[1].getData().replace("*","");
         root = new huffmanTree(newString, newCount);     //Store the completed tree as root
         root.setRight(trees[0]);
         root.setLeft(trees[1]);
      }catch(Exception e){
         root = trees[0];
      }
   }

   //Shrink trees array by removing lowest two counts, and adding a new "root" tree into the appropriately sorted spot
   private huffmanTree[] shrinkArray(huffmanTree[] trees, huffmanTree right, huffmanTree left){
      int newCount = right.getFreq() + left.getFreq();
      String newString = right.getData().replace("*","") + "*" + left.getData().replace("*","");
      huffmanTree tempRoot = new huffmanTree(newString, newCount);
      tempRoot.setRight(right);
      tempRoot.setLeft(left);
      huffmanTree[] tempTrees = new huffmanTree[trees.length-1];
      int i=0;
      while((i+2)<trees.length && trees[i+2].getFreq() < newCount){
         tempTrees[i] = trees[i+2];                //i+2 signifies skipping the first two spots, as they are to be removed
         i++;
      }
      tempTrees[i] = tempRoot;
      i++;
      while(i<tempTrees.length){
         tempTrees[i] = trees[i+1];               //i+1 because we incremented i without checking the i+2 spot on trees
         i++;
      }
      return tempTrees;
   }

   //Encodes a word using the probabilities
   public String encode(String word){
      String encoded = "";
      huffmanTree curr = null;
      String[] split = null;

      char[] wordArray = new char[word.length()];

      for(int i = 0; i<word.length();i++)
         wordArray[i] = word.charAt(i);

      for(char c: wordArray){
         curr = root;
         while(!curr.getData().equals(c+"")){
            split = curr.getData().split("\\*");
            if(split[0].contains(c+"")){
               encoded+="0";
               curr = curr.getRight();
            }
            else if(split[1].contains(c+"")){
               encoded+="1";
               curr = curr.getLeft();
            }
            else{          //In case the character is not in the tree *shouldn't happen*
               System.out.println("Character not in tree: " + c);
               System.exit(0);
            }
         }
      }

      return encoded;
   }
}
