import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;


public class FlowApplications {

    /**
     * cloneFlowNetwork() makes a deep copy of a FlowNetwork
     * (FlowNetwork has unfortunately no copy constructor)
     *
     * @param flowNetwork the flow network that should be cloned
     * @return cloned flow network (deep copy) with same order of edges
     */
    private static FlowNetwork cloneFlowNetwork(FlowNetwork flowNetwork) {
        int V = flowNetwork.V();
        FlowNetwork clone = new FlowNetwork(V);

//        Simple version (but reverses order of edges)
//        for (FlowEdge e : flowNetwork.edges()) {
//            FlowEdge eclone = new FlowEdge(e.from(), e.to(), e.capacity());
//            clone.addEdge(eclone);
//        }

        for (int v = 0; v < flowNetwork.V(); v++) {
            // reverse so that adjacency list is in same order as original
            Stack<FlowEdge> reverse = new Stack<>();
            for (FlowEdge e : flowNetwork.adj(v)) {
                if (e.to() != v) {
                    FlowEdge eclone = new FlowEdge(e.from(), e.to(), e.capacity());
                    reverse.push(eclone);
                }
            }
            while (!reverse.isEmpty()) {
                clone.addEdge(reverse.pop());
            }
        }
        return clone;
    }


    /**
     * numberOfEdgeDisjointPaths() returns the (maximum) number of edge-disjoint paths that exist in
     * an undirected graph between two nodes s and t using Edmonds-Karp.
     *
     * @param graph the graph that is to be investigated
     * @param s     node on one end of the path
     * @param t     node on the other end of the path
     * @return number of edge-disjoint paths in graph between s and t
     */

    public static int numberOfEdgeDisjointPaths(Graph graph, int s, int t) {
        // TODO
        FlowNetwork flowgraph = new FlowNetwork(graph.V());
        for (int v = 0; v < graph.V(); v++) {
            for (Integer other : graph.adj(v)) {
                FlowEdge fe = new FlowEdge(v, other, 1);
                flowgraph.addEdge(fe);
            }
        }
        FordFulkerson fordgraph = new FordFulkerson(flowgraph, s, t);
        return (int) (fordgraph.value());
    }

    /**
     * edgeDisjointPaths() returns a maximal set of edge-disjoint paths that exist in
     * an undirected graph between two nodes s and t using Edmonds-Karp.
     *
     * @param graph the graph that is to be investigated
     * @param s     node on one end of the path
     * @param t     node on the other end of the path
     * @return a {@code Bag} of edge-disjoint paths in graph between s and t
     * Each path is stored in a {@code LinkedList<Integer>}.
     */

    public static Bag<LinkedList<Integer>> edgeDisjointPaths(Graph graph, int s, int t) {
        // TODO
        Bag<LinkedList<Integer>> result = new Bag<LinkedList<Integer>>();
        FlowNetwork flowgraph = new FlowNetwork(graph.V());
        for (int v = 0; v < graph.V(); v++) {
            for (Integer other : graph.adj(v)) {
                FlowEdge fe = new FlowEdge(v, other, 1);
                flowgraph.addEdge(fe);
            }
        }
        FordFulkerson fordgraph = new FordFulkerson(flowgraph, s, t);
        Stack<FlowEdge> used = new Stack<FlowEdge>();
        for (FlowEdge fe : flowgraph.adj(s)) {
            if (fe.flow() == 1 && !used.contains(fe)) {
                used.push(fe);
                LinkedList<Integer> path = new LinkedList<Integer>();
                path.add(s);
                int v = fe.other(s);
                while (v != t) {
                    path.add(v);
                    for (FlowEdge fe_sec : flowgraph.adj(v)) {
                        if (fe_sec.flow() == 1 && !used.contains(fe_sec) &&  fe_sec.from() == v) {
                            used.push(fe_sec);
                            v = fe_sec.other(v);
                            break;
                        }
                    }
                }

                path.add(t);
                result.add(path);
            }
        }
        return result;
    }


    /**
     * isUnique determines for a given Flow Network that has a guaranteed minCut,
     * if that one is unique, meaning it's the only minCut in that network
     *
     * @param flowNetworkIn the graph that is to be investigated
     * @param s             source node s
     * @param t             sink node t
     * @return true if the minCut is unique, otherwise false
     */

    public static boolean isUnique(FlowNetwork flowNetworkIn, int s, int t) {
        // TODO
        Boolean[] state_s = new Boolean[flowNetworkIn.V()];
        FordFulkerson fordNetwork_s = new FordFulkerson(flowNetworkIn, s, t);
        Boolean[] state_t = new Boolean[flowNetworkIn.V()];
        FlowNetwork reversed = new FlowNetwork(flowNetworkIn.V());
        for (FlowEdge fe : flowNetworkIn.edges()) {
            FlowEdge e = new FlowEdge(fe.to(), fe.from(), fe.capacity());
            reversed.addEdge(e);
        }
        FordFulkerson fordNetwork_t = new FordFulkerson(reversed, t, s);
        for (int i = 0; i < flowNetworkIn.V(); i++) {
            state_s[i] = fordNetwork_s.inCut(i);
            state_t[i] = fordNetwork_t.inCut(i);
        }
        boolean result = true;
        for (int i = 0; i < flowNetworkIn.V(); i++) {
            if (state_t[i] == state_s[i]) {
                result = false;
            }
        }
        return result;
    }


    /**
     * findBottlenecks finds all bottleneck nodes in the given flow network
     * and returns the indices in a Linked List
     *
     * @param flowNetwork the graph that is to be investigated
     * @param s           index of the source node of the flow
     * @param t           index of the target node of the flow
     * @return {@code LinkedList<Integer>} containing all bottleneck vertices
     * @throws IllegalArgumentException is flowNetwork does not have a unique cut
     */

    public static LinkedList<Integer> findBottlenecks(FlowNetwork flowNetwork, int s, int t) {
        // TODO
        LinkedList<Integer> result = new LinkedList<Integer>();
        FordFulkerson fordNetwork = new FordFulkerson(flowNetwork, s, t);
        for (FlowEdge fe : flowNetwork.edges()) {
            if (fordNetwork.inCut(fe.from()) && !fordNetwork.inCut(fe.to()) && !result.contains(fe.from())) {
                result.add(fe.from());
            }
        }
        return result;
    }

    public static void main(String[] args) {
/*
        // Test for Task 2.1 and 2.2 (useful for debugging!)
        Graph graph = new Graph(new In("Graph1.txt"));
        int s = 0;
        int t = graph.V() - 1;
        int n = numberOfEdgeDisjointPaths(graph, s, t);
        System.out.println("#numberOfEdgeDisjointPaths: " + n);
        Bag<LinkedList<Integer>> paths = edgeDisjointPaths(graph, s, t);
        for (LinkedList<Integer> path : paths) {
            System.out.println(path);
        }
*/

/*
        // Example for Task 3.1 and 3.2 (useful for debugging!)
        FlowNetwork flowNetwork = new FlowNetwork(new In("Flussgraph1.txt"));
        int s = 0;
        int t = flowNetwork.V() - 1;
        boolean unique = isUnique(flowNetwork, s, t);
        System.out.println("Is mincut unique? " + unique);
        // Flussgraph1 is non-unique, so findBottlenecks() should be tested with Flussgraph2
        flowNetwork = new FlowNetwork(new In("Flussgraph2.txt"));
        LinkedList<Integer> bottlenecks = findBottlenecks(flowNetwork, s, t);
        System.out.println("Bottlenecks: " + bottlenecks);
*/
    }

}

