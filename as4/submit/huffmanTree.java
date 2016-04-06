/*
   Author: Ethan Hamman
   StudentID: 10125341
   Tree Node
*/

public class huffmanTree{

   private String data;
   private int freq;
   private huffmanTree right;
   private huffmanTree left;


   public huffmanTree(String data, int freq){
      this.data = data;
      this.freq = freq;
      right = null;
      left = null;
   }

   public String getData(){
      return data;
   }

   public int getFreq(){
      return freq;
   }

   public huffmanTree getRight(){
      return right;
   }

   public huffmanTree getLeft(){
      return left;
   }

   public void setRight(huffmanTree right){
      this.right = right;
   }

   public void setLeft(huffmanTree left){
      this.left = left;
   }


}
