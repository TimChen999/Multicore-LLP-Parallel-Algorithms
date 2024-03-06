public class ParallelPrefix extends LLP {

    // A is an input array that we want to compute the prefix scan for
    // S is the pre-computed summation tree (reduction), computed using LLP-Reduce
    public ParallelPrefix(int[] A, int[] S) {
        super(A.length);
    }

    @Override
    public boolean forbidden(int j) {
        return false;
    }

    @Override
    public void advance(int j) {

    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Return only the prefix scan part of the state vector
        // i.e. return the last n elements
        return new int[]{};
    }
}
