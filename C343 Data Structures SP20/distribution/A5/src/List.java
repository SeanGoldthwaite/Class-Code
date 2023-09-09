
class NoSuchElementE extends Exception {}

abstract class List<E> {
    abstract int length();
    abstract E getFirst() throws NoSuchElementE;
    abstract List<E> getRest() throws NoSuchElementE;

    static <E> List<E> fromArray (E[] elements) {
        List<E> result = new EmptyL<>();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = new NodeL<>(elements[i], result);
        }
        return result;
    }
}

class EmptyL<E> extends List<E> {
    int length() { return 0; }

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
