# ReturningRandomWalk
Project for computations and simulations concerning returning-random-walks on graphs and transition matrices

This project contains:
- a **Graph** class to represent weighted graphs, initialize graphs from adjacency matrices and create adjacency matrices vise versa
- a **GraphWalker** class to simulate returning random walks
- a **GraphPrinter** class to print graphs as png- and dot-files
- a **Matrix** class to effectivly compute Matrix operations, store and load matrices
- a **MatPowerSeries** class to compute any matrix power series
- a **Calc** class with example scripts
- many more usefull methods

**Application:** These methods are used to **provide a solution to the Jane Street Puzzle "Andy's Morning Stroll" from July 2022** (https://www.janestreet.com/puzzles/andys-morning-stroll-index/), which I handed in successfully. The step-by-step calculations can be executed in the Calc class while important adjacency matrices and graphs are stored in the main folder.


**Format for storing adjacency matrice:**
- the first line just contains an integer that specifies the dimension of the square matrix
- rows beginn and end with "|"
- entries are separated by " , "


**Explanation of the puzzle's solution:**

We first need to compute the expected value of the length of a returning random walk 
on the football graph (football_AdjM_noWeights.txt). Therefore, we compute the probability of a random walk to return after less or exact k steps. As it is impossible for a walker to return after 1 steps and the case of zero steps is excluded, we have to manipulate the adjacency matrix $A_0$ (football_AdjM.txt) by deleting the edge to walk away from the starting point (set the enties (2,1),(3,1) and (4,1) to zero) to obtain the Transition Matrix $A$ (football_TransitionMatrix.txt). Therefore, the probability of a random walk to return after less or exact k steps (k being greater than 1) can be computed by taking the first entry of the vector $$A^{k-1}v  ~~~ ,~~~ v=(0,1/3,1/3,1/3,0,...,0)^t$$ where $v$ is the "1th-step-probability-vector" derived by $A_0 e_1 =v$ and $e_1$ is the first standard-basis-vector. Therefore, we need to compute the power series $$\sum_{k=2}^\infty kA^{k-1}$$ By considering the geometric series, it can be shown that the series converges and its limit $E$ is "almost" equal to $(I-A)^{-1}$ (they differ in the entry (1,1), which is redundant when multiplying with $v$). Consequently, the expected value is the first entry of $Ev$, which is equal to 20.

Now, we need to compute the probability p of a random walk on a infinite hexagon-floor-tiling-graph not to return after 20 steps. First, we create a method to constrcut arbitrary large centered finite hexagon-floor-tiling-graphs. We can compute $1-p$ by calculating the probability for a random walker to return after less than 21 steps. Therefore, we can construct a sufficiently large hexagon-floor-tiling-graph such that it is immpossible for a walker to reach the border and return within less than 20 steps as we would consequenlty leave out the possible walk to go one tile further and still return. As the constructing of the floor-tiling-graph is based on a breadth-frist search approach, it is sufficient to build a graph with "search-depth" 10 which can be already achieved by building 5 hexagrid-levels around the starting tile.
By extracting the adjacency matrix out of the graph, we can now compute the probability of a walker to terminate at the starting tile after less than 21 steps similar to the calculations above. The result is $1-p=0.55196744$ and by rouding to 7 significant digits, we obtain $$p\approx 0.4480326$$
