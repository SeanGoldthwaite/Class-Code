// Complete the implementation of this class
// For this implementation, you can make some of the methods in the interface take O(n) time instead of the expected O(1) time


class QueueList<E> implements QueueI<E> {
    private List<E> elements;

    QueueList() {
        elements = new EmptyL<>();
    }

    public void clear() {
        elements = new EmptyL<>();
    }

    public void offer(E elem) {
        elements = elements.addLast(elem);
    }

    public E poll() throws NoSuchElementE {
        return elements.getFirst();
    }

    public E remove() throws NoSuchElementE {
        E temp = elements.getFirst();
        int length = elements.length();
        if (length > 2)
            elements = new NodeL<>(elements.getRest().getFirst(), elements.getRest().getRest());
        else if (length == 2)
            elements = new NodeL<>(elements.getRest().getFirst(), new EmptyL<>());
        else
            elements = new EmptyL<>();
        return temp;
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
