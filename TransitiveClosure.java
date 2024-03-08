public class TransitiveClosure extends LLP {

    boolean[][] original;
    boolean[][] solution;

    // edges[i][j] is true if there is an edge from node i to node j
    public TransitiveClosure(boolean[][] edges) {
        super(edges.length);
        //Have one thread to check for reachable nodes from every node
        //Ex: one thread for nodes reachable from 0, 1 for node 1, and so on...
        //For every node, try to find path from a node to other nodes
        //Nodes are either direct (value true) or indirect (value false) check intermediate nodes (all reachable nodes that aren't current node)
        //Loop over all other nodes for direct/indirect path from current node, there might be new reachable paths found by each node at every iteration
        //If a direct path is found, nothing changes for this node (solution is already true). Other nodes don't get any new info either
        //If an indirect path is found, set the node in solution to true (advance), this provides new info to other nodes to find new potential indirect
        //paths as well in the next iteration (thru current node), since we know a new node is accessible from current, return forbidden = true
        //Repeat this until you reach a point where at a level, no new paths are found for any node

        //For a path with 2 intermediate nodes: lets say original node is j, intermediate nodes are k and l, and final node is x
        //Originally, x and l are not reachable

        //First method:
        //j tries to find path to x, look thru reachable nodes, none of the reachable nodes lead to x, not found (l is not reachable yet)
        //j tries to find path to l, look thru reachable nodes, finds k is reachable, then paths from k to l, new reachable node found
        //New level:
        //j tries to find path to x, look thru reachable nodes, finds l is reachable, then finds x thru l

        //Second method:
        //j tries to find path to x, look thru reachable nodes, none of the reachable nodes lead to x, not found (j does not know k has path to x)
        //k tries to find path to x, finds indirect path to x from direct path l
        //(j will also find path to l on this level, but j does not use l to find x)
        //New level:
        //j tries to find path to x, look thru reachable nodes, finds k, k has path to x, so j has path to x

        //For a path of any length, nodes on the path will be found one level at a time


        original = edges;
        solution = new boolean[original.length][original[0].length];

        //Copy original to solution
        for(int i = 0; i < original.length; i++){
            for(int j = 0; j < original.length; j++){
                solution[i][j] = original[i][j];
            }
        }
    }

    @Override
    public boolean forbidden(int j) {
        //Look through adjacent nodes
        for(int i = 0; i < solution[j].length; i++){
            //If node is false, try to find indirect path (indicates a new reachable node path is discovered)
            if(solution[j][i] == false && j != i){
                System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + "Node: " + j + " Tries to find path to: " + i);
                //Look for intermediate node k, reachable from j
                //If j can reach k, test if k can reach i, if true, j can reach i
                for(int k = 0; k < solution[j].length; k++){
                    if(solution[j][k] == true && j != k){ //k is intermediate node, can k reach i?
                        System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + "Node: " + j + " Tries to find path to: " + i + " Thru Node " + k);
                        if(solution[k][i] == true){
                            System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + "Node: " + j + " FOUND PATH: " + i + " Thru Node " + k);
                            //Usually advance would be used to set new values, but since this path already found in the forbidden step, might as well set it here to save on runtime
                            solution[j][i] = true;
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void advance(int j) {

    }

    // This method will be called after solve()
    public boolean[][] getSolution() {
        // Return the transitive closure of the graph
        return solution;
    }
}
