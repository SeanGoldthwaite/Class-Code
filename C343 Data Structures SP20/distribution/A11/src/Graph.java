import java.util.*;

public class Graph {

    private ArrayList<Item> nodes;
    private Hashtable<Item, ArrayList<Item>> neighbors;
    private Hashtable<Edge, Integer> weights;

    Graph(
            ArrayList<Item> nodes,
            Hashtable<Item, ArrayList<Item>> neighbors,
            Hashtable<Edge, Integer> weights) {
        this.nodes = nodes;
        this.neighbors = neighbors;
        this.weights = weights;
    }

    // -----

    ArrayList<Item> getNodes() {
        return nodes;
    }

    Hashtable<Item, ArrayList<Item>> getNeighbors() {
        return neighbors;
    }

    Hashtable<Edge, Integer> getWeights() {
        return weights;
    }

    // -----
    // Computes all shortest paths from a given node
    // Nodes are marked with the shortest path to the source

    void allShortestPaths(Item source) {
        nodes.forEach(Item::reset);
        nodes.forEach(n -> n.setValue(Integer.MAX_VALUE));
        source.setValue(0);
        WeakHeap heap = new WeakHeap(nodes);
        while (!heap.isEmpty()) {
            Item u = heap.extractMin();
            if (!u.isVisited()) {
                u.setVisited(true);
                neighbors.get(u).forEach(v -> {
                    int newDistance = u.getValue() == Integer.MAX_VALUE ?
                            Integer.MAX_VALUE :
                            u.getValue() + weights.get(new Edge(u, v));
                    if (newDistance < v.getValue()) {
                        heap.updateKey(v.getPosition(), newDistance);
                        v.setPrevious(u);
                    }
                });
            }
        }
    }

    // -----
    // Point-to-point shortest path; stops as soon as it reaches destination

    ArrayList<Edge> shortestPath(Item source, Item dest) {
        nodes.forEach(Item::reset);
        nodes.forEach(n -> n.setValue(Integer.MAX_VALUE));
        source.setValue(0);
        WeakHeap heap = new WeakHeap(nodes);
        while (!heap.isEmpty()) {
	    TreePrinter.print(heap.findMin());
            Item u = heap.extractMin();
            if (u.equals(dest)) break;
            if (!u.isVisited()) {
                System.out.println("visiting" + u);
                u.setVisited(true);
                neighbors.get(u).forEach(v -> {
                    int newDistance = u.getValue() == Integer.MAX_VALUE ? Integer.MAX_VALUE : u.getValue() + weights.get(new Edge(u, v));
                    if (newDistance < v.getValue()) {
                        heap.updateKey(v.getPosition(), newDistance);
                        v.setPrevious(u);
                    }
                });
            }
        }
        return Item.pathToSource(source);
    }

    // -----

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(String.format("Nodes:%n%s%n", nodes));
        res.append(String.format("Neighbors:%n%s%n", neighbors));
        res.append(String.format("Weights:%n%s%n", weights));
        return new String(res);
    }
}
