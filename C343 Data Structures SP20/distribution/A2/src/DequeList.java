// Complete the implementation of this class

class DequeList<E> implements DequeI<E> {
    private List<E> elements;

    DequeList () {
        elements = new EmptyL<>();
    }

    public void clear() {
        elements = new EmptyL<>();
    }

    public void addFirst(E elem) {
        elements = new NodeL<>(elem, elements);
    }

    public void addLast(E elem) {
        elements = elements.addLast(elem);
    }

    public E getFirst() throws NoSuchElementE {
        return elements.getFirst();
    }

    public E getLast() throws NoSuchElementE {
        return elements.getLast();
    }

    public int size() {
        return elements.length();
    }
    public boolean equals(Object other) {
        return this.toString().equals(other.toString());
    }
    public String toString() {
        return elements.toString();
    }
}

