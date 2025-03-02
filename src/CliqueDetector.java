import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/// Zain-Abbas Merchant
/// CPSC 4100 Assignment 1
// store N, M
public class CliqueDetector {
    static int N, M;
    static int[] degree; //
    static List<Set<Integer>> adjList; // to store

    // Node class to implement our PriorityQueue
    static class Node implements Comparable<Node> {
        int id;
        int degree;

        Node(int id, int degree) {
            this.id = id;
            this.degree = degree;
        }

        @Override
        public int compareTo(Node o) {
            return this.degree - o.degree;
        }
    }


    // Static class (Nested class)
    static class UnionFind {
        int[] parent; // parent[i] = parent of i
        int[] size; // size[i] = number of elements in subtree located at i


        //constructor for UnionFind:
        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                // System.out.println("parent[i[" + i + "]] = " + parent[i]);
                size[i] = 1;
            }
        }

        int find(int i) {
            while (i != parent[i]) {
                parent[i] = parent[parent[i]]; // path halving
                i = parent[i];
            }
            return i;
        }

        // method to check if two elements are in the same set:
        public boolean connected(int p, int q) {
            return find(p) == find(q);
        }

        void union(int p, int q) {
            // locate root of p and q
            int rootP = find(p);
            // System.out.println("rootP = " + rootP);
            //System.out.println("p = " + p);
            int rootQ = find(q);
            //  System.out.println("rootQ = " + rootQ);
            // System.out.println("q = " + q);
            // p and q contain same root (already merged)
            if (rootP == rootQ) {
                //  System.out.println("p and q contain same root");
                return;
            }
            // make smaller root point to larger one
            // if p < q
            if (size[rootP] < size[rootQ]) {
                // System.out.println("size[rootP] < size[rootQ]");
                // 1) set q to be parent of p
                parent[rootP] = rootQ;
                //System.out.println("parent[rootP] = " + parent[rootP]);
                // 2) update q size by adding subtree p
                size[rootQ] += size[rootP];
                //System.out.println("size[rootQ] = " + size[rootQ]);
            } else {
                // System.out.println("size[rootQ] >= size[rootP]");
                // otherwise p >= q
                // if p == q: rootP is the parent of rootQ (for consistency)
                // else(size[rootP] >= size[rootQ])
                // 1) set p to be the parent of q
                parent[rootQ] = rootP;
                // System.out.println("parent[rootQ] = " + parent[rootQ]);
                // 2) Update p size by adding subtree q
                size[rootP] += size[rootQ];
                //System.out.println("size[rootP] = " + size[rootP]);
            }
            // Print the state of the parent and size arrays after each union operation
            /*
            System.out.println("After union(" + p + ", " + q + "):");
            System.out.println("parent: " + Arrays.toString(parent));
            System.out.println("size: " + Arrays.toString(size));
           */
        }

        int getSize(int s) {
            return size[find(s)]; // will retrieve the size of
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide the input filename as the first argument.");//
            System.exit(1);
        }
        try {
            System.out.println("going to read text file as argument: ");
            //reading input file
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            // System.out.println("name of file: " + br);
            // Read N and M
            // N = #of nodes
            // M = #of connections/edges
            // br.readLine() -> reading first line of file to get number of Nodes (N) and edges (M)
            // split("\\s+") -> splits first line into an array of substrings (tokens based on whitespace)
            String[] tokens = br.readLine().split("\\s+");
            // token[0] -> N
            // token[1] -> M
            N = Integer.parseInt(tokens[0]);
            // System.out.println("reading N: " + N);
            M = Integer.parseInt(tokens[1]);
            //  System.out.println("reading M: " + M);
            // create adjacency list
            // 1 2
            //1 3
            //1 4
            //2 3
            //2 4
            //3 4
            //1 5
            //2 6
            //3 7
            //4 8
            // ex:
            // {2,3,4,5} Node: 1 -> this is HashSet<>()
            // {1,3,4,6} Node: 2
            // {1,2,4,7}
            adjList = new ArrayList<>();
            //iterate over every Node in the graph
            // creating list of sets (each
            for (int i = 0; i <= N; i++) {
                adjList.add(new HashSet<>());
            }
            // Read edges
            for (int i = 0; i < M; i++) {
                // tokens -> reading each line from file (not including first)
                tokens = br.readLine().split("\\s+");
                // retrieve (x,y) each edge in the graph
                int x = Integer.parseInt(tokens[0]);
                // System.out.println("x: " + x);
                int y = Integer.parseInt(tokens[1]);
                //System.out.println("y: " + y);
                // your code here
                // adjList.get(x): retrieves set associated with node x (contains all neighbors of x)
                // adjList.add(y): adds node y to set of neighbors of node x
                // ex) for (1,2): adjList.get(1).add(2)
                // adjList = [
                //    {} // Node 0 {2} // Node 1 (has neighbor 2) {1} // Node 2 has neighbor 1]
                adjList.get(x).add(y); //
                adjList.get(y).add(x); // for symmetry (undirected 1,2 = 2,1)
                // Print the adjacency list after adding the edge (x, y)
                /*
                System.out.println("Adjacency List after adding edge (" + x + ", " + y + "):");
                for (int j = 1; j <= N; j++) {
                    System.out.print(j + ": " + adjList.get(j) + " ");
                }
                System.out.println();  // Print a newline after each printout
*/
            }

            br.close();

            int maxScore = bestScore();
            System.out.println("maxScore: " + maxScore);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the input file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static int bestScore() {

        // example:
        // 1) create pq: pq = [(1,2), (2,2), (3,2)]
        // 2) remove node with smallest degree:
        //    pq.poll() // (1,2)
        //    // check if
        //  3) mark and track the order that this node has been removed:
        //     -> removed(1) = true [false, true, false, ... ]
        //     -> degreeAtRemoval(1) = 2   removalDegree[0, 2, ... ,]
        //     -> removalO(0) = 1
        //  4) check neighbors of the node (in this case (1)
        //     // adjList(
        //
        boolean[] removed = new boolean[N + 1];
        int[] degreeAtRemoval = new int[N + 1];
        int[] removalO = new int[N + 1]; // order of removal
        int[] degreeC = new int[N + 1]; // array to store current degree of our Nodes
        boolean[] inQueue = new boolean[N + 1]; // to track if Node already in Queue (no duplicates)
        // 1) Populate your pq
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 1; i <= N; i++) {
            degreeC[i] = adjList.get(i).size();
            //System.out.println("Degree " + i + ": " + degreeC[i]);
            pq.offer(new Node(i, degreeC[i]));
            inQueue[i] = false;
           // System.out.println("pq: " + pq.size());
        }

        for (int i = 1; i <= N; i++) {
            System.out.println("degreeC: " + i + " " + degreeC[i]);
        }

        int orderI = 0;
        // remove each node in pq that has smallest degree
        while (!pq.isEmpty()) {
            Node node = pq.poll();
            int nodeId = node.id; // local variable to store node
            System.out.println(" removing the node from the pq " + nodeId + " that has degree: " + node.degree);
           // System.out.println("removed[nodeId]: " + removed[nodeId]);
            // check if node has not already been removed or that
            if (removed[nodeId] || node.degree != degreeC[nodeId])
                //System.out.println("removed[nodeId]: " + removed[nodeId]);
                continue;

            //1) mark node as 'removed'
            removed[nodeId] = true;
            //2) store nodes degree once removed
           // System.out.println("marking : " + removed[nodeId]);
            degreeAtRemoval[nodeId] = node.degree;
            //System.out.println("degreeAtRemoval[nodeId]: " +  degreeAtRemoval[nodeId]);
            //3) track order of removal
            removalO[orderI++] = nodeId;
          //  System.out.println("orderI: " + orderI);
           // System.out.println("removalO[nodeId]++: " +  removalO[nodeId]);

            // Custom print statements
            System.out.print("Removal Degree: [");
            for (int i = 1; i <= N; i++) {
                System.out.print(degreeAtRemoval[i]);
                if (i < N) System.out.print(", ");
            }
            System.out.println("]");

            System.out.print("Removal Order: [");
            for (int i = 0; i <= N; i++) {
                System.out.print(removalO[i]);
                if (i < N) System.out.print(", ");
            }
            System.out.println("]");

            System.out.print("Current Degree: [");
            for (int i = 1; i <= N; i++) {
                System.out.print(degreeC[i]);
                if (i < N) System.out.print(", ");
            }
            System.out.println("]");

            System.out.print("Removed Nodes: [");
            for (int i = 1; i <= N; i++) {
                System.out.print(removed[i]);
                if (i < N) System.out.print(", ");
            }
            System.out.println("]");

            System.out.print("inQueue: [");
            for (int i = 1; i <= N; i++) {
                System.out.print(inQueue[i]);
                if (i < N) System.out.print(", ");
            }
            System.out.println("]");

            // Decrease degree of each neighbor to signify it has been removed
            for (int neighbor : adjList.get(nodeId)) {
                // check that neighbor has not already been removed
                if (!removed[neighbor]) {
                    // sever connection
                    degreeC[neighbor]--;
                    if (!inQueue[neighbor]) {
                        // check that neighbor is not already in pq
                        // push back into pq
                        // Print the values before adding to the priority queue
                        System.out.println("Adding to priority queue: Node ID = " + neighbor + ", Degree = " + degreeC[neighbor]);
                        pq.offer(new Node(neighbor, degreeC[neighbor]));
                        // Mark neighbor node as being in the queue
                        inQueue[neighbor] = true;
                    }
                    else {
                        //Remove and re-add the neighbor with updated degree
                        pq.remove(new Node(neighbor, degreeC[neighbor]));
                        pq.offer(new Node(neighbor, degreeC[neighbor]));
                    }
                }
            }
        }

        int maxScore = 0;

        // Add nodes back in reverse removal order and merge connected ones to reconstruct graph
        boolean[] added = new boolean[N+1];
        UnionFind uf = new UnionFind(N+1);
        // Process nodes in reverse removal order
        for (int i = N - 1; i >= 0; i--) {
            // retrieve node from removal order (starting from end)
            int node = removalO[i];
            // mark node
            added[node] = true;
            // Union this node with any neighbor that's already been added
            for (int neighbor : adjList.get(node)) {
                if (added[neighbor]) {
                    uf.union(node, neighbor);
                }
            }
            // Calculating Clique score by
            int componentSize = uf.getSize(node);
            int candidateScore = componentSize * degreeC[node];
            maxScore = Math.max(maxScore, candidateScore);
        }

        return maxScore;
}

    }



