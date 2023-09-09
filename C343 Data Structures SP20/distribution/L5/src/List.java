
class NoSuchElementE extends Exception {}

abstract class List<E> {
    abstract int length();
    abstract boolean isEmpty();
    abstract E getFirst() throws NoSuchElementE;
    abstract List<E> getRest() throws NoSuchElementE;
}

class EmptyL<E> extends List<E> {
    int length() { return 0; }

    boolean isEmpty () { return true; }

    E getFirst() throws NoSuchElementE {
        throw new NoSuchElementE();
    }

    List<E> getRest() throws NoSuchElementE {
        throw new NoSuchElementE();
    }

    public String toString () {
        return "_";
    }
}

class NodeL<E> extends List<E> {
    private E data;
    private List<E> rest;

    NodeL (E data, List<E> rest) {
        this.data = data;
        this.rest = rest;
    }

    int length() {
        return 1 + rest.length();
    }

    boolean isEmpty () { return false; }

    E getFirst() {
        return data;
    }

    List<E> getRest() {
        return rest;
    }

    public String toString () {
        return data + ", " + rest.toString();
    }
}
