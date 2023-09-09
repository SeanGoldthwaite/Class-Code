
// Do not modify this file

// -------------------------------------------

abstract class List<E> {
    abstract int length();
    abstract boolean isEmpty();
    abstract List<E> addLast(E elem);
    abstract E getFirst() throws NoSuchElementE;
    abstract List<E> getRest() throws NoSuchElementE;
    abstract E getLast() throws NoSuchElementE;
    abstract boolean contains (E elem);
}

// -------------------------------------------

class EmptyL<E> extends List<E> {
    int length() { return 0; }

    boolean isEmpty () { return true; }

    List<E> addLast(E elem) {
        return new NodeL<>(elem, this);
    }

    E getFirst() throws NoSuchElementE {
        throw new NoSuchElementE();
    }

    List<E> getRest() throws NoSuchElementE {
        throw new NoSuchElementE();
    }

    E getLast() throws NoSuchElementE {
        throw new NoSuchElementE();
    }

    boolean contains(E elem) {
        return false;
    }
}


// -------------------------------------------


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

    List<E> addLast(E elem) {
        return new NodeL<>(data, rest.addLast(elem));
    }

    E getFirst() {
        return data;
    }

    List<E> getRest() {
        return rest;
    }

    E getLast() throws NoSuchElementE {
        if (rest.isEmpty()) { return data; }
        else return rest.getLast();
    }

    boolean contains(E elem) {
        return (data.equals(elem)) || rest.contains(elem);
    }
}


// -------------------------------------------
// -------------------------------------------
