

class NoSuchElementE extends Exception {}

// Interfaces for stack, queue, and deque
// Every operation is now expected to have (amortized) O(1) time complexity


interface StackI<E> {
    // removes all elements from the current stack
    void clear();

    // returns the number of elements in the current stack
    int size();

    // updates the current stack by adding the given element to the top
    void push (E item);

    // returns but does not remove the element at the top of the stack
    E peek() throws NoSuchElementE;

    // updates the current stack by removing the top element; return it as well
    E pop() throws NoSuchElementE;
}

interface QueueI<E> {
    // removes all elements from the current queue
    void clear();

    // returns the number of elements in the current queue
    int size();

    // updates the current queue by adding the given element of the end of the queue
    void offer(E elem);

    // returns but does not remove the element at the front of the queue
    E poll() throws NoSuchElementE;

    // updates the current queue by removing the element at the front; return it as well
    E remove() throws NoSuchElementE;
}

interface DequeI<E> {
    // removes all elements from the current dequeue
    void clear();

    // returns the number of elements in the current dequeue
    int size();

    // updates the current queue by adding the given element to the front of the queue
    void addFirst(E elem);

    // updates the current queue by adding the given element to the end of the queue
    void addLast(E elem);

    // returns but does not remove the element at the front
    E getFirst() throws NoSuchElementE;

    // returns but does not remove the element at the end
    E getLast() throws NoSuchElementE;

    // updates the current deque by removing the element at the front; return it as well
    E removeFirst() throws NoSuchElementE;

    // updates the current deque by removing the element at the end; return it as well
    E removeLast() throws NoSuchElementE;

}
