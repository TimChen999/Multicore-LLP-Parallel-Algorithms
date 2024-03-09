//LLP for stable matching, each thread is a different man
public class StableMarriage extends LLP {

    // mprefs[i][k] is man i's kth choice
    // wprefs[i][k] is woman i's kth choice
    int[][] mprefs;
    int[][] wprefs;
    int[] G; //Current matching on pref (index = man, value at index = man's woman ranking from man's pref list)
    int[] matching; //What wife each man is matched to (convert from rankings in G)
    public StableMarriage(int[][] mprefs, int[][] wprefs) {
        super(mprefs.length);
        this.mprefs = mprefs;
        this.wprefs = wprefs;
        //For init, everyone proposes to their first choice G[j] = 0
        G = new int[wprefs.length];
        matching = new int[wprefs.length];
    }

    //Forbidden state, advance thread to next element
    @Override
    public boolean forbidden(int j) {
        //If there exists "i" another man in G. The other man prefers "j"'s partner to their own, "j"'s partner also prefers "i" to "j"
        //For each i
        for(int i = 0; i < mprefs.length; i++) {
            //Look at "j"'s partner in "i"'s list, look at "i" in "j"'s partner's list

            if(i != j) {
                //Who is "j"'s partner (G[j] measures what rank j's partner is)
                int jW = mprefs[j][G[j]];

                //Does "i" like "jW"?
                int iRanksjW = mprefs[i][jW];
                int iRanksiW = G[i];

                //Does "jW" like "i"?
                int jWRanksi = wprefs[jW][i];
                int jWRanksj = wprefs[jW][j];

                //Print debug
                //System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + " Man " + j + " Wife " + jW + " iRanksjW " + iRanksjW + " iRanksiW " + iRanksiW + " jWRanksi " + jWRanksi + " jWRanksj " + jWRanksj);

                //If "i" likes "jW" over "iW" and "jW" likes "i" over "j"
                if (iRanksjW <= iRanksiW && jWRanksi <= jWRanksj) {
                    return true;
                }
            }
        }
        return false;
    }

    //Advance means you go up, here it means to go to the next woman
    @Override
    public void advance(int j) {
        //If this is true, move "j" to the next position
        G[j] = G[j] + 1;
    }

    // This method will be called after solve()
    public int[] getSolution() {
        //Debug print
        //System.out.print("G [");
        for(int i = 0; i < G.length; i++){
            //Debug print
            //System.out.print(G[i] + " ");
            matching[i] = mprefs[i][G[i]];
        }
        //Debug print
        //System.out.println("]");
        // Return the partner for each man
        return matching;
    }
}

//For every man, advance will be called at most "n" times (there are only "n" partners to go through)
//Each time, forbidden is called, a man has to check through all other men O(n) time
//If for a thread, forbidden is not called on one level, the next level might result in a forbidden call if someone else has to advance
//At one level, the minimum of advance calls required to have a new level is 1 (all threads except one return forbidden = false)
//If an advance is called, all other threads have to recheck forbidden, matches might have been destabilized by the advance
//In the worst case, there are n^2 advances, which all happen serially (each advance is on a new level). In this case,
//there are n^2 levels, each requiring a forbidden check of time n
//Worst case runtime is O(n^3)
//According to the textbook, runtime is critical path length

