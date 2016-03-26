/*
   Author: Ethan Hamman
   StudentID: 10125341
   Simple object for a character and it's probability
*/

public class probMap{

   private char c;
   private int count;
   private probMap next;
   private probMap prev;

   public probMap(char c){
      this.c = c;
      count = 0;
      next = null;
      prev = null;
   }

   public probMap(char c, probMap next, probMap prev){
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

   public void setNext(probMap next){
      this.next = next;
   }

   public probMap getNext(){
      return next;
   }

   public void setPrev(probMap prev){
      this.prev = prev;
   }

   public probMap getPrev(){
      return prev;
   }

}
