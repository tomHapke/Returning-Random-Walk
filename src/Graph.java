import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

public class Graph {

    public static class Edge{
        private int to;
        private double weight;


        public Edge(int to, double weight) {
            this.to = to;
            this.weight = weight;
        }

        public int getTo() {
            return to;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight){
            this.weight=weight;
        }
    }

    /**
     * Nodes are represented by ints.
     */
    private HashMap<Integer, HashMap<Integer,Edge>> nodes;
    private int counter;

    public Graph() {
        this.nodes = new HashMap<Integer, HashMap<Integer, Edge>>();
        this.counter=0;
    }

    /**
     * Creates an instance of Graph by initializing a adjacency matrix read out of the passed file.
     * See also Matrix.readMatrix(String filename)
     */
    public static Graph buildOfAdjacencyMatrix(String filename){
        Matrix m =Matrix.readMatrix(filename);
        Graph g= new Graph();
        g.insertNodes(m.getN());
        for(int i=0;i<m.getN();i++){
            for(int j=i;j<m.getN();j++){
                if(Math.abs(m.getA(i,j))> 2 * Double.MIN_VALUE){
                    g.addEdge(i,j,m.getA(i,j));
                }
            }
        }
        return g;
    }

    /**
     * Inserts a new Node. A node itself does not store any information as it is represented by an integer value
     */
    public int insertNode(){
        nodes.put(counter,new HashMap<>());
        return counter++;
    }

    /**
     * Inserts a certain number of nodes
     */
    public void insertNodes(int numb){
        for(int i=0;i<numb;i++){
            this.insertNode();
        }
    }

    /**
     * Get all nodes of the graph
     */
    public HashMap<Integer, HashMap<Integer, Edge>> getNodes() {
        return nodes;
    }

    /**
     * Add a new edge with specified weight to the graph
     */
    public void addEdge(int from, int to, double weight){
        Edge forward= new Edge(to, weight);
        Edge backward= new Edge(from, weight);
        nodes.get(from).put(to,forward);
        nodes.get(to).put(from,backward);
    }

    /**
     * Some special getter-methods that will be used later on
     */
    public int getNumbOfNeighbours(int node){
        return nodes.get(node).keySet().size();
    }
    public int getSmallestNeighbour(int node){
        return nodes.get(node).keySet().stream().min((a,b)->a-b).get();
    }
    public int getBiggestNeighbour(int node){
        return nodes.get(node).keySet().stream().max((a,b)->a-b).get();
    }

    /**
     * Compute the adjacencyMatrix of the graph
     */
    public Matrix getAdjacencyMatrix(){
        Matrix ad= new Matrix(counter);
        for(int i=0;i<counter;i++){
            for(int j=0;j<counter;j++){
                if(nodes.get(i).containsKey(j)){
                    ad.setA(i,j,nodes.get(i).get(j).getWeight());
                }
            }
        }
        return ad;
    }

