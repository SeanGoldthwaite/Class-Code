// Complete the implementation of this class
// For this implementation all the methods should take amortized O(1) time

class QueueTwoStacks<E> implements QueueI<E> {
    private StackList<E> front, back;

    QueueTwoStacks () {
        front = new StackList<>();
        back = new StackList<>();
    }

    public void clear() {

    }

    public void offer(E elem) {

    }

    public E poll() throws NoSuchElementE {
        return null;
    }

    public E remove() throws NoSuchElementE {
        return null;
    }

    public int size() {
        return 0;
    }
}
