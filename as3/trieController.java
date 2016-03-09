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

   /*
   -------------------------------------------------------------------------------------------------------
   INSERTION
   -------------------------------------------------------------------------------------------------------
   */
   //Inserts a word into the trie
   public void insert(String word){
      trieNode head = null;        //Used to remember the node at the beginning of a word, depth 0

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
      int depth = 1;

      while(depth < word.length()){

         if(spot.getChild()==null){
            spot.insertChild(new trieNode(word.charAt(depth), depth));
            spot = spot.getChild();
            depth++;
         }
         else{
            spot = insertChar(word.charAt(depth), spot, depth);
            depth++;
         }
      }

       //Insert '$' which symbolizes the end of a word
      if(spot.getChild()==null)
         spot.insertChild(new trieNode('$', depth));
      else
         insertChar('$', spot, depth);

   }

   /*
      Used to insert a character into the list of children under this parent node, only used when the child is NOT null
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
         spot.insertLeft(sibling);
         sibling.insertRight(spot);
         //If new node is rightmost, it is direct child of parent
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

   /*
   -------------------------------------------------------------------------------------------------------
   SPELLCHECKING
   -------------------------------------------------------------------------------------------------------
   */
   public void spellCheck(String word){

      String endWord = word+'$';        //Append the end word character
      trieNode spot = root;
      boolean found = false;

      for(int i=0; i<endWord.length();i++){
         while(spot.getData() < endWord.charAt(i) && spot.getRight()!=null)
            spot = spot.getRight();

         if(spot.getData()==endWord.charAt(i) && spot.getData()=='$'){
            found=true;
         }
         else if(spot.getData()==endWord.charAt(i))
            spot = spot.getChild();
         else
            break;
      }

      if(found==true)
         System.out.println(word + " is correctly spelled");
      else
         System.out.println(word + " is incorrectly spelled");
   }

}
