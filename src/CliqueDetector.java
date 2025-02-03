//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// store N, M
public class CliqueDetector {
    static int N, M;
    static int[] degree; //
    static List<Set<Integer>> adjList; // to store
    // ex) 1: {

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
            System.out.println("name of file: " + br);
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
            //
            adjList = new ArrayList<>();
            //iterate over every Node in the graph
            // creating list of sets (each
            for (int i = 0; i <= N; i++) {
                adjList.add(new HashSet<>());
                // HashSet<> : represents
            }

            // ex: adjList = [ new HashSet<>(), new HashSet<>()]
            // HashSet<>() -> set for each Node

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
                System.out.println("Adjacency List after adding edge (" + x + ", " + y + "):");
                for (int j = 1; j <= N; j++) {
                    System.out.print(j + ": " + adjList.get(j) + " ");
                }
                System.out.println();  // Print a newline after each printout

            }

            br.close();
            System.out.println("just read into adjancency list");
            // get degree (calculated by finding number of connections of each node)
            degree = new int[N + 1]; // 1) initialize array
            for (int i = 1; i <= N; i++) { // find #of connections of each Node
                degree[i] = adjList.get(i).size(); // store number of connections in array
                System.out.print("degree[i] " + degree[i] + " ");
            }
            // example: adjList.get(1).size() -> getting number of connected components of node 1
            // adjList.get(i) -> neighbors in node i

            int maxScore = bestScore();
            System.out.println("maxScore: " + maxScore);
            // your code here

            // Output the result
            // System.out.println(maxScore);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the input file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static int bestScore() {
        //1) call Union-Find algorithm to find connected components
        UnionFind uf = new UnionFind(N + 1); //
        System.out.println("going to do Union find ");
        for (int i = 1; i <= N; i++) { // go through each node in graph
            for (int neighbor : adjList.get(i)) { // go through all neighbors of node i
                uf.union(i, neighbor); // go through
               // System.out.println("Neighbor " + neighbor + " " + i);
                System.out.println("uf.union(" + i + ", " + neighbor + ")");
                // neighbor -> connections of Node i
                // i -> Node

            }
        }
        // 2) Find all connected Components
        // cc stores distinct connected components
        // root of each node determines connectivity
        // (can sort the value of the roots to check for disjoint sets)

        var cc = new ArrayList<Integer>();
        var roots = new int[N + 1];
        {
            // map is like 'roots' but it gets sorted.
            // it's solely used to find 'cc'.
            var map = new int[N + 1];
            for (int i = 1; i <= N; i++) {
                int root = uf.find(i); // find root of each Node
                map[i] = root; // store root of each Node in map Array
                roots[i] = root;
                // ex) i = 1, find(1) returns 1 map[1] = 1
                System.out.print("map[i] " + map[i] + " ");
                System.out.println("Node: " + i + " root of Node " + root);
            }
            Arrays.sort(map); // put all the equal items together (group all nodes with same root together)
            cc.add(map[1]); // add first root to connected component
            for (int x = map[1], i = 2; i <= N; i++) { // x = map[1], the root of the first node
                if (x != map[i]) { // check for changes in Root values
                    x = map[i]; // update x
                    cc.add(x); // add new root to connected component
                }
            }
            System.out.println("cc: " + cc.toString());
        }
        int maxScore = 0;
        // cc.size() -> gives number of distinct connected components
        // processing connected component for each root
        // retrieve each root from connected component
        /// calculating node that has smallest degree in connected component ns
        /// add nodes that have smallest degree
        /// store in array
        /// score_no -> calculating score of entire (connected component)
        /// score_yes -> store the maximum score by considering cliques that exclude nodes w/
        /// smallest degree
        for (int c : cc) {
            System.out.println("c: " + c);
            var ns = new ArrayList<Integer>(); // will store nodes belonging to the current connected component
            // find Nodes in current connected component
            // n <= N -> iterate through every Node in the graph (1 to N)
            for (int n = 1; n <= N; n++) {
                // roots[n] -> contains root of connected component for node n
                // c -> root of current connected node
                // roots[n] == c:
                if (roots[n] == c) ns.add(n); System.out.println("ns: " + ns.toString());
            }
            // connected component only has one node (skip) clique cannot be single node
            if (ns.size() == 1) {
                continue;
            } // base-case: if there are only two nodes
            // the score must always be 2 (degree = 1, clique size = 2)
            if (ns.size() == 2) {
                maxScore = Math.max(maxScore, 2);
                continue;
            }
            System.out.println("ns: " + ns.toString());
            // implement recursive case for cliques
            // find cliques within the connected component

            /// calculating node that has smallest degree in connected component ns
            int leastDegree = Integer.MAX_VALUE;
            //
            var leastDegreeVertices = new ArrayList<Integer>();
            // iterate through each node in the connected component (ns)
            for (int n : ns) {
                // updates leastDegree to be minumum of current leastDegree
                // degree[n] -> looking at degree of each connected component
                // System.out.println("n: " + n);
                leastDegree = Math.min(leastDegree, degree[n]);
                System.out.println("leastDegree: " + leastDegree);
            }
            /// add nodes that have smallest degree
            for (int n : ns) {
                // retrieve nodes with smallest degree
                if (degree[n] == leastDegree) {
                    /// store in array
                    leastDegreeVertices.add(n);
                    System.out.println("leastDegreeVertices: " + leastDegreeVertices.toString());
                }
            }


            int score_no = 0;
            // start by calculating score of entire connected component (clique = entire connected component)
            int score_yes = leastDegree * ns.size();
            System.out.println("score_yes: " + score_yes);
            // go through vertices with leastDegree
            for (int n : leastDegreeVertices) {
                // create list of neighbors before modifying adjancency list
                List<Integer> neighbors = new ArrayList<>(adjList.get(n));
                // used to track changes to the degrees of the nodes
                Stack<int[]> degreeChanges = new Stack<>();
                // used to track changes to adj list
                Stack<int[]> adjChanges = new Stack<>();
                System.out.println("Vertice with least degree (n): " + n);
                // m: neighbors in node n
                // adjList.get(n): set of neighbors for node n
                //
                for (int m : neighbors) {
                    // push current state of degree before modifying degree of node
                    // m: first element is neighbor node m
                    // degree[m]: current degree of node m
                    degreeChanges.push(new int[]{m, degree[m]});
                    // push current state of degree before modifying connection between n and m
                    //
                    adjChanges.push(new int[]{m, n});
                    //System.out.println("neighbor of least degree vertice (m): " + m);
                    //System.out.println("adjList.get(n): " + adjList.get(n));
                    // Update degree and remove connection
                    degree[m]--; // account for removal of connection between n and m
                    // System.out.println("degree[m]: " + degree[m]);
                    adjList.get(m).remove(n); //remove actual connection between n and m

                    System.out.println("adjList[n] after removal of connection n and m: " + adjList.get(m));
                }
                var emptySet = new HashSet<Integer>();
                var existingSet = adjList.get(n);
                adjList.set(n, emptySet);

                score_no = Math.max(score_no, bestScore());
                adjList.set(n, existingSet);
                System.out.println("score_no: " + score_no);




                /*
                // restore connections after calculation of remaining scores
                for (int m : adjList.get(n)) {
                    degree[m]++;
                    adjList.get(m).add(n);
                }
                 */

                for (int k = 1; k <= N; k++) {
                    System.out.println("adjList.get(k) " + adjList.get(k));
                }

                while (!degreeChanges.isEmpty()) {
                    int[] change = degreeChanges.pop();
                    degree[change[0]] = change[1];
                }

                while (!adjChanges.isEmpty()) {
                    int[] change = adjChanges.pop();
                    adjList.get(change[0]).add(change[1]);
                }

                // calculate highest score after recursive removal of nodes
                maxScore = Math.max(maxScore, Math.max(score_yes, score_no));
                System.out.println("maxScore: " + maxScore);
            }



        }
        for (int i = 1; i <= N; i++) {
            System.out.println("adjList.get(i) " + adjList.get(i));
        }
        return maxScore;
    }

}


