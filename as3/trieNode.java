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

   public void trieNode(char data){
      left = null;
      right = null;
      child = null;
      this.data = data;
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

   public trie getRight(){
      return right;
   }

   public trie getLeft(){
      return left;
   }

   public trie getChild(){
      return child;
   }

}
