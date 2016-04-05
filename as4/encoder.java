/*
   Author: Ethan Hamman
   StudentID: 10125341
   Collects all characters and handles their counts
   Also encrypts words
*/
import java.util.*;

public class encoder{

   private probNode head;
   private huffmanTree root;
   private int charCount;
   private int uniqueChar;

   public encoder(){
      head = null;
      root = null;
      charCount=0;
      uniqueChar=0;
   }

   public int getNumChars(){
      return uniqueChar;
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
            uniqueChar++;
         }
         else{       //Insert at the end of the list
            probNode insert = new probNode(c, null, curr);
            curr.setNext(insert);
            insert.inc();
            uniqueChar++;
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


   //Returns a byte array containing symbol, nBits, and the bits, for every character
   public String[] encodings(){
      String chars = root.getData();
      chars = chars.replaceAll("\\*","");
      ArrayList<String> toReturn = new ArrayList<String>();
      char c;
      int count = 0;

      for(int i=0; i<chars.length();i++){
         c=chars.charAt(i);
         String encoding = charCode(c);
         toReturn.add(c+"");
         toReturn.add(encoding.length()+"");
         int addBits = 8-(encoding.length()%8);
         for(int j=0;j<addBits;j++){
            encoding+="0";
         }
         toReturn.add(encoding);        //Encoding will be "'c''nBits''bits'" toReturn is this repeated for every char c, bits will always be of length 8
      }
      return toReturn.toArray(new String[0]);
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

   /*Encodes a word using the tree
   public byte[] encode(String word){
      byte[] encoded = new byte[0];
      byte[] tempEncoded;
      byte[] charCodes;
      huffmanTree curr = null;
      String[] split = null;

      char[] wordArray = new char[word.length()];

      for(int i = 0; i<word.length();i++)
         wordArray[i] = word.charAt(i);

      for(char c: wordArray){
         charCodes = charCode(c);
         tempEncoded = new byte[encoded.length + charCodes.length-1];
         for(int i=0; i<encoded.length;i++){
            tempEncoded[i] = encoded[i];
         }
         int k=1;
         for(int j=encoded.length;j<tempEncoded.length;j++){
            tempEncoded[j] = charCodes[k];
            k++;
         }
         encoded = tempEncoded;
      }

      return encoded;
   }*/

   //Encodes a word using the tree
   public String encode(String word){
      huffmanTree curr = null;
      String[] split = null;
      String code ="";

      char[] wordArray = new char[word.length()];

      for(int i = 0; i<word.length();i++){
         curr = root;
         char c = word.charAt(i);
         while(!curr.getData().equals(c+"")){
            split = curr.getData().split("\\*");
            if(split[0].contains(c+"")){
               code+='0';
               curr = curr.getRight();
            }
            else if(split[1].contains(c+"")){
               code+='1';
               curr = curr.getLeft();
            }
            else{          //In case the character is not in the tree *shouldn't happen*
               System.out.println("Character not in tree encode: " + c);
               System.exit(0);
            }
         }
      }
      return code;
   }

   //Returns a byte[] consisting of [nBits, bits]
   private String charCode(char c){
      huffmanTree curr = root;
      String code = "";
      String[] split;
      while(!curr.getData().equals(c+"")){
         split = curr.getData().split("\\*");
         if(split[0].contains(c+"")){
            code+='0';
            curr = curr.getRight();
         }
         else if(split[1].contains(c+"")){
            code+='1';
            curr = curr.getLeft();
         }
         else{          //In case the character is not in the tree *shouldn't happen*
            System.out.println("Character not in tree charCode: " + c);
            System.exit(0);
         }
      }
      return code;
   }
}
