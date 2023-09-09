import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueueListTest {

    private int rand;
    private QueueList<Integer> empty, one, two, three, four, five;

    @Before
    public void setUp() throws Exception {
        empty = new QueueList<>();
        one = new QueueList<>();
        two = new QueueList<>();
        three = new QueueList<>();
        four = new QueueList<>();
        five = new QueueList<>();
        for (int i = 0; i < 10000; i++) {
            rand = (int)(Math.random() * Integer.MAX_VALUE);
            one.offer(rand);
            two.offer(rand);
            three.offer(rand);
            three.remove();
        }
        four.offer(7);
        four.offer(10);
        four.offer(11);
        four.offer(14);
        four.offer(16);
        five.offer(7);
        five.offer(10);
        five.offer(11);
        five.offer(14);
        five.offer(16);
    }

    @After
    public void tearDown() throws Exception {
        empty = null;
        one = null;
        two = null;
        three = null;
        four = null;
        five = null;
    }

    @Test
    public void clear() {
        five.clear();
        assertEquals(empty, five);
        assertEquals(five, new QueueList<>());
        one.clear();
        two.clear();
        assertEquals(one, two);
    }

    @Test
    public void offer() {
    }

    @Test
    public void poll() throws NoSuchElementE{
        assertEquals(four.poll(), five.poll());
        assertEquals(one.poll(), two.poll());
        assertNotEquals(four.poll(), one.poll());
    }
    @Test (expected = NoSuchElementE.class)
    public void pollEror() throws NoSuchElementE {
        empty.poll();
        three.poll();
    }

    @Test
    public void remove() throws NoSuchElementE {
        assertEquals(four, five);
        four.remove();
        assertNotEquals(four, five);
        five.remove();
        assertEquals(four, five);
        four.remove();
        four.remove();
        four.remove();
        four.remove();
        assertEquals(three, four);
    }
    @Test (expected = NoSuchElementE.class)
    public void removeError() throws NoSuchElementE {
        four.remove();
        four.remove();
        four.remove();
        four.remove();
        four.remove();
        four.remove();
    }

    @Test
    public void size() {
        assertEquals(0, empty.size());
        assertEquals(10000, one.size());
        assertEquals(10000, two.size());
        assertEquals(0, three.size());
        assertEquals(5, four.size());
        assertEquals(5, five.size());
    }
}