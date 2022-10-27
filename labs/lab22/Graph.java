import jdk.swing.interop.SwingInterOpUtils;

import java.util.*;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
//        System.out.println();
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
//        System.out.print("add:" + " v1: " + v1 + ", v2: "+  v2 +", weight: " + weight + "|||");
        Edge toAdd = new Edge(v1, v2, weight);
        LinkedList<Edge> vertices = adjLists[v1];
        vertices.add(toAdd);
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        // TODO: YOUR CODE HERE
        addEdge(v1, v2, weight);
        addEdge(v2, v1, weight);
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
//        System.out.print("is adjcent, ");
        // TODO: YOUR CODE HERE
        LinkedList<Edge> vertices = adjLists[from];
        for (Edge each : vertices) {
            if (each.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        // TODO: YOUR CODE HERE
//        System.out.print("neighbor");
        List<Integer> result = new ArrayList<>();
        LinkedList<Edge> vertices = adjLists[v];
        for (Edge each : vertices) {
            result.add(each.to);
        }
        return result;
    }
    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
//        System.out.print("Degree: " + v);
        // TODO: YOUR CODE HERE
        int degree = 0;
        for (int i = 0; i < adjLists.length; i++) {
            for (Edge each : adjLists[i]) {
                if (each.to == v) {
                    degree++;
                }
            }
        }
        return degree;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     *  A class that iterates through the vertices of this graph,
     *  starting with a given vertex. Does not necessarily iterate
     *  through all vertices in the graph: if the iteration starts
     *  at a vertex v, and there is no path from v to a vertex w,
     *  then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }


        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

        //ignore this method
        public void remove() {
            throw new UnsupportedOperationException(
                    "vertex removal not implemented");
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
//        System.out.print("dfs: " + v);
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        // TODO: YOUR CODE HERE
        if (start == stop) {
            return true;
        }
        List<Integer> children = dfs(start);
        return children.contains(stop);
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
//        System.out.println(start + ", " + stop);
//        System.out.print(start + ", " + stop + "|");
        List<Integer> result = new ArrayList<>();
//        System.out.println(dfs(start));
//        System.out.println("start == stop:" + (start == stop));
        if (start == stop) {
            result.add(start);
            return result;
        }
//        System.out.println("!pathExists: " + (!pathExists(start, stop)));
        if (!pathExists(start, stop)) {
//            System.out.println("No path");
//            result.add(start);
            return result;
        }
//        System.out.println("----------Path executed--------");
        List<Integer> vertices = dfs(start);
//        System.out.println("vertices: " + vertices);

        int index = vertices.indexOf(stop);
//        System.out.println("index: " + index);

        result.add(stop);
//        System.out.println("result before loop: " + result);
//        while (index >= 0) { // two number are possibly adjacent
        for (int j = stop * stop + 1; j >= 0; j--) {
//            System.out.println("in the while loop");
            for (int i = 0; i <= index; i++) {
//                System.out.println("index - i:" + (index - i));
//                System.out.println("index: " + index);
//                System.out.println("ajacent: " + isAdjacent(vertices.get(index - i), vertices.get(index)));
                if (isAdjacent(vertices.get(index - i), vertices.get(index))) {
                    if (!result.contains(vertices.get(index))) {
                        result.add(vertices.get(index));
                    }
                    index = index - i;
                }
            }
        }
        if (!result.contains(start)) {
            result.add(start);
        }
        Collections.reverse(result);
//        System.out.println("Final result: " + result);
        return result;
    }

    /* reference:
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }
     */



    public List<Integer> topologicalSort() {
//        System.out.println("t, ");
//        System.out.println("topologicalSort, ");
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;

        // TODO: Instance variables here!
        int[] degree = new int[vertexCount];

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            // TODO: YOUR CODE HERE
            for (int i = 0; i < degree.length; i++) {
                if (inDegree(i) == 0) {
                    fringe.push(i);
                }
                degree[i] = inDegree(i);
            }
        }

        public boolean hasNext() {
            // TODO: YOUR CODE HERE
            return !fringe.isEmpty(); // What if unconnected
        }
        public Integer next() {
            // TODO: YOUR CODE HERE
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            for (Integer e : lst) {
                degree[e]--;
                if (degree[e] == 0) {
                    fringe.push(e);
                }
            }
            return curr;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    /*
    at AGTestGraph.testPath4Undirected:113 (AGTestGraph.java)
    add: 0, 1,
    add: 1, 0,
    add: 0, 2,
    add: 2, 0,
    add: 1, 3,
    add: 3, 1,
    add: 1, 4,
    add: 4, 1,
    add: 2, 5,
    add: 5, 2,
    add: 2, 6,
    add: 6, 2,
 */
    private void generateG5() {
        addUndirectedEdge(0, 1);
        addUndirectedEdge(2, 0);
        addUndirectedEdge(3, 1);
        addUndirectedEdge(4, 1);
        addUndirectedEdge(2, 5);
        addUndirectedEdge(2, 6);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
//        System.out.println("p called");
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(7);
        g1.generateG5();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(4);
        g1.printDFS(5);
        g1.printDFS(6);

        /*
        at AGTestGraph.testPath4Undirected:113 (AGTestGraph.java)
        0, 1|
        0, 2|
        0, 3|
        0, 4|
        0, 5|
        0, 6|
        1, 0|
        1, 2|
        1, 3|
        1, 4|
        1, 5|
        1, 6|
        2, 0|
        2, 1|
        2, 3|
        2, 4|
        2, 5|
        2, 6|
        3, 0|
     */
        g1.printDFS(3);
        g1.printPath(3, 0);
//        g1.printVertices();
//        System.out.println("4 to 0: " + g1.pathExists(4, 0));

        Graph g2 = new Graph(5);
        g2.generateG2();
        g2.printTopologicalSort();

    }

    public void printVertices() {
        for (int i = 0; i < adjLists.length; i++) {
            for (int j = 0; j < adjLists.length; j++){
                printPath(i, j);
            }
        }
    }

    public List<Integer> shortestPath(int start, int stop) {
        // Initialization
        PriorityQueue<Vertex> fringe = new PriorityQueue<>();
        boolean[] isVisted = new boolean[vertexCount];
        //Terminal: Next pair
        HashMap<Integer, Integer> routeTable = new HashMap<>();
//        HashMap<Integer, Integer> distanceTable = new HashMap<>();
        int[] distanceTable = new int[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            distanceTable[i] = Integer.MAX_VALUE;
        }
        // Start vertex
        fringe.add(new Vertex(start, 0));
        routeTable.put(start, start);
        distanceTable[start] = 0;
    // Traverse
        while (!fringe.isEmpty()) {

            Vertex currVertex = fringe.poll();
            int currID = currVertex.nodeID;
//            System.out.println("Curr vertes ID:" + currID);
//            System.out.println("Finge:" + fringe);
            //edges from current vertex
            for (Edge edges: adjLists[currVertex.getNodeID()]) {
//                System.out.println("Now is reading edges from " + currID + " to " + edges.to + ". Visted? : " + isVisted[currID]);
                if (!isVisted[edges.to]) {
                    //check Contains: if yes, then compare the distance; if no, just add
                    boolean isInFringe = false;
//                    int cumulativeDistance = 0;
                    for (Vertex fringedVertex: fringe) {
                        if (fringedVertex.nodeID == edges.to) {
                            isInFringe = true;
//                            cumulativeDistance = fringedVertex.distance;
//                            cumulativeDistance = distanceTable[currID];
                            break;
                        }
                    }
                    //add to fringe directly
//                    System.out.println("Distance to " +currID + ": " +  distanceTable[currID] + ". Distance to new node: " + edges.weight);
                    int newDistance = edges.weight + distanceTable[currID];
//                    System.out.println("is in the Fringe: " + isInFringe + " New distance: " + newDistance);
                    if (!isInFringe) {
//                        System.out.println("Not in the fringe, adding node ID: " + edges.to + " distance: " + newDistance);
                        fringe.add(new Vertex(edges.to, newDistance));
                        distanceTable[edges.to] = newDistance;
                        routeTable.put(edges.to, currID);
                    }

                    /*If it is in the fringe, update it.
                      if the distance (added by cumulative and current edge)
                      is less than recorded, then update distance table and
                     */
                    else if (newDistance < distanceTable[edges.to]) {
//                        System.out.println("In the fringe, updating node ID: " + edges.to + " distance: " + newDistance);
                        fringe.add(new Vertex(edges.to, newDistance));
                        distanceTable[edges.to] = newDistance;
                        routeTable.put(edges.to, currID);
                    }
                }
            }
            isVisted[currID] = true;
//            System.out.println(routeTable.toString());
//            System.out.println("===============");
        }
        if (routeTable.containsKey(stop)) {
            LinkedList<Integer> reversed = new LinkedList<>();
            int pointer = stop;
            while (pointer != start) {
                reversed.add(pointer);
                pointer = routeTable.get(pointer);
            }
            reversed.add(start);
            Collections.reverse(reversed);
            return reversed;
        } else {
            return null;
        }
    }

    public Edge getEdge(int u, int v) {
        // TODO: YOUR CODE HERE
        for (Edge edges: adjLists[u]) {
            if (edges.to == u) {
                return edges;
            }
        }
        return null;
    }


    class Vertex implements Comparable {
        private int nodeID;
        private int distance;

        public Vertex(int nodeID, int distance) {
            this.nodeID = nodeID;
            this.distance = distance;
        }

        @Override
        public int compareTo(Object o){
            Vertex compare = (Vertex) o;
            if (this.distance > compare.distance) {
                return 1;
            }
            else if (this.distance < compare.distance) {
                return -1;
            }
            else {
                return 0;
            }
        }

        public int getNodeID() {
            return nodeID;
        }

        public int getDistance() {
            return distance;
        }
    }

//    private boolean fringeContains(PriorityQueue<Vertex> fringe, int vertexID) {
//        boolean isInFringe = false;
//        for (Vertex fringedVertex: fringe) {
//            if (fringedVertex.nodeID == vertexID) {
//                isInFringe = true;
//                break;
//            }
//        }
//        return isInFringe;
//    }
}

