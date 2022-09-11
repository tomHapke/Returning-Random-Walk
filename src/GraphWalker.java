import java.util.Random;

public class GraphWalker {

    /**
     * This class contains useful methods to perform random walks on a graph
     */
    private Graph g;

    public GraphWalker(Graph g) {
        this.g = g;
    }

    /**
     * This method returns the length of a random walk performed on graph g, starting at node start,
     * terminating when start is reached again
     */
    public int randomWalkLength(int start){
        int current=start;
        Random random= new Random();
        int steps=0;
        do{
            current=g.randomLocalDecision(current,random);
            steps++;
        }while (current!=start);
        return steps;
    }

    /**
     * This method returns true if and only if the random Walk staring at 'start' terminates at 'start'
     * within less than (limt+1) steps
     */
    public boolean randomWalkLengthLimited(int start, int limit){
        int current=start;
        Random random= new Random();

        for(int steps=0;steps<limit;steps++){
            current=g.randomLocalDecision(current,random);
            if(current==start){
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns the average length of (numbOfWalks) random walks starting at 'start'
     */
    public double averageWalkLength(int start, int numbOfWalks){
        long sum=0L;
        for(int i=0;i<numbOfWalks;i++){
            sum+=randomWalkLength(start);
        }

        double e=((double) sum)/(double) numbOfWalks;

        System.out.println("Number of Walks: "+numbOfWalks);
        System.out.println("Average Length: "+ e);
        return e;
    }

    /**
     * This method returns the percentage of (numbOfWalks) random walks starting at 'start' that terminated
     * within less than (limt+1) steps
     */
    public double averageWalkLimited(int start, long numbOfWalks, int limit){
        long t1=System.currentTimeMillis();
        long sum=0;
        int percentage=1;
        for(long i=0;i<numbOfWalks;i++){
            if(percentage*((double)numbOfWalks)/100<i){
                System.out.println(percentage+"%");
                percentage++;
            }
            if(randomWalkLengthLimited(start,limit)){
                sum++;
            }
        }

        double e=((double) sum)/(double) numbOfWalks;

        System.out.println("Number of Walks: "+numbOfWalks);
        System.out.println("Step Limit: "+limit);
        System.out.println("Percentage of terminated Walks: "+ e);
        System.out.println("Duration: "+(System.currentTimeMillis()-t1)+"ms");
        return e;
    }
}
