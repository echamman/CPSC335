/*
   Controls insertion into the tree, as well as when a node needs to be split
*/

public class bController{

   private btree root;
   private int depth;
   private int numKeys;
   private int numNodes;


   public bController(){
      root = new btree();
      depth = 1;

   }

   public void insert(int num){
      btree toInsert;
      int err = 0;

      //Checks if the number has already been inserted
      if(root.search(num)){
         System.out.println(num + " is already in this tree.");
         return;
      }

      if(root.isLeaf()){
         err = root.insert(num);
         //Split root if error
         if(err == -1){
            splitRoot();
         }
      }
      else{
         toInsert = root.findTree(num);
         err = toInsert.insert(num);
         //split leaf if error
         if(err==-1){
            splitNode(toInsert, num);
         }
      }
   }

   public void reportStats(){
      depth = root.getDepth();
      numKeys = root.getNumKeys();
      numNodes = root.getNumNodes();

      System.out.println("Depth: " + depth + "\nNumber of Keys: " + numKeys + "\nNumber of Nodes: " + numNodes);
   }

   //If the root overflows, break into two, make new root, re-organize values and links
   private void splitRoot(){
      int[] tempVals = root.getVals();
      btree[] tempLinks = root.getLinks();

      btree left = new btree();
      btree right = new btree();

      //Manually set values, would have to be made into function if 'order' is variable
      left.setVals(new int[] {tempVals[0], tempVals[1], 0, 0, 0});
      right.setVals(new int[] {tempVals[3],tempVals[4], 0, 0, 0});
      left.setLinks(new btree[] {tempLinks[0], tempLinks[1], tempLinks[2], null, null, null});
      right.setLinks(new btree[] {tempLinks[3], tempLinks[4], tempLinks[5], null, null, null});

      root.setVals(new int[] {tempVals[2],0,0,0,0});
      root.setLinks(new btree[] {left, right, null, null, null, null});
   }

   //If Node overflows, split into two, re-organize values, add new mid value and two links to parent
   private void splitNode(btree toSplit, int num){
      btree parent = root.getParent(toSplit, num);
      int[] tempVals = toSplit.getVals();
      btree[] tempLinks = toSplit.getLinks();

      btree left = new btree();
      btree right = new btree();

      //Manually set values, would have to be made into function if 'order' is variable
      left.setVals(new int[] {tempVals[0], tempVals[1], 0, 0, 0});
      right.setVals(new int[] {tempVals[3],tempVals[4], 0, 0, 0});
      left.setLinks(new btree[] {tempLinks[0], tempLinks[1], tempLinks[2], null, null, null});
      right.setLinks(new btree[] {tempLinks[3], tempLinks[4], tempLinks[5], null, null, null});

      //Insert new mid node and links to parent, if parent overflows, recursively call on parent
      int err = parent.insertIntoNode(tempVals[2], left, right);

      if(err==-1){
         if(parent == root){
            splitRoot();
         }
         else{
            splitNode(parent, tempVals[2]);
         }
      }

   }

}
