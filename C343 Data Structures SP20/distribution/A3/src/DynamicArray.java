import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;

public class DynamicArray<E> implements StackI<E>, QueueI<E>, DequeI<E> {

    private Optional<E>[] elements;
    private int capacity, front, back, size;
    //
    // data stored in locations:
    // front+1, front+2, ... back-2, back-1 (all mod capacity)
    //
    // common cases:
    // front points to the first filled location
    // back points to the last filled location
    // adding to front decreases 'front' by 1
    // adding to back increases 'back' by 1
    // removing does the opposite
    //
    // |-------------------------|
    // | 4 5 6 _ _ _ _ _ _ 1 2 3 |
    // |-------------------------|
    //       /\          /\      /\
    //      back        front  capacity
    //

    @SuppressWarnings("unchecked")
    DynamicArray (int initialCapacity) {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, initialCapacity);
        Arrays.fill(elements, Optional.empty());
        capacity = initialCapacity;
        front = 1;
        back = 0;
        size = 0;
    }
    @SuppressWarnings("unchecked")
    public void clear() {
        elements = (Optional<E>[]) Array.newInstance(Optional.class, capacity);
        Arrays.fill(elements, Optional.empty());
        front = 1;
        back = 0;
        size = 0;
    }
    @SuppressWarnings("unchecked")
    void doubleCapacity() {
        Optional<E>[] temp = (Optional<E>[]) Array.newInstance(Optional.class, capacity * 2);
        Arrays.fill(temp, Optional.empty());
        int index = front;
        for (int i = 0; i < size; i++) {
            temp[i] = elements[index++ % capacity];
        }
        /*while (size > 0) {
            temp[index++] = Optional.of(this.removeFirst());
        }*/
        capacity *= 2;
        front = 0;
        back = size - 1;
        elements = temp;
    }

    // Complete the definitions of the following methods from the interfaces

    public int size () {
	    return size;
    }

    //Stack methods
    public void push(E item) {
        this.addLast(item);
    }

    public E peek() throws NoSuchElementE {
        return this.getLast();
    }

    public E pop() throws NoSuchElementE {
        return this.removeLast();
    }

    //Queue Methods
    public void offer(E elem) {
        this.addFirst(elem);
    }

    public E poll() throws NoSuchElementE {
       return this.getLast();
    }

    public E remove() throws NoSuchElementE {
        return this.removeLast();
    }

    //Dequeue methods
    public void addFirst(E elem) {
        if (size == capacity)
            doubleCapacity();
        front = Math.floorMod(front - 1, capacity);
        elements[front] = Optional.of(elem);
        size++;
    }

    public void addLast(E elem) {
        if (size == capacity)
            doubleCapacity();
        back = (back + 1) % capacity;
        elements[back] = Optional.of(elem);
        size++;
    }

    public E getFirst() throws NoSuchElementE {
        if (elements[front].isPresent()) {
            return elements[front].get();
        }
        else {
            throw new NoSuchElementE();
        }
    }

    public E getLast() throws NoSuchElementE {
        if (elements[back].isPresent()) {
            return elements[back].get();
        }
        else {
            throw new NoSuchElementE();
        }
    }

    public E removeFirst() throws NoSuchElementE {
        if (elements[front].isPresent()) {
            E temp = elements[front].get();
            elements[front] = Optional.empty();
            front = Math.floorMod(++front, capacity);
            size--;
            return temp;
        }
        else {
            throw new NoSuchElementE();
        }
    }

    public E removeLast() throws NoSuchElementE {
        if (elements[back].isPresent()) {
            E temp = elements[back].get();
            elements[back] = Optional.empty();
            back = Math.floorMod(back - 1, capacity);
            size--;
            return temp;
        }
        else {
            throw new NoSuchElementE();
        }
    }

    // the following methods are used for debugging and testing;
    // please do not edit them

    Optional<E>[] getElements () { return elements; }

    int getCapacity () { return capacity; }

    int getFront () { return front; }

    int getBack () { return back; }
}
