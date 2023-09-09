// Complete the implementation of this class

class SetList<E> implements SetI<E> {
    private List<E> elements;

    SetList () {
        elements = new EmptyL<>();
    }

    public void clear() {
        elements = new EmptyL<>();
    }

    public boolean isEmpty() {
        return elements instanceof EmptyL;
    }

    public void add(E elem) {
        if (!elements.contains(elem))
            elements = new NodeL<>(elem, elements);
    }

    public boolean contains(E elem) {
        return elements.contains(elem);
    }

    public int size() {
        return elements.length();
    }
}
