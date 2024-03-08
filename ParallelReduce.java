public class ParallelReduce extends LLP {

    // A is an input array that we want to compute the reduction for
    int[] A;
    int[] G;
    public ParallelReduce(int[] A) {
        super(A.length - 1);
        this.A = A;
        G = new int[A.length - 1];
        for(int i = 0; i < G.length; i++){
            G[i] = Integer.MIN_VALUE;
        }
    }

    @Override
    public boolean forbidden(int j) {
        //Ensure: for 1 <= j < n/2: G[j] >= G[2j] + G[2j + 1], for n/2 <= j < n: G[j] >= A[2j - n + 1] + A[2j - n + 2]
        //Adjust for 0 indexed arrays
        //If this is not met, return forbidden and set G[j] to meet those conditions

        //Left part
        if(j < (A.length-1)/2){
            if(G[j] < G[2*j + 1] + G[2*j + 2]){
                return true;
            }
        }
        //Right part
        else{
            if(G[j] < A[2*(j - (A.length-1) / 2)] + A[2*(j - (A.length-1) / 2) + 1]){
                return true;
            }
        }
        return false;
    }

    @Override
    public void advance(int j) {
        if(j < (A.length-1)/2){
            G[j] = G[2*j + 1] + G[2*j + 2];
        }
        else{
            G[j] = A[2*(j - (A.length-1) / 2)] + A[2*(j - (A.length-1) / 2) + 1];
        }
    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Trim the state vector to only the reduce elements
        // Your result should have n-1 elements

        //Debug print
        //System.out.print("{");
        for(int i = 0; i < G.length; i++){
            //System.out.print(G[i] + " ");
        }
        //System.out.println("}");
        return G;
    }
}
