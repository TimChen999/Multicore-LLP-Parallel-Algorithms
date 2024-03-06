public class TransitiveClosure extends LLP {

    // edges[i][j] is true if there is an edge from node i to node j
    public TransitiveClosure(boolean[][] edges) {
        super(edges.length);
    }

    @Override
    public boolean forbidden(int j) {
        return false;
    }

    @Override
    public void advance(int j) {

    }

    // This method will be called after solve()
    public boolean[][] getSolution() {
        // Return the transitive closure of the graph
        return new boolean[][]{};
    }
}
