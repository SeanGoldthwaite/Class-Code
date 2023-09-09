import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;
import static org.junit.Assert.*;

public class HeapTest {

    BinaryHeap bhp;

    @Before
    public void sortBH () {
        ArrayList<Item> items = new ArrayList<>();

        Item it;

        it = new Item("a1",  7);
        items.add(it);

        it = new Item("a2",  2);
        items.add(it);

        it = new Item("a3",  8);
        items.add(it);

        it = new Item("a4",  3);
        items.add(it);

        it = new Item("a5",  6);
        items.add(it);

        it = new Item("a6",  9);
        items.add(it);

        it = new Item("a7",  10);
        items.add(it);

        it = new Item("a8",  4);
        items.add(it);

        it = new Item("a9",  5);
        items.add(it);

        bhp = new BinaryHeap(items);
        TreePrinter.print(bhp.findMin());
    }
    @After
    public void tearDown() {
        bhp = null;

        for (int i = 1; i < 10; i++) assertEquals(i, bhp.extractMin().getValue());
    }

    @Test
    public void extractMin() {
        for (int i = 1; i <= 9; i++) assertEquals(i + 1, bhp.extractMin().getValue());
    }
    /*@Test
    public void getLeftChildIndex() throws NoLeftChildE {
        for (int i = 0; i <= 3; i++) {
            assertEquals((i * 2) + 1, bhp.getLeftChildIndex(i));
        }
    }*/
    @Test (expected = NoLeftChildE.class)
    public void getLeftCildIndexError() throws NoLeftChildE {
        for (int i = 4; i < bhp.getSize(); i++) {
            bhp.getLeftChildIndex(i);
        }
    }
    /*@Test
    public void getRightChildIndex() throws NoRightChildE{
        for (int i = 0; i <= 3; i++) {
            assertEquals((i * 2) + 2, bhp.getRightChildIndex(i));
        }
    }*/
    @Test (expected = NoRightChildE.class)
    public void getRightCildIndexError() throws NoRightChildE{
        for (int i = 4; i < bhp.getSize(); i++) {
            bhp.getRightChildIndex(i);
        }
    }
    @Test
    public void getParentIndex() throws NoParentE{
        for (int i = 1; i < bhp.getSize(); i++) {
            assertEquals((i / 2), bhp.getParentIndex(i));
        }
    }
    @Test (expected = NoParentE.class)
    public void getParentIndexError() throws NoParentE {
        bhp.getParentIndex(0);
    }
}
