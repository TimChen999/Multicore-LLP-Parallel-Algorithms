import java.util.Arrays;

public class ParallelPrefix extends LLP {

    // A is an input array that we want to compute the prefix scan for
    // S is the pre-computed summation tree (reduction), computed using LLP-Reduce
    int n;
    int[] A;
    int[] S;
    int[] G;
    public ParallelPrefix(int[] A, int[] S) {
        //Make array 1 indexed, converting to 0 index is too much work :(
        //For all arrays, indexes are now from 1 to (end value). index 0 is meaningless
        //Threads from ID 0 to 2n-1, G is an array from 1 to 2n-1, so thread 0 does nothing
        super(A.length * 2);
        n = A.length;

        //System.out.println("n " + n);

        //A
        this.A = new int[A.length + 1];
        for(int i = 0; i < A.length; i++){
            this.A[i+1] = A[i];
        }

        //S
        this.S = new int[S.length + 1];
        for(int i = 0; i < S.length; i++){
            this.S[i+1] = S[i];
        }

        //G
        this.G = new int[2 * n];
        for(int i = 1; i < G.length; i++){
            G[i] = Integer.MIN_VALUE;
        }
    }

    @Override
    public boolean forbidden(int j) {
        //1 indexed algorithm
        //1) Ensure G[j] >= 0 if j = 1
        //2) Ensure G[j] >= G[j/2] if j is even
        //3) Ensure G[j] >= S[j-1] + G[j/2] if j is odd and j < n
        //4) Ensure G[j] >= A[j-n] + G[j/2] if j is odd and j > n

        //Don't do anything with index 0:
        if(j == 0){return false;}

        //Ensure with rest of array (1 indexed)
        if(j == 1 && !(G[j] >= 0)){ //First element G[1] is at least 0
            return true;
        }
        else if(j % 2 == 0 && !(G[j] >= G[j/2])){ //Even elements G[j] are at least G[j/2]
            return true;
        }
        else if(j % 2 == 1 && j < n && !(G[j] >= S[j-1] + G[j/2])){ //Odd elements j < n, G[j] at least S[j-1] + G[j/2]
            return true;
        }
        else if(j % 2 == 1 && j > n && !(G[j] >= A[j-n] + G[j/2])){ //Odd elements j > n, G[j] at least A[j-n] + G[j/2]
            return true;
        }
        return false;
    }

    @Override
    public void advance(int j) {
        if(j == 0){return;}

        //Ensure with rest of array
        if(j == 1){
            G[j] = 0;
        }
        else if(j % 2 == 0){
            G[j] = G[j/2];
        }
        else if(j % 2 == 1 && j < n){
            G[j] = S[j-1] + G[j/2];
        }
        else if(j % 2 == 1 && j > n){
            G[j] = A[j-n] + G[j/2];
        }
        int a = 0;
    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Return only the prefix scan part of the state vector
        // i.e. return the last n elements

        //Print debug
        //System.out.print("Reduction array: {"); for(int i = 1; i < G.length; i++){System.out.print(G[i] + " ");} System.out.println("}");

        //Return last half of G (for indexes 0-15 (Length = 16), return indexes 8-15)
        return Arrays.copyOfRange(G, G.length/2, G.length); //Start is inclusive, end is exclusive
    }
}
