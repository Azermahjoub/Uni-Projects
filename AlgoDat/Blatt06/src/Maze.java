import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Class that represents a maze with N*N junctions.
 *
 * @author Vera Röhr
 */
public class Maze {
    private final int N;
    private Graph M;    //Maze
    public int startnode;

    public Maze(int N, int startnode) {

        if (N < 0) throw new IllegalArgumentException("Number of vertices in a row must be nonnegative");
        this.N = N;
        this.M = new Graph(N * N);
        this.startnode = startnode;
        buildMaze();
    }

    public Maze(In in) {
        this.M = new Graph(in);
        this.N = (int) Math.sqrt(M.V());
        this.startnode = 0;
    }


    /**
     * Adds the undirected edge v-w to the graph M.
     *
     * @param v one vertex in the edge
     * @param w the other vertex in the edge
     * @throws IllegalArgumentException unless both {@code 0 <= v < V} and {@code 0 <= w < V}
     */
    public void addEdge(int v, int w) {
        // TODO
        M.addEdge(v, w);
    }

    /**
     * Returns true if there is an edge between 'v' and 'w'
     *
     * @param v one vertex
     * @param w another vertex
     * @return true or false
     */
    public boolean hasEdge(int v, int w) {
        // TODO
        if ((v < 0) || (v > N * N - 1) || (w < 0) || (w > N * N - 1)) {
            return false;
        } else {
            if (v == w) {
                return true;
            } else {
                return M.adj(v).contains(w);
            }
        }
    }

    /**
     * Builds a grid as a graph.
     *
     * @return Graph G -- Basic grid on which the Maze is built
     */
    public Graph mazegrid() {
        // TODO
        Graph graph = new Graph(N * N);
        Graph G = new Graph(N * N);
        for (int i = 0; i < (N * N) - N; i++) {
            G.addEdge(i, i + N);
        }
        for (int i = 0; i < N * N; i++) {
            if ((i) % N != N - 1) {
                G.addEdge(i, i + 1);
            }
        }
        return G;
    }

    /**
     * Builds a random maze as a graph.
     * The maze is build with a randomized DFS as the Graph M.
     */
    private void buildMaze() {
        // TODO
        Graph G = new Graph(N * N);
        G = mazegrid();
        RandomDepthFirstPaths f = new RandomDepthFirstPaths(G, startnode);
        f.randomDFS(G);
        for (int i = 0; i < f.edge().length; i++) {
            if (i != startnode) {
                addEdge(i, f.edge()[i]);
            }
        }
    }

    /**
     * Find a path from node v to w
     *
     * @param v start node
     * @param w end node
     * @return List<Integer> -- a list of nodes on the path from v to w (both included) in the right order.
     */
    public List<Integer> findWay(int v, int w) {
        // TODO
        DepthFirstPaths f = new DepthFirstPaths(M, w);
        f.dfs(M);
        return f.pathTo(v);
    }

    /**
     * @return Graph M
     */
    public Graph M() {
        return M;
    }

    public static void main(String[] args) {
        // FOR TESTING

    }


}

