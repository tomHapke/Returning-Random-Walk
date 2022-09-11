# ReturningRandomWalk
Project for computations and simulations concerning returning-random-walks on graphs and transition matrices

This project contains:
- a **Graph** class to represent weighted graphs, initialize graphs from adjacency matrices and create adjacency matrices vise versa
- a **GraphWalker** class to simulate returning random walks
- a **GraphPrinter** class to print graphs as png- and dot-files (Graphiz must be installed and linked in PATH)
- a **Matrix** class to effectively compute Matrix operations, store and load matrices
- a **MatPowerSeries** class to compute any matrix power series
- a **Calc** class with example scripts
- many more useful methods

**Application:** These methods are applied to **provide a solution to the Jane Street Puzzle "Andy's Morning Stroll" from July 2022** (https://www.janestreet.com/puzzles/andys-morning-stroll-index/), which I handed in successfully. The step-by-step calculations can be executed in the Calc class while important adjacency matrices and graphs are stored in the main folder.


**Format for storing adjacency matrice:**
- the first line just contains an integer that specifies the dimension of the square matrix
- rows beginn and end with "|"
- entries are separated by " , "


**Explanation of the puzzle's solution:**

We first need to compute the expected value of the length of a returning random walk 
on the football graph (football_AdjM_noWeights.txt). Therefore, we compute the probability of a random walk to return after exactly k steps. Because it is impossible for a walker to return after one step and the case of zero steps is excluded, we have to manipulate the adjacency matrix $A_0$ (football_AdjM.txt) by deleting the edge to walk away from the starting point (set the entries (2,1),(3,1) and (4,1) to 0.0 so that the walker is on the starting tile if and only if he returned on the k-th step) to obtain the transition matrix $A$ (football_TransitionMatrix.txt). Therefore, the probability of a random walk to return after exactly k steps (k being greater than 1) can be computed by taking the first entry of the vector $$A^{k-1}v  ~~~ ,~~~ v=(0,1/3,1/3,1/3,0,...,0)^t$$ where $v$ is the "1st-step-probability-vector" derived by $A_0 e_1 =v$ and $e_1$ is the first standard-basis-vector. Therefore, we need to determine the power series $$\sum_{k=2}^\infty kA^{k-1}$$ By some algebra and the geometric series, it can be shown that the series converges and its limit $E$ is "almost" equal to $(I-A)^{-2}$ (they differ in the entry (1,1), which is redundant when multiplying with $v$). Consequently, the expected value is the first entry of $Ev$, which is equal to 20.

Now, we need to compute the probability p of a random walk on a infinite hexagon-floor-tiling-graph not to return after 20 steps. First, we create a method to constrcut arbitraryly large centered finite hexagon-floor-tiling-graphs. We can compute $1-p$ by calculating the probability for a random walker to return after less or exactly 20 steps. We construct a sufficiently large hexagon-floor-tiling-graph such that it is immpossible for a walker to reach the border and return within less than 20 steps as we would consequenlty leave out the case to go one tile further and still return to the starting tile. As the constructing of the floor-tiling-graph is based on a breadth-frist search approach, it is sufficient to build a graph with "search-depth" 10 which can be already achieved by building 5 hexagrid-levels around the starting tile.
By extracting the adjacency matrix out of the graph, we can now compute the probability of a walker to terminate at the starting tile after less or exactly 20 steps similarly to the calculations above (additionally set the entry (1,1) of the adjacency matrix to 1.0 to obtain the transition matrix as the walker should "wait" on the starting tile in case he returned in less than 20 steps). The result is $1-p=0.55196744$ and by rouding to seven significant digits, we obtain $$p\approx 0.4480326$$
