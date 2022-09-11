import org.junit.jupiter.api.Test;

import javax.swing.text.Style;
import java.io.IOException;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.fail;

public class Calc {

    /**
     * This script creates a small and simple graph and performs the averageWalkLength method on it.
     * The graph is printed as a png-file as well as a dot-file.
     * Furthermore, the adjacency matrix of the graph is stored in a txt file called 'simpleGraph.txt'.
     */
    @Test
    void simpleWalk() throws IOException {
        Graph g=new Graph();
        g.insertNodes(4);
        double p=0.333333333333333333;
        g.addEdge(0,1,p);
        g.addEdge(0,2,p);
        g.addEdge(0,3,p);

        g.addEdge(1,2,p);
        g.addEdge(2,3,p);
        g.addEdge(3,1,p);

        Matrix ad= g.getAdjacencyMatrix();
        ad.writeMatrix("simpleGraph_AjdM.txt");
        GraphWalker gw= new GraphWalker(g);
        gw.averageWalkLength(0,1000);
        g.printGraph("simpleGraph");
        System.out.println("----------------------------");
        System.out.println("Open 'simpleGraph.png' to have a look at the graph.");
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This script computes the expected value of the length of a returning walk on the football by
     * iterating until the k-th term in the expected value summation
     * The result of this series can also be computed explicitly as shown in README.md. According to this computation,
     * the exact expected value is 20 , which the series clearly converges to as shown.
     */
    @Test
    void expectedValueOfWalkLengthOnFootball() throws IOException {
        int k=10000;  //maxTerm
        Function<Integer,Double> expectedValueSequence = index ->
        {
            if(index==0){
                return 0.0;
            }
            else{
                return (double)index+1;
            }
        };

        Matrix A= Matrix.readMatrix("football_TransitionMatrix.txt");
        MatPowerSeries s= new MatPowerSeries(expectedValueSequence);
        Matrix seriesMat =s.computeSeries(k,A);

        //Extracting the expected value out of the series by mutiplying
        // seriesMat with the "1th-step-probability-vector" (0, 1/3 , 1/3 , 1/3 , 0 , ... , 0)
        // and selecting the first entry
        double expValue= (seriesMat.getA(0,1)/3+seriesMat.getA(0,2)/3+seriesMat.getA(0,3)/3);
        System.out.println("Expected Value: "+expValue);
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This script returns the average walk length on the football-graph terminating when returned to start.
     * This supports the theoretically computed average length being exactly 20 steps.
     */
    @Test
    void randomWalkOnFootball() throws InterruptedException {
        int numbOfWalks=1_000_000;
        Graph g= Graph.buildOfAdjacencyMatrix("football_AdjM.txt");
        GraphWalker gw= new GraphWalker(g);
        gw.averageWalkLength(0,numbOfWalks);
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This script writes the regular-hexagonal-floor-tiling-graph with a finite depth in adjacency matrix form
     * into a txt-file (format explained in README.md) and prints the graph as a png file aswell as a dot-file
     */
    @Test
    void buildFloorTilingGraph() throws IOException {
        int depth= 10;
        Graph g= Graph.buildHexaGrid(depth);
        Matrix m = g.getAdjacencyMatrix();
        m.writeMatrix("floor_tiling_AdjM_depth_"+depth+".txt");
        g.printGraph("floor_tiling_Graph_depth_"+depth);
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This script returns the exact probability of a random returning walk
     * on the floor-tiling-graph to be longer than 20 steps.
     * This number is the answer to the puzzle.
     */
    @Test
    void randomWalkExactProbabilities() throws IOException {
        int depth=10;
        int power=19;

        Graph g= Graph.buildHexaGrid(depth);
        Matrix m=g.getAdjacencyMatrix();
        m.setA(0,0,1.0);
        m.setA(3,0,0.0);
        m.setA(7,0,0.0);
        m.setA(11,0,0.0);

        Matrix.setCache(m.getN());

        m.powOR(power);

        //mutiplying m with the "1th-step-probability-vector" (0, 1/3 , 1/3 , 1/3 , 0 , ... , 0)
        //and extracting the first entry of the result vector
        double p;
        p=m.getA(0,3)+m.getA(0,7)+m.getA(0,11);
        p=p/3;

        //the resulting probability is the exact counter event, therefore, set p=1-p
        p=1-p;

        System.out.println("Depth of the floor-tiling-graph: "+depth);
        System.out.println("Probability of random returning walk on the floor-tiling-graph to be longer than 20 steps: ");
        System.out.println(p);
    }


    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This script returns the percentage of random walks on the floor-tiling-graph terminating within
     * less than 21 steps.
     * In fact, its counter-probability should converge against the probability computed in
     * randomWalkExactProbabilities() , but getting a result with a precision of 7 digits requires
     * an unreasonable amount of time as one can convince himself by trying out
     */
    @Test
    void randomFiniteWalkOnFinitePlanarFootball(){
        long numbOfWalks=10_000_000L;
        Graph g= Graph.buildHexaGrid(10);
        GraphWalker gw= new GraphWalker(g);
        gw.averageWalkLimited(0,numbOfWalks,20);
    }

    /**
     * Feel free to compute something
     */
    @Test
    void clac(){
        System.out.println("Computation goes brr....");
        Graph g = Graph.buildOfAdjacencyMatrix("football_AdjM.txt");
        g.printGraph("football_graph");
    }
}
