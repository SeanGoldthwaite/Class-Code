public class Edge {
    private Item from, to;

    Edge(Item from, Item to) {
        this.from = from;
        this.to = to;
    }

    Item getFrom() {
        return from;
    }

    Item getTo() {
        return to;
    }

    Edge flip() {
        return new Edge(to, from);
    }

    public String toString() {
        return String.format("%s -> %s", from, to);
    }

    public boolean equals(Object o) {
        if (o instanceof Edge) {
            Edge that = (Edge) o;
            return from.equals(that.from) && to.equals(that.to);
        }
        return false;
    }

    public int hashCode() {
        return from.hashCode() + to.hashCode();
    }

}
