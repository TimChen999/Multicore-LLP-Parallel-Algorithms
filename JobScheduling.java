//A list of jobs to schedule, every job should have its own thread
//Initial state: schedule all jobs at t = 0
public class JobScheduling extends LLP {
    int[] time; //Time of each job
    int[][] prerequisites; //Prerequisites of each job
    int[] startingTime; //Starting time of each job

    // time[i] is the amount of time job i takes to complete
    // prerequisites[i] is a list of jobs that job i depends on
    public JobScheduling(int[] time, int[][] prerequisites) {
        super(time.length);
        this.time = time;
        this.prerequisites = prerequisites;
        this.startingTime = new int[time.length];
    }

    //Forbidden if pre-requisite jobs are not completed by start time of current job
    //Can jobs run in parallel? Assume they can if not dependent on each other
    @Override
    public boolean forbidden(int j) {
        for(int i = 0; i < time.length; i++){
            if(i != j){
                //is "i" prerequisite of "j"?
                boolean require = false;
                for(int p = 0; p < prerequisites[j].length; p++){
                    if(prerequisites[j][p] == i){
                        require = true;
                    }
                }

                //if "i" is a prerequisite, does "j" start after "i" finish?
                if(require){
                    int iFinish = startingTime[i] + time[i];
                    if(iFinish > startingTime[j]){
                        //Print debug
                        //System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + " Scheduling conflict: Job " + j + "(start: " + startingTime[j] + " end: " + (startingTime[j] + time[j]) + ")" + " conflicts with job " + i + "(start: " + startingTime[i] + " end: " + (startingTime[i] + time[j]) + ")");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void advance(int j) {
        //startingTime[j] = startingTime[j] + 1;

        //Start the job after its pre-req finishes
        for(int i = 0; i < time.length; i++){
            if(i != j){
                //is "i" prerequisite of "j"?
                boolean require = false;
                for(int p = 0; p < prerequisites[j].length; p++){
                    if(prerequisites[j][p] == i){
                        require = true;
                    }
                }

                //if "i" is a prerequisite, does "j" start after "i" finish, this step should advance j
                if(require){
                    int iFinish = startingTime[i] + time[i];
                    if(iFinish > startingTime[j]){
                        startingTime[j] = iFinish;
                    }
                }
            }
        }
    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Return the completion time for each job, convert from starting to finish time
        //System.out.print("{");
        int[] finalTime = startingTime;
        for(int i = 0; i < startingTime.length; i++){
            finalTime[i] = startingTime[i] + time[i];
            //System.out.print(finalTime[i] + " ");
        }
        //System.out.println("}");
        return finalTime;
    }
}

//For each job, it should have at most "n-1" pre-reqs (if all other jobs are required for it)
//Each forbidden/advance can take up to O(n) runtime, a job loops through all other jobs in pre-req and advance
//For each level, forbidden can happen multiple times, if a pre-req job also advances, forbidden might be called again
//since "n" threads are running, a thread, at worst, calls forbidden "n" times on each level
//Therefore, each level can have up to O(n^2) runtime
//If a job has a linear chain of dependent jobs, it can be required to run up to "n" levels
//Therefore overall complexity is O(n^3)
