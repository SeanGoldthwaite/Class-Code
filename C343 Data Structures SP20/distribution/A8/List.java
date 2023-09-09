import java.util.function.BiFunction;
import java.util.function.Function;

abstract class List {
    abstract int length();
    abstract List append (List ys);
    abstract List map (Function<Integer,Integer> f);
    abstract int fold (BiFunction<Integer,Integer,Integer> g, int acc);
    abstract List reverse ();
    abstract List reverseH (List ys);
    List reverse2 () { return reverseH(new EmptyL()); }
}

class EmptyL extends List {
    int length() {
        return 0;
    }

    List append(List ys) {
        return ys;
    }

    List map(Function<Integer, Integer> f) {
        return this;
    }

    int fold(BiFunction<Integer, Integer, Integer> g, int acc) {
        return acc;
    }

    List reverse() {
        return this;
    }

    List reverseH(List ys) {
        return ys;
    }
}

class NodeL extends List {
    private int x;
    private List xs;

    NodeL (int x, List xs) {
        this.x = x;
        this.xs = xs;
    }

    int length() {
        return 1 + xs.length();
    }

    List append(List ys) {
        return new NodeL(x, xs.append(ys));
    }

    List map(Function<Integer, Integer> f) {
        return new NodeL(f.apply(x), xs.map(f));
    }

    int fold(BiFunction<Integer, Integer, Integer> g, int acc) {
        return g.apply(x, xs.fold(g,acc));
    }

    List reverse() {
        return xs.reverse().append(new NodeL(x,new EmptyL()));
    }

    List reverseH(List ys) {
        return xs.reverseH(new NodeL(x,ys));
    }
}
