import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkedListTest {

    private int rand;
    private LinkedList<Integer> empty, one, two, three, four, five, six, seven;

    @Before
    public void setUp() throws Exception {
        empty = new LinkedList();
        one = new LinkedList<>();
        two = new LinkedList<>();
        three = new LinkedList<>();
        four = new LinkedList<>();
        five = new LinkedList<>();
        six = new LinkedList<>();
        seven = new LinkedList<>();
        for (int i = 0; i < 10000; i++) {
            rand = (int)(Math.random() * Integer.MAX_VALUE);
            one.addFirst(rand);
            two.addLast(rand);
            three.addFirst(rand);
            three.removeFirst();
        }
        four.addFirst(7);
        four.addFirst(10);
        four.addFirst(11);
        four.addFirst(14);
        four.addFirst(16);
        five.addFirst(7);
        five.addFirst(10);
        five.addFirst(11);
        five.addFirst(14);
        five.addFirst(16);
        six.addLast(16);
        six.addLast(14);
        six.addLast(11);
        six.addLast(10);
        six.addLast(7);
        seven.addLast(11);
        seven.addFirst(14);
        seven.addLast(10);
        seven.addLast(7);
        seven.addFirst(16);
    }

    @After
    public void tearDown() throws Exception {
        empty = null;
        one = null;
        two = null;
        three = null;
        four = null;
        five = null;
        six = null;
        seven = null;
    }

    @Test
    public void clear() {
        five.clear();
        assertEquals(empty, five);
        assertEquals(five, new LinkedList<>());
        six.clear();
        assertEquals(five, six);
        one.clear();
        two.clear();
        assertEquals(one, two);
    }

    @Test
    public void size() {
        assertEquals(0, empty.size());
        assertEquals(0, three.size());
        assertEquals(10000, one.size());
        assertEquals(10000, two.size());
        assertEquals(four, five);
        assertEquals(four, six);
        assertEquals(five, six);
    }

    @Test
    public void addFirst() {
        assertEquals(five, six);
        five.addFirst(100);
        six.addFirst(100);
        assertEquals(five, six);
        assertNotEquals(four, six);
        assertNotEquals(four, five);
        four.addFirst(100);
        assertEquals(four, six);
    }

    @Test
    public void addLast() {
        assertEquals(five, six);
        five.addLast(100);
        six.addLast(100);
        assertEquals(five, six);
        assertNotEquals(four, six);
        assertNotEquals(four, five);
        four.addLast(100);
        assertEquals(four, six);
    }

    @Test
    public void getFirst() throws NoSuchElementE{
        assertEquals(two.getFirst(), one.getLast());
        assertEquals(two.getLast(), one.getFirst());
        assertEquals(four.getFirst(), five.getFirst());
        assertEquals(four.getFirst(), six.getFirst());
        assertEquals(four.getFirst(), seven.getFirst());
    }
    @Test (expected = NoSuchElementE.class)
    public void getFirstError() throws NoSuchElementE {
        empty.getFirst();
        three.getFirst();
    }

    @Test
    public void getLast() throws NoSuchElementE {
        assertEquals((Integer)7, four.getLast());
        assertEquals((Integer)7, five.getLast());
        assertEquals((Integer)7, six.getLast());
        assertEquals(four.getLast(), five.getLast());
        assertEquals(four.getLast(), six.getLast());
        assertEquals(five.getLast(), six.getLast());
    }
    @Test (expected = NoSuchElementE.class)
    public void getLastError() throws NoSuchElementE {
        empty.getLast();
    }

    @Test
    public void removeFirst() throws NoSuchElementE {
        assertEquals(five, six);
        five.removeFirst();
        six.removeFirst();
        assertEquals(five, six);
        assertNotEquals(four, six);
        assertNotEquals(four, five);
        four.removeFirst();
        assertEquals(four, six);
        assertNotEquals(one.removeFirst(), four.removeFirst());
    }
    @Test (expected = NoSuchElementE.class)
    public void removeFirstError() throws NoSuchElementE {
        empty.removeFirst();
        three.getFirst();
        four.removeFirst();
        four.removeFirst();
        four.removeFirst();
        four.removeFirst();
        four.removeFirst();
        four.removeFirst();
    }
}