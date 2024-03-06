//A list of jobs to schedule, every job should have its own thread
//Initial state: schedule all jobs at t = 0
public class JobScheduling extends LLP {
    int[] time; //Time of each job
    int[][] prerequisites; //Prerequisites of each job
    int[] solution; //Starting time of each job

    // time[i] is the amount of time job i takes to complete
    // prerequisites[i] is a list of jobs that job i depends on
    public JobScheduling(int[] time, int[][] prerequisites) {
        super(time.length);
        this.time = time;
        this.prerequisites = prerequisites;
        this.solution = new int[time.length];
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
                    int iFinish = solution[i] + time[i];
                    if(iFinish > solution[j]){
                        System.out.println("Running task " + j + " in thread " + Thread.currentThread().getName() + " Scheduling conflict: Job " + j + "(start: " + solution[j] + " end: " + (solution[j] + time[j]) + ")" + " conflicts with job " + i + "(start: " + solution[i] + " end: " + (solution[i] + time[j]) + ")");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void advance(int j) {
        solution[j] = solution[j] + 1;
    }

    // This method will be called after solve()
    public int[] getSolution() {
        // Return the completion time for each job, convert from starting to finish time
        System.out.print("{");
        for(int i = 0; i < solution.length; i++){
            solution[i] = solution[i] + time[i];
            System.out.print(solution[i] + " ");
        }
        System.out.println("}");
        return solution;
    }
}