import java.util.function.Function;

abstract class BinTree {
    abstract boolean leaf();
    abstract int height ();
    abstract int edges ();
    abstract List longestPath();
    abstract int sum ();
    abstract int nodes ();
    abstract int internalNodes ();
    abstract int leaves ();
    abstract List flatten ();
    abstract List flattenH (List nums);
    List flatten2 () { return flattenH(new EmptyL()); }
    abstract BinTree map (Function<Integer,Integer> f);
}

class Leaf extends BinTree {
    private int d;

    Leaf (int d) { this.d = d; }

    boolean leaf() { return true; }

    int height() {
        return 0;
    }

    int edges () { return 0; }

    List longestPath() {
        return new NodeL(d, new EmptyL());
    }

    int sum() {
        return d;
    }

    int nodes() {
        return 1;
    }

    int internalNodes() {
        return 0;
    }

    int leaves() {
        return 1;
    }

    List flatten() {
        return new NodeL(d,new EmptyL());
    }

    List flattenH(List nums) {
        return new NodeL(d,nums);
    }

    BinTree map(Function<Integer, Integer> f) {
        return new Leaf(f.apply(d));
    }

}

class NodeT extends BinTree {
    private int d;
    private BinTree t1, t2;

    NodeT (int d, BinTree t1, BinTree t2) {
        this.d = d;
        this.t1 = t1;
        this.t2 = t2;
    }

    boolean leaf() { return false; }

    int height() {
        return 1 + Integer.max(t1.height(),t2.height());
    }

    int edges () { return 2 + t1.edges() + t2.edges(); }

    List longestPath() {
        List t1P = t1.longestPath();
        List t2P = t2.longestPath();
        List longest = t1P.length() > t2P.length() ? t1P : t2P;
        return new NodeL(d, longest);
    }

    int sum() {
        return d + t1.sum() + t2.sum();
    }

    int nodes() {
        return 1 + t1.nodes() + t2.nodes();
    }

    int internalNodes() {
        return 1 + t1.internalNodes() + t2.internalNodes();
    }

    int leaves() {
        return t1.leaves() + t2.leaves();
    }

    List flatten() {
        return t1.flatten().append(new NodeL(d, t2.flatten()));
    }

    List flattenH(List nums) {
        return t1.flattenH(new NodeL(d, t2.flattenH(nums)));
    }

    BinTree map(Function<Integer, Integer> f) {
        return new NodeT(f.apply(d), t1.map(f), t2.map(f));
    }
}
