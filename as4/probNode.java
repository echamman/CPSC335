/*
   Author: Ethan Hamman
   StudentID: 10125341
   Simple object for a character and it's probability, used only for inputting words
*/

public class probNode{

   private char c;
   private int count;
   private probNode next;
   private probNode prev;

   public probNode(char c){
      this.c = c;
      count = 0;
      next = null;
      prev = null;
   }

   public probNode(char c, probNode next, probNode prev){
      this.c = c;
      count = 0;
      this.next = next;
      this.prev = prev;
   }

   public void inc(){
      count++;
   }

   public char getChar(){
      return c;
   }

   public float getProb(int total){
      return ((float)count)/((float)total);
   }

   public int getCount(){
      return count;
   }

   public void setNext(probNode next){
      this.next = next;
   }

   public probNode getNext(){
      return next;
   }

   public void setPrev(probNode prev){
      this.prev = prev;
   }

   public probNode getPrev(){
      return prev;
   }

}
