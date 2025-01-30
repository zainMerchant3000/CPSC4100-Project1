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
        static int[] parent; // parent[i] = parent of i
        static int[] size; // size[i] = number of elements in subtree located at i

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

            System.out.println("After union(" + p + ", " + q + "):");
            System.out.println("parent: " + Arrays.toString(parent));
            System.out.println("size: " + Arrays.toString(size));


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
               /* System.out.println("Adjacency List after adding edge (" + x + ", " + y + "):");
                for (int j = 1; j <= N; j++) {
                    System.out.print(j + ": " + adjList.get(j) + " ");
                }
                System.out.println();  // Print a newline after each printout
            */
            }


            br.close();
            // get degree (calculated by finding number of connections of each node)
            degree = new int[N + 1]; // 1) initialize array
            for (int i = 1; i <= N; i++) { // find #of connections of each Node
                degree[i] = adjList.get(i).size(); // store number of connections in array
                //  System.out.print(degree[i] + " ");
            }
            // example: adjList.get(1).size() -> getting number of connected components of node 1
            // adjList.get(i) -> neighbors in node i
            //

            bestScore();

            // your code here

            // Output the result
            // System.out.println(maxScore);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the input file:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void bestScore() {
        //1) call Union-Find algorithm to find connected components
        UnionFind uf = new UnionFind(N + 1); //
        for (int i = 1; i <= N; i++) { // go through each node in graph
            for (int neighbor : adjList.get(i)) { // go through all neighbors of node i
                uf.union(i, neighbor); // go through
                //System.out.println("Neighbor " + neighbor + " " + i);
                //System.out.println("uf.union(" + neighbor + ", " + i + ")");
                // neighbor -> connections of Node i
                // i -> Node

            }
        }
        // 2) Find all connected Components
        // Map<Integer, Integer> map = new HashMap<>(); // create Components map
        // map[i] -> storing root of each node
        var map = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = uf.find(i); // find root of each Node
            map[i] = root; // store root of each Node in map Array
            // ex) i = 1, find(1) returns 1 map[1] = 1
            // System.out.print(map[i] + " ");
            //System.out.println("Node: " + i + " root of Node " + root);
        }
        // cc stores distinct connected components
        // root of each node determines connectivity
        // (can sort the value of the roots to check for disjoint sets)

        var cc = new ArrayList<Integer>();
        Arrays.sort(map); // put all the equal items together (group all nodes with same root together)
        cc.add(map[1]); // add first root to connected component
        for (int x = map[1], i = 2; i <= N; i++) { // x = map[1], the root of the first node
            if (x != map[i]) { // check for changes in Root values
                x = map[i]; // update x
                cc.add(x); // add new root to connected component
            }
        }
        // System.out.println("cc: " + cc.toString());
        int maxScore = 0;
        // cc.size() -> gives number of distinct connected components
        // processing connected component for each root
        for (int i = 0; i < cc.size(); i++) {
            int c = cc.get(i); // retrieve each root from connected component
            System.out.println("c: " + c);
            var ns = new ArrayList<Integer>(); // will store nodes belonging to the current connected component
            // find Nodes in current connected component
            // n <= N -> iterate through every Node in the graph (1 to N)
            for (int n = 1; n <= N; n++) {
                // map[n] -> contains root of connected component for node n
                // c -> root of current connected node
                // map[n] == c:
                if (map[n] == c) ns.add(n);
            }
            // connected component only has one node (skip) clique cannot be single node
            if (ns.size() == 1) {
                continue;
            } // base-case: if there are only two nodes
            // the score must always be 2 (degree = 1)
            if (ns.size() == 2) {
                maxScore = Math.max(maxScore, 2);
                continue;
            }
            System.out.println("ns: " + ns.toString());
            // implement recursive case for cliques
            // find cliques within the connected component
            //
            // List<Set<Integer>> cliques = findCliques(ns, adjList);
            int leastDegree = 1000;
            var leastDegreeVertices = new ArrayList<Integer>();
            for (int n: ns) {
                leastDegree = Math.min(leastDegree, degree[n]);
            }
            for (int n: ns) {
                if (degree[n] == leastDegree) {
                    leastDegreeVertices.add(n);
                }
            }
            int score_no = leastDegree * ns.size();
            int score_yes = 0;
            for (int n: leastDegreeVertices) {
                for (int m: adjList.get(n)) {
                    degree[m]--;
                    adjList.get(m).remove(n);
                }

                // TODO: do recursion on best score and assign into score_yes
                score_yes = Math.max(score_yes, bestScore());
                for (int m: adjList.get(n)) {
                    degree[m]++;
                    adjList.get(m).add(n);
                }
            }
            /*
            // Calculate scores for each clique
            for (Set<Integer> clique : cliques) {
                 int score = calculateCliqueScore(clique, adjList);
                 maxScore = Math.max(maxScore, score);
               }

             */


        }
    }

    /*
    static List<Set<Integer>> findCliques(List<Set<Integer>> adjList, int[] degree) {
        // Implement a method to find all cliques in the graph
        // This can be done using a recursive approach or a combinatorial approach
        return new ArrayList<>(); // Placeholder return
    }

    static int calculateCliqueScore(Set<Integer> clique, List<Set<Integer>> adjList) {
    int size = clique.size();
    int minDegree = Integer.MAX_VALUE;
    for (int node : clique) {
        int degree = 0;
        for (int neighbor : adjList.get(node)) {
            if (clique.contains(neighbor)) {
                degree++;
            }
        }
        minDegree = Math.min(minDegree, degree);
    }
    return size * minDegree;
}


     */
}
