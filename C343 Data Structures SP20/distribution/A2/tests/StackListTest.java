import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StackListTest {

    private int rand;
    private StackList<Integer> empty, one, two, three, four, five;

    @Before
    public void setUp() throws Exception {
        empty = new StackList<>();
        one = new StackList<>();
        two = new StackList<>();
        three = new StackList<>();
        four = new StackList<>();
        five = new StackList<>();
        for (int i = 0; i < 10000; i++) {
            rand = (int)(Math.random() * Integer.MAX_VALUE);
            one.push(rand);
            two.push(rand);
            three.push(rand);
            three.pop();
        }
        four.push(7);
        four.push(10);
        four.push(11);
        four.push(14);
        four.push(16);
        five.push(7);
        five.push(10);
        five.push(11);
        five.push(14);
        five.push(16);
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
        assertEquals(five, new StackList<>());
        one.clear();
        two.clear();
        assertEquals(one, two);
    }

    @Test
    public void empty() {
        assertTrue(empty.empty());
        assertTrue(three.empty());
        assertFalse(one.empty());
        assertFalse(two.empty());
        assertFalse(four.empty());
        assertFalse(five.empty());
    }

    @Test
    public void peek() throws NoSuchElementE {
        assertEquals(four.peek(), five.peek());
        assertEquals(one.peek(), two.peek());
        assertNotEquals(one.peek(), four.peek());
    }
    @Test (expected = NoSuchElementE.class)
    public void peekError() throws NoSuchElementE{
        empty.peek();
        three.pop();
    }

    @Test
    public void pop() throws NoSuchElementE {
        assertEquals(four.pop(), five.pop());
        assertEquals(one.pop(), two.pop());
    }
    @Test (expected = NoSuchElementE.class)
    public void popError() throws NoSuchElementE{
        empty.pop();
        three.pop();
    }

    @Test
    public void push() {
        four.push(10);
        assertNotEquals(four, five);
        five.push(10);
        assertEquals(four, five);
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