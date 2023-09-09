import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;

public class DynamicArrayTest {
    private DynamicArray<Integer> d;

    @Before
    public void setUp() throws Exception {
        d = new DynamicArray<>(5);
    }

    @After
    public void tearDown() throws Exception {
        d = null;
    }

    @Test
    public void size() {
        assertEquals(0, d.size());
    }

    @Test (expected = NoSuchElementE.class)
    public void dequeueError() throws NoSuchElementE{
        d.removeFirst();
        d.removeLast();
    }
    @Test
    public void dequeNoResize() throws NoSuchElementE {
        assertEquals(0, d.size());
        d.addLast(1);
        d.addFirst(2);
        d.addLast(3);
        d.addFirst(4);
        assertEquals(4, (int) d.removeFirst());
        assertEquals(3, (int) d.removeLast());
        assertEquals(2, (int) d.removeFirst());
        assertEquals(1, (int) d.removeLast());
        assertEquals(0, d.size());


        //tests wrapping
        assertEquals(0, d.size());
        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        d.addFirst(4);
        assertEquals(1, (int) d.removeLast());
        assertEquals(2, (int) d.removeLast());
        assertEquals(3, (int) d.removeLast());

        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        assertEquals(4, (int) d.getLast());
        assertEquals(4, (int) d.removeLast());
        assertEquals(1, (int) d.removeLast());
        assertEquals(2, (int) d.removeLast());

        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        assertEquals(3, (int) d.getLast());
        assertEquals(3, (int) d.removeLast());
        assertEquals(1, (int) d.removeLast());
        assertEquals(2, (int) d.removeLast());

        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        assertEquals(3, (int) d.getLast());
        assertEquals(3, (int) d.removeLast());
        assertEquals(1, (int) d.removeLast());
        assertEquals(2, (int) d.removeLast());

        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);
        assertEquals(3, (int) d.getLast());
        assertEquals(3, (int) d.getFirst());
        assertEquals(3, (int) d.removeLast());
        assertEquals(1, (int) d.removeLast());
        assertEquals(2, (int) d.removeLast());
        assertEquals(3, (int) d.getLast());
        assertEquals(3, (int) d.getFirst());
        assertEquals(3, (int) d.removeLast());
        assertEquals(0, d.size());
    }

    @Test
    public void dequeResize() throws NoSuchElementE {
        d.addLast(1);
        d.addLast(2);
        d.addFirst(3);
        d.addLast(4);
        d.addFirst(5);
        assertEquals(5, d.getCapacity());
        d.addFirst(6);
        assertEquals(10, d.getCapacity());
        d.addLast(7);
        d.addLast(8);
        d.addLast(9);
        d.addFirst(10);
        assertEquals(10, d.getCapacity());
        d.addLast(11);
        assertEquals(20, d.getCapacity());
        assertEquals(10, (int) d.getFirst());
        assertEquals(11, (int) d.getLast());
        assertEquals(10, (int) d.removeFirst());
        assertEquals(6, (int) d.getFirst());
        assertEquals(11, (int) d.getLast());
        assertEquals(6, (int) d.removeFirst());
        assertEquals(5, (int) d.removeFirst());
        assertEquals(3, (int) d.removeFirst());
        assertEquals(1, (int) d.removeFirst());
        assertEquals(2, (int) d.removeFirst());
        assertEquals(4, (int) d.removeFirst());
        assertEquals(7, (int) d.removeFirst());
        assertEquals(8, (int) d.removeFirst());
        assertEquals(9, (int) d.removeFirst());
        assertEquals(11, (int) d.removeFirst());
        assertEquals(0, d.size());
    }
    @Test
    public void stackNoResize() throws NoSuchElementE{
        assertEquals(0, d.size());
        d.push(1);
        d.push(2);
        d.push(3);
        d.push(4);
        assertEquals(4, (int) d.pop());
        assertEquals(3, (int) d.pop());
        assertEquals(2, (int) d.pop());
        assertEquals(1, (int) d.pop());
        assertEquals(0, d.size());


        assertEquals(0, d.size());
        d.push(1);
        d.push(2);
        d.push(3);
        d.push(4);
        assertEquals(4, (int) d.peek());
        assertEquals(4, d.size());
        assertEquals(4, (int) d.pop());
        assertEquals(3, (int) d.peek());
        assertEquals(3, (int) d.pop());
        assertEquals(2, (int) d.peek());
        assertEquals(2, (int) d.pop());
        assertEquals(1, d.size());
    }
    @Test (expected = NoSuchElementE.class)
    public void stackError() throws NoSuchElementE{
        assertEquals(0, d.size());
        d.peek();
        d.pop();
    }
    @Test
    public void stackResize() throws NoSuchElementE{
        d.push(1);
        d.push(2);
        d.push(3);
        d.push(4);
        d.push(5);
        assertEquals(5, d.getCapacity());
        d.push(6);
        assertEquals(10, d.getCapacity());
        assertEquals(6, (int) d.peek());
        d.push(7);
        d.push(8);
        d.push(9);
        d.push(10);
        assertEquals(10, d.getCapacity());
        d.push(11);
        assertEquals(20, d.getCapacity());
        assertEquals(11, (int) d.peek());
        assertEquals(11, (int) d.pop());
        assertEquals(10, (int) d.pop());
        assertEquals(9, (int) d.pop());
        assertEquals(8, (int) d.pop());
        assertEquals(7, (int) d.pop());
        assertEquals(6, (int) d.pop());
        assertEquals(5, (int) d.pop());
        assertEquals(4, (int) d.pop());
        assertEquals(3, (int) d.pop());
        assertEquals(2, (int) d.pop());
        assertEquals(1, (int) d.pop());
        assertEquals(0, d.size());
    }
    @Test
    public void queueNoResize() throws NoSuchElementE {
        assertEquals(0, d.size());
        d.offer(1);
        d.offer(2);
        d.offer(3);
        d.offer(4);
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());
        assertEquals(3, (int) d.remove());
        assertEquals(4, (int) d.remove());
        assertEquals(0, d.size());


        //tests wrapping
        assertEquals(0, d.size());
        d.offer(1);
        d.offer(2);
        d.offer(3);
        d.offer(4);
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());
        assertEquals(3, (int) d.remove());

        d.offer(1);
        d.offer(2);
        d.offer(3);
        assertEquals(4, (int) d.poll());
        assertEquals(4, (int) d.remove());
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());

        d.offer(1);
        d.offer(2);
        d.offer(3);
        assertEquals(3, (int) d.poll());
        assertEquals(3, (int) d.remove());
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());

        d.offer(1);
        d.offer(2);
        d.offer(3);
        assertEquals(3, (int) d.poll());
        assertEquals(3, (int) d.remove());
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());

        d.offer(1);
        d.offer(2);
        d.offer(3);
        assertEquals(3, (int) d.poll());
        assertEquals(3, (int) d.remove());
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());
        assertEquals(3, (int) d.poll());
        assertEquals(3, (int) d.remove());
        assertEquals(0, d.size());
    }
    @Test (expected = NoSuchElementE.class)
    public void queueError() throws NoSuchElementE {
        d.poll();
        d.remove();
    }
    @Test
    public void queueResize() throws NoSuchElementE {
        d.offer(1);
        d.offer(2);
        d.offer(3);
        d.offer(4);
        d.offer(5);
        assertEquals(5, d.getCapacity());
        d.offer(6);
        assertEquals(10, d.getCapacity());
        assertEquals(1, (int) d.poll());
        d.offer(7);
        d.offer(8);
        d.offer(9);
        d.offer(10);
        assertEquals(10, d.getCapacity());
        d.offer(11);
        assertEquals(20, d.getCapacity());
        assertEquals(1, (int) d.poll());
        assertEquals(1, (int) d.remove());
        assertEquals(2, (int) d.remove());
        assertEquals(3, (int) d.remove());
        assertEquals(4, (int) d.remove());
        assertEquals(5, (int) d.remove());
        assertEquals(6, (int) d.remove());
        assertEquals(7, (int) d.remove());
        assertEquals(8, (int) d.remove());
        assertEquals(9, (int) d.remove());
        assertEquals(10, (int) d.remove());
        assertEquals(11, (int) d.remove());
        assertEquals(0, d.size());
    }
}
