import java.util.Random;
import java.util.function.Function;
//---------------------------------------------------------------------------
class EmptyListE extends Exception {}
//---------------------------------------------------------------------------
abstract class List<E> {
    abstract E getFirst() throws EmptyListE;
    abstract List<E> getRest() throws EmptyListE;
    abstract boolean isEmpty();
    abstract int length();
    abstract List<E> append(List<E> other);
    abstract List<E> copy();
    // static methods
    static <A> List<A> MakeList(Function<Void, A> g, int size) {
        List<A> result = new Empty<>();
        for (int i = 0; i < size; i++)
            result = new Node<>(g.apply(null), result);
        return result;
    }
    static List<Integer> MakeIntList(Random r, int bound, int size) {
        return MakeList((v) -> r.nextInt(bound), size);
    }
}
//---------------------------------------------------------------------------
class Empty<E> extends List<E> {
    E getFirst () throws EmptyListE {
        throw new EmptyListE();
    }
    List<E> getRest () throws EmptyListE {
        throw new EmptyListE();
    }
    boolean isEmpty () { return true; }
    int length() { return 0; }
    List<E> append (List<E> other) { return other; }
    List<E> copy () { return this; }
    // override Object
    public boolean equals(Object o) {
        return o instanceof Empty;
    }
    public int hashCode() {
        return 0;
    }
    public String toString () {
        return "_";
    }
}
//---------------------------------------------------------------------------
class Node<E> extends List<E> {
    private E data;
    private List<E> rest;
    private int hash;
    Node (E data, List<E> rest) {
        this.data = data;
        this.rest = rest;
        hash = data.hashCode() + 31 * rest.hashCode();
    }
    E getFirst () { return data; }
    List<E> getRest () { return rest; }
    boolean isEmpty () { return false; }
    int length() { return 1 + rest.length(); }
    List<E> append (List<E> other) {
        return new Node<>(data, rest.append(other));
    }
    List<E> copy () { return new Node<>(data, rest.copy()); };
    // override Object
    public boolean equals(Object o) {
        if (!(o instanceof Node)) return false;
        else {
            Node<E> that = (Node<E>) o;
            return data.equals(that.data) && rest.equals(that.rest);
        }
    }
    public int hashCode() {
        return hash;
    }
    public String toString () {
        return data + "," + rest;
    }
}
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------