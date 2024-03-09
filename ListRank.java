import java.util.Arrays;

public class ListRank extends LLP {

    // parent[i] tells us the index of the parent of node i
    // the root r has parent[r] = -1
    int[] traversal; //What is the next node for current node to traverse to (goal is -1 for all nodes, start at original node, = parent)
    int[] distance; //How far each node has traveled
    int[] parent; //Parent of each node
    public ListRank(int[] parent) {
        super(parent.length);
        this.parent = parent;
        this.traversal = new int[parent.length];
        for(int i = 0; i < parent.length; i++){
            traversal[i] = parent[i];
        }
        distance = new int[parent.length];
    }

    //This algorithm finds how far each node is from the root, given node "i"
    //Each node has its own thread, at every level, if a node is not at root, return forbidden = true
    //Advanced will move the node up one step (towards parent) and increment distance by 1
    //Repeat until all nodes reach parent
    @Override
    public boolean forbidden(int j) {
        //Root node reached
        if(traversal[j] == -1){
            return false;
        }
        //Root node not reached
        return true;
    }

    @Override
    public void advance(int j) {
        //Advance j, move to the next node
        int nextNode = traversal[j];
        traversal[j] = parent[nextNode];

        //Increment distance
        distance[j] = distance[j] + 1;
    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Return the distance of every node to the root
        return distance;
    }

    //For a node, it will traverse upwards (advance) at most "n" times. Forbidden/advance are both O(1), so
    //every upwards traversal takes O(1) time. Overall runtime is O(n)
}