    /**
     * Returns a random neighbour edge of the node 'from'.
     * This method works by selecting a random double p between 0 and 1
     * and adds up weights (probabilities) of the neighbour edges as long as
     * the sum is smaller than p. When the sum reaches a value greater than p,
     * the edge referring to the last added weight is chosen.
     * This is equivalent to mapping all edges e to subintervalls of length e.getWeight()
     * in the intervall from 0 to 1 and choosing the edge where the random double p is contained.
     */
    public int randomLocalDecision(int from, Random random){
        double p = random.nextDouble();
        double pointer=0.0;
        HashMap<Integer,Edge>neighbours= nodes.get(from);
        for(Edge out : neighbours.values()){
            pointer+=out.weight;
            if(pointer>p){
                return out.to;
            }
        }
        System.err.println("Weights do not add up to 1");
        return 0;
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This method creates a graph representing the regular-hexagonal-floor-tiling-graph with a finite depth.
     * The first node represents the center and every other nodes are build around it by using a
     * breadth-first search approach
     */
    public static Graph buildHexaGrid(int depth){
        Graph g= new Graph();
        double p=0.3333333333333333333333333333333333333333333333333333;

        //first level
        g.insertNodes(13);

        for(int i=1;i<12;i++){
            g.addEdge(i,i+1,p);
        }
        g.addEdge(12,1,p);

        g.addEdge(0,3,p);
        g.addEdge(0,7,p);
        g.addEdge(0,11,p);

        int start=1;

        for(int i=1;i<depth;i++){
            start=Graph.buildNewHexaLevel(g,p,start);
        }

        Graph.addBorderNodes(g,p,start);
        return g;
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This method is used in the method Graph buildHexaGrid(int depth) to create a new depth-level.
     * In terms of the "breadth-first search-depth", every NewHexaLevel increases the "breadth-first search-depth"
     * by 2.
     */
    public static int buildNewHexaLevel(Graph g,double p, int start){
        int newStart=g.insertNode();

        //first hexagon

        int temp1=g.insertNode();
        g.addEdge(newStart,temp1,p);
        int temp2=temp1;
        temp1=g.insertNode();
        g.addEdge(temp2,temp1,p);
        int current=g.getSmallestNeighbour(start);


        int buildOn=temp1;
        int next;
        int nextNext;
        int numbOfNeighbours;

        while (true){
            next=g.getBiggestNeighbour(current);
            g.addEdge(buildOn,current,p);
            if(current>next){
                temp2=buildOn;
                for(int i=0;i<3;i++){
                    temp1=g.insertNode();
                    g.addEdge(temp2,temp1,p);
                    temp2=temp1;
                }

                g.addEdge(temp1,newStart,p);
                g.addEdge(temp1,start,p);

                return newStart;
            }

            numbOfNeighbours=g.getNumbOfNeighbours(next);

            if(numbOfNeighbours==3){
                nextNext=g.getBiggestNeighbour(next);
                if(next>nextNext){
                    temp2=buildOn;
                    for(int i=0;i<2;i++){
                        temp1=g.insertNode();
                        g.addEdge(temp2,temp1,p);
                        temp2=temp1;
                    }

                    g.addEdge(temp1,newStart,p);
                    g.addEdge(temp1,start,p);

                    return newStart;
                }
                current=nextNext;
                temp1=g.insertNode();
                g.addEdge(buildOn,temp1,p);
                temp2=temp1;
                temp1=g.insertNode();
                g.addEdge(temp2,temp1,p);
                buildOn=temp1;
            }
            else if(numbOfNeighbours==2){
                current=next;
                temp2=buildOn;
                for(int i=0;i<3;i++){
                    temp1=g.insertNode();
                    g.addEdge(temp2,temp1,p);
                    temp2=temp1;
                }
                buildOn=temp1;
            }
            else {
                System.err.println("Wrong build operation!");
                return 0;
            }

        }
    }

    /**
     * Andy's Morning Stroll
     * JULY 2022 : PUZZLE
     * This method is used in the method Graph buildHexaGrid(int depth) to create a border to the finite HexaGird Graph
     */
    public static void addBorderNodes(Graph g, double p, int start){
        int current=start;
        int borderNode;
        int numbOfNeighbours;
        int next;
        while (true){


            numbOfNeighbours=g.getNumbOfNeighbours(current);
            next=g.getBiggestNeighbour(current);
            if(current==start){
                next=g.getSmallestNeighbour(start);
            }
            if(numbOfNeighbours>3||numbOfNeighbours<2){
                System.err.println("Wrong build operation!");
                return;
            }
            if(numbOfNeighbours==2){
                borderNode=g.insertNode();
                g.addEdge(borderNode,current,p);
                g.nodes.get(borderNode).get(current).setWeight(1.0);
            }
            if(next<current){
                break;
            }
            current=next;
        }
    }

    /**
     * Uses the GraphPrinter Class to print as an png- and adot-file
     */
    public void printGraph(String filePrefix){
        GraphPrinter gp= new GraphPrinter(filePrefix);
        for(Integer node: this.getNodes().keySet()){
            for(Integer neighbour : this.getNodes().get(node).keySet()){
                gp.addln(node+" -> "+neighbour);
            }
        }
        gp.print();
    }

    public static void main(String[] args) throws IOException {
        Graph g= new Graph();
        g.insertNodes(5);
        g.addEdge(0,2,0.5);
        g.addEdge(0,1,0.2);
        Matrix m = g.getAdjacencyMatrix();
        m.writeMatrix("ad.txt");
    }

}
