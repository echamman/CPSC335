/*
   Author: Ethan Hamman
   StudentID: 10125341
   This class controls insertion and iterating through the tree
*/

public class trieController{

   private trieNode root;        //Topmost Rightmost Node in the trie

   public trieController(){
      root = null;
   }

   public void insert(String word){
      trieNode head = null;        //Used to remember the node at the beginning of a word, depth 1

      if(root==null){
         root = new trieNode(word.charAt(0), 0);
         head = root;
      }
      else{
         char firstLetter = word.charAt(0);
         trieNode spot = root;
         while(spot.getData() < firstLetter && spot.getRight() != null)
            spot = spot.getRight();

         //Determines whether new node needs to be inserted, and where
         if(spot.getData()==firstLetter)
            head = spot;
         else if(spot.getData() > firstLetter){
            //Insert new node into list
            head = new trieNode(firstLetter, 0);
            head.insertLeft(spot.getLeft());
            spot.insertLeft(head);
            head.insertRight(spot);
            //If head is rightmost, it is root
            if(head.getLeft() == null)
               root = head;
         }
         else{
            //Insert new node into end of list
            head = new trieNode(firstLetter, 0);
            spot.insertRight(head);
            head.insertLeft(spot);
         }

      }

      insertWord(word, head);

   }

   /*
      Inserts the word starting at the head node, which will have already been found or newly inserted
   */
   private void insertWord(String word, trieNode head){

      trieNode spot = head;
      trieNode parent = null;             //Used to maintain a parent, in the case of the ordered list of children has a new 'head'
      int depth = 1;

      while(depth < word.length()){

         if(spot.getChild()==null){                   //If there is no child, but still more word to add, loop through the rest of the word
            for(int i=1; i<word.length(); i++){
               spot.insertChild(new trieNode(word.charAt(depth), depth));
               spot = spot.getChild();
               depth++;
            }
         }
         else{
            spot = insertChar(word.charAt(depth), spot, depth);
            depth++;
         }
      }

      insertChar('$', spot, depth);             //Insert '$' which symbolizes the end of a word

   }

   /*
      Used to insert a character into the list of children under this parent node
   */
   private trieNode insertChar(char data, trieNode parent, int depth){

      trieNode spot = parent.getChild();
      trieNode sibling = null;

      while(spot.getData() < data && spot.getRight() != null)
         spot = spot.getRight();

      //Determines whether new node needs to be inserted, and where
      if(spot.getData()==data)
         sibling = spot;
      else if(spot.getData() > data){
         //Insert new node into list
         sibling = new trieNode(data, depth);
         sibling.insertLeft(spot.getLeft());
         sibling.insertLeft(sibling);
         sibling.insertRight(spot);
         //If head is rightmost, it is root
         if(sibling.getLeft() == null)
            parent.insertChild(sibling);
      }
      else{
         //Insert new node into end of list
         sibling = new trieNode(data, depth);
         spot.insertRight(sibling);
         sibling.insertLeft(spot);
      }

      return sibling;
   }
}
