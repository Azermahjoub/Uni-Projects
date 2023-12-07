import java.util.Collections;
import java.util.Stack;

public class ShortestPathsTopological {
    private int[] parent;
    private int s;
    private double[] dist;

    public ShortestPathsTopological(WeightedDigraph G, int s) {
        // TODO
        this.s=s;
        TopologicalWD tp = new TopologicalWD(G);
        tp.dfs(s);
        dist = new double[G.V()];
        for (int i = 0 ; i < G.V(); i++){
            dist[i] = Double.POSITIVE_INFINITY;
        }
        parent = new int[G.V()];
        for (int i = 0 ; i < G.V() ; i++){
            parent[i] = -1;
        }
        Stack<Integer> rev = tp.order();
        Collections.reverse(rev);
        dist[s]=0;
        for  (Integer v: rev){
            for (DirectedEdge e : G.incident(v)){
                relax(e);
            }
        }
    }

    public void relax(DirectedEdge e) {
        // TODO
        if (dist[e.to()]> dist[e.from()]+e.weight()){
            parent[e.to()] = e.from();
            dist[e.to()] = dist[e.from()]+e.weight();
        }
    }

    public boolean hasPathTo(int v) {
        return parent[v] >= 0;
    }

    public Stack<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        Stack<Integer> path = new Stack<>();
        for (int w = v; w != s; w = parent[w]) {
            path.push(w);
        }
        path.push(s);
        return path;
    }
}

