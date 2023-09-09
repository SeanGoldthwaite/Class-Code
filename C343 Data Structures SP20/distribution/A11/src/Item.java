import java.util.ArrayList;

public class Item implements TreePrinter.PrintableNode {
    private String name;
    private int value;
    private int position;
    private WeakHeap wh;
    private boolean visited;
    private Item previous;

    Item(String name, int value) {
        this.name = name;
        this.value = value;
        this.reset();
    }

    void reset() {
        this.position = -1;
        this.visited = false;
        this.previous = null;
    }

    static ArrayList<Edge> pathToSource(Item u) {
        ArrayList<Edge> path = new ArrayList<>();
        Item current = u;
        Item previous = current.getPrevious();
        while (previous != null) {
            path.add(new Edge(current, previous));
            current = previous;
            previous = current.getPrevious();
        }
        return path;
    }

    int getValue() { return value; }

    void setValue (int value) { this.value = value; }

    int getPosition() { return position; }

    void setPosition(int position) {
        this.position = position;
    }

    void setHeap (WeakHeap wh) {
        this.wh = wh;
    }

    boolean isVisited () {
	return visited;
    }

    Item getPrevious () {
	return previous;
    }

    void setVisited (boolean visited) {
	this.visited = visited;
    }

    void setPrevious (Item previous) {
	this.previous = previous;
    }

    public String toString() {
        return name + ":" + value;
    }

    public boolean equals(Object o) {
        if (o instanceof Item) {
            Item that = (Item) o;
            return name.equals(that.name);
        }
        return false;
    }

    public int hashCode() {
        return name.hashCode();
    }

    // PrintableNode interface

    public TreePrinter.PrintableNode getLeft() {
        try {
            return wh.getElem(wh.getLeftChildIndex(position));
        }
        catch (NoLeftChildE e) { return null; }
    }

    public TreePrinter.PrintableNode getRight() {
        try {
            return wh.getElem(wh.getRightChildIndex(position));
        }
        catch (NoRightChildE e) { return null; }
    }

    public String getText() { return toString(); }
}
