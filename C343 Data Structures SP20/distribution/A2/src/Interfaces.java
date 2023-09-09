

// Do not modify this file

class NoSuchElementE extends Exception {}

// -------------------------------------------
// Interfaces for linked list, stack, queue, deque, set
// -------------------------------------------

interface LinkedListI<E> {
    // removes all elements from the current list
    // expected O(1)
    void clear ();

    // returns the number of elements in the current list
    int size();

    // updates the current list by adding the given element to the front
    // expected O(1)
    void addFirst (E elem);

    // updates the current list by adding the given element to the end
    // expected O(n)
    void addLast (E elem);

    // returns but does not remove the element at the front of the current list
    // expected O(1)
    E getFirst() throws NoSuchElementE;

    // returns but does not remove the element at the end of the current list
    // expected O(n)
    E getLast() throws NoSuchElementE;

    // updates the current list by removing the element at the front
    // expected O(1)
    E removeFirst() throws NoSuchElementE;
}

// -------------------------------------------

interface StackI<E> {
    // removes all elements from the current stack
    // expected O(1)
    void clear();

    // checks if the current stack is empty
    // expected O(1)
    boolean empty();

    // returns but does not remove the element at the top of the stack
    // expected O(1)
    E peek() throws NoSuchElementE;

    // updates the current stack by removing the top element
    // expected O(1)
    E pop() throws NoSuchElementE;

    // updates the current stack by adding the given element to the top
    // expected O(1)
    void push (E item);

    // returns the number of elements in the current queue
    int size();
}

// -------------------------------------------

interface QueueI<E> {
    // removes all elements from the current queue
    void clear();

    // updates the current queue by adding the given element of the end of the queue
    // expected O(1)
    void offer(E elem);

    // returns but does not remove the element at the front of the queue
    // expected O(1)
    E poll() throws NoSuchElementE;

    // updates the current queue by removing the element at the front
    // expected O(1)
    E remove() throws NoSuchElementE;

    // returns the number of elements in the current queue
    int size();

}

// -------------------------------------------

interface DequeI<E> {
    // removes all elements from the current dequeue
    // expected O(1)
    void clear();

    // updates the current queue by adding the given element to the front of the queue
    // ideally expected O(1) but for this assignment not required
    void addFirst(E elem);

    // updates the current queue by adding the given element to the end of the queue
    // ideally expected O(1) but for this assignment not required
    void addLast(E elem);

    // returns but does not remove the element at the front
    // ideally expected O(1) but for this assignment not required
    E getFirst() throws NoSuchElementE;

    // returns but does not remove the element at the end
    // ideally expected O(1) but for this assignment not required
    E getLast() throws NoSuchElementE;

    // returns the number of elements in the current dequeue
    int size();
}

// -------------------------------------------

interface SetI<E> {
    // removes all elements from the current set
    void clear();

    // checks if the current set is empty
    boolean isEmpty();

    // updates the current set by adding the given element (if not present)
    // can do better than O(n) but not required for this assignment
    void add(E elem);

    // checks if the current set contains the given element
    // can do better than O(n) but not required for this assignment
    boolean contains (E elem);

    // returns the number of elements in the current set
    int size();
}

// -------------------------------------------
// -------------------------------------------
