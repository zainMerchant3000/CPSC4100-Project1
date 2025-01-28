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
            int rootQ = find(q);
            // p and q contain same root (already merged)
            if (rootP == rootQ) {
                return;
            }
            // make smaller root point to larger one
            // if p < q
            if (size[rootP] < size[rootQ]) {
                // 1) set q to be parent of p
                parent[rootP] = rootQ;
                // 2) update q size by adding subtree p
                size[rootQ] += size[rootP];
            } else {
                // otherwise p >= q
                // if p == q: rootP is the parent of rootQ (for consistency)
                // else(size[rootP] >= size[rootQ])
                // 1) set p to be the parent of q
                parent[rootQ] = rootP;
                // 2) Update p size by adding subtree q
                size[rootP] += size[rootQ];
            }
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
            // get degree (calculated by finding number of connections of each node)
            degree = new int[N+1]; // 1) initialize array
            for (int i = 1; i <= N; i++) { // find #of connections of each Node
                degree[i] = adjList.get(i).size(); // store number of connections in array
                //System.out.println("for node "+ (i + 1) + " " + degree[i] + " ");
                // System.out.println("neighbors of node "+ (i + 1) + " " + adjList.get(i) + " ");
            }
            // example: adjList.get(0).size() -> getting number of connected components of node 1 (i + 1)
            // adjList.get(i) -> neighbors in node i
            //

            //1) call Union-Find algorithm to find connected components
            UnionFind uf = new UnionFind(N + 1); //
            for (int i = 1; i <= N; i++) { // go through each node in graph
                for (int neighbor : adjList.get(i)) { // go through all neighbors of node i
                    uf.union(neighbor, i); // construct the graph
                    // neighbor -> connections of Node i
                    // i -> Node

                }
            }

            // 2) Find all connected Components
            // Map<Integer, Integer> map = new HashMap<>(); // create Components map
            var map = new int[N + 1];
            // key = root of connected component
            // value = list of nodes in component
            for (int i = 1; i <= N; i++) {
                int root = uf.find(i); // find root of each Node
                map[i] = root;
                System.out.println(i + " " + root);
            }
            // cc stores distinct connected components
            var cc = new ArrayList<Integer>();
            Arrays.sort(map); // put all the equal items together
            cc.add(map[1]);
            for (int x = map[1], i = 2; i <= N; i++) {
                if (x != map[i]) {
                    x = map[i];
                    cc.add(x);
                }
            }
            int maxScore = 0;
            for (int i = 0; i < cc.size(); i++) {
                int c = cc.get(i);
                var ns = new ArrayList<Integer>();
                for (int n = 1; n <= N; n++) {
                    if (map[n] == c) ns.add(n);
                }
                if (ns.size() == 1) {
                    continue;
                }
                if (ns.size() == 2) {
                    maxScore = Math.max(maxScore, 2);
                    continue;
                }
                //



            }

            // your code here

            // Output the result
            // System.out.println(maxScore);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the input file:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
