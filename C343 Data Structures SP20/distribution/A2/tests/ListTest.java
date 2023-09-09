import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ListTest {
    private Random rand;
    private int len;
    private List<Integer> empty, one, three, many;


    @Before
    public void setUp() {
        rand = new Random(1);
        empty = new EmptyL<>();
        one = new NodeL<>(5, empty);
        three = new NodeL<>(10, new NodeL<>(20, new NodeL<>(30, empty)));
        len = 4999;
        many = new EmptyL<>();
        for (int i=0; i<len; i++) {
            many = new NodeL<>(rand.nextInt(), many);
        }
    }

    @After
    public void tearDown() {
        rand = null;
        empty = null;
        one = null;
        three = null;
        len = 0;
        many = null;
    }

    @Test
    public void length() {
        assertEquals(0, empty.length());
        assertEquals(1, one.length());
        assertEquals(3, three.length());
        assertEquals(len, many.length());
    }

    @Test
    public void isEmpty() {
        assertTrue(empty.isEmpty());
        assertFalse(one.isEmpty());
        assertFalse(three.isEmpty());
    }

    @Test
    public void addLast() {
        assertEquals(1, empty.addLast(7).length());
        assertEquals(2, one.addLast(7).length());
        assertEquals(4, three.addLast(7).length());
        assertEquals(len+1, many.addLast(7).length());
    }

    @Test
    public void getFirst() throws NoSuchElementE {
        assertEquals(5, (int) one.getFirst());
        assertEquals(10, (int) three.getFirst());
    }

    @Test(expected=NoSuchElementE.class)
    public void getFirstE() throws NoSuchElementE {
        empty.getFirst();
    }

    @Test(timeout=1)
    public void getFirstT() throws NoSuchElementE {
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
        many.getFirst();
    }

    @Test
    public void getRest() throws NoSuchElementE {
        assertEquals(0, one.getRest().length());
        assertEquals(2, three.getRest().length());
        assertEquals(len-1, many.getRest().length());
    }

    @Test(expected = NoSuchElementE.class)
    public void getRestE() throws NoSuchElementE {
        empty.getRest();
    }

    @Test
    public void getLast() throws NoSuchElementE {
        assertEquals(5, (int) one.getLast());
        assertEquals(30, (int) three.getLast());
        assertEquals(87, (int) many.addLast(87).getLast());
    }

    @Test(expected = NoSuchElementE.class)
    public void getLastE() throws NoSuchElementE {
        empty.getLast();
    }

    @Test
    public void contains() {
        assertFalse(empty.contains(3));
        assertFalse(one.contains(3));
        assertTrue(one.contains(5));
        assertTrue(many.addLast(87).contains(87));
    }
}