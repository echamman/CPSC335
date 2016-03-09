/*
   Author: Ethan Hamman
   StudentID: 10125341
   Structure for the Trie
   I am following a tree I found on Wikipedia, it goes as follows:
   Each node has siblings and children, the siblings represent letters that can substitute each other, and children represent letters that
   come after the parent
   https://en.wikipedia.org/wiki/Trie#/media/File:Pointer_implementation_of_a_trie.svg
*/

public class trieNode{

   private trieNode left;
   private trieNode right;
   private trieNode child;
   private char data;
   private int depth;

   public trieNode(char data, int depth){
      left = null;
      right = null;
      child = null;
      this.data = data;
      this.depth = depth;
   }

   public void insertRight(trieNode rightNew){
      right = rightNew;
   }

   public void insertLeft(trieNode leftNew){
      left = leftNew;
   }

   public void insertChild(trieNode childNew){
      child = childNew;
   }

   public trieNode getRight(){
      return right;
   }

   public trieNode getLeft(){
      return left;
   }

   public trieNode getChild(){
      return child;
   }

   public char getData(){
      return data;
   }

   public int getDepth(){
      return depth;
   }

}
