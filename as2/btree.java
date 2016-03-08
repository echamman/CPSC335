

public class btree{

   private int[] values;
   private btree[] links;
   private int depth;
   private int numKeys;
   private int numNodes;

   public btree(){
      values = new int[5];                //An extra slot in values and links, for when a node overflows
      links = new btree[6];
      depth = 0;
      numKeys = 0;
      numNodes = 0;
   }

   //Insert will try to insert a number into a leaf, and return an int status code for overflows
   public int insert(int num){
      //Since node is already determined to be a leaf, insert easily
      for(int i=3; i>=0; i--){
         if(values[i]>num)
            values[i+1] = values[i];
         else if(values[i]<num && values[i] !=0){
            values[i+1] = num;
            break;
         }
         if(i==0)
            values[i] = num;
      }
      //If the node is full, return error code
      if(isFull())
         return -1;
      else
         return 0;

   }

   //Insert a new key into a node that has nodes AND links, used for when a child overflows, returns error code for overflows
   public int insertIntoNode(int num, btree left, btree right){
      int i;
      //insert value, move everything greater than it forward one spot
      for(i=3; i>=0; i--){
         if(values[i]>num)
            values[i+1] = values[i];
         else if(values[i]<num && values[i] !=0){
            values[i+1] = num;
            break;
         }
         if(i==0 && values[i]>num){
            values[i] = num;
            i=-1;
            break;
         }
      }
      i++;
      //insert new links to each side of split node
      for(int j=4; j>i;j--){
         links[j+1] = links[j];
      }
      links[i] = left;
      links[i+1] = right;

      //If the node is full, return error code
      if(isFull())
         return -1;
      else
         return 0;
   }

   //Finds the tree to insert new number into
   public btree findTree(int num){
      int i = 0;
      for(int val : values){
         if(val<num && val != 0)
            i++;
      }

      if(links[i].isLeaf()){
         return links[i];
      }
      else
         return links[i].findTree(num);


   }

   //Returns true if the value is in the tree
   public boolean search(int num){
      int i = 0;
      for(int val : values){
         if(val<num && val != 0)
            i++;
      }

      if(num == values[i])
         return true;
      else if(!isLeaf())
         return links[i].search(num);          //Search the tree it was pointing to
      else
         return false;

   }

   //Recursively get depth
   public int getDepth(){
      if(isLeaf())
         return 1;
      else{
         int c=0;
         int maxDepth = 0;
         while(links[c]!=null){
            if(links[c].getDepth()>maxDepth)
               maxDepth=links[c].getDepth();
            c++;
         }
         return 1+maxDepth;
      }
   }

   //Recursively call on each node to get number of keys of it and all below it
   public int getNumKeys(){
      int c=0;
      while(values[c]!=0 && c<4)
         c++;

      if(!isLeaf()){
         int i=0;
         while(links[i]!=null && i<5){
            c += links[i].getNumKeys();
            i++;
         }
      }
      return numKeys = c;
   }

   //Recursively call to get number of nodes of it and all below it
   public int getNumNodes(){
      int c=0;
      numNodes = 1;
      if(!isLeaf()){
         while(links[c]!=null){
            numNodes += links[c].getNumNodes();
            c++;
         }
      }
      return numNodes;
   }

   //Finds the parent of a given child node
   public btree getParent(btree child, int num){
      for(btree link : links){
         if(child == link){
            return this;
         }
      }
      int i;
      for(i=0; values[i] < num && i<5 && values[i]!=0;i++);        //Find value larger than number given
      return links[i].getParent(child, num);

   }

   //Getters and setters for values and links
   public int[] getVals(){
      return values;
   }

   public void setVals(int[] vals){
      values = vals;
   }

   public btree[] getLinks(){
      return links;
   }

   public void setLinks(btree[] links){
      this.links = links;
   }

   //Checks if last position has a value
   public boolean isFull(){
      return(values[4]!=0);
   }

   //If there is no first link the tree is a leaf
   public boolean isLeaf(){
      return(links[0]==null);
   }
}
