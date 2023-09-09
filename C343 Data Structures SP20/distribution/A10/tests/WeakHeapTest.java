import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class WeakHeapTest {
    private WeakHeap wh1;

    @Before
    public void setUp () {
        wh1 = new WeakHeap();
        wh1.insert(new Item(30));
        wh1.insert(new Item(50));
        wh1.insert(new Item(40));
        wh1.insert(new Item(70));
        wh1.insert(new Item(80));
        wh1.insert(new Item(60));
        wh1.insert(new Item(170));
        wh1.insert(new Item(110));
        assertTrue(wh1.checkOrder());
        TreePrinter.print(wh1.findMin());
        /*
                                30_0
                                  └───────────────────┐
                                                    50_1
                                            ┌─────────┴─────────┐
                                          40_2                70_3
                                       ┌────┴────┐         ┌────┴────┐
                                     80_4      60_5      170_6     110_7
         */
    }

    @Test
    public void getParentIndex() throws RootE {
        assertEquals(70, wh1.getValue(wh1.getParentIndex(7)));
        assertEquals(70, wh1.getValue(wh1.getParentIndex(6)));
        assertEquals(40, wh1.getValue(wh1.getParentIndex(5)));
        assertEquals(40, wh1.getValue(wh1.getParentIndex(4)));
        assertEquals(50, wh1.getValue(wh1.getParentIndex(3)));
        assertEquals(50, wh1.getValue(wh1.getParentIndex(2)));
        assertEquals(30, wh1.getValue(wh1.getParentIndex(1)));
    }

    @Test
    public void getLeftChildIndex() throws NoLeftChildE {
        assertEquals(40, wh1.getValue(wh1.getLeftChildIndex(1)));
        assertEquals(80, wh1.getValue(wh1.getLeftChildIndex(2)));
        assertEquals(170, wh1.getValue(wh1.getLeftChildIndex(3)));
    }

    @Test
    public void getRightChildIndex() throws NoRightChildE {
        assertEquals(50, wh1.getValue(wh1.getRightChildIndex(0)));
        assertEquals(70, wh1.getValue(wh1.getRightChildIndex(1)));
        assertEquals(60, wh1.getValue(wh1.getRightChildIndex(2)));
        assertEquals(110, wh1.getValue(wh1.getRightChildIndex(3)));
    }

    @Test
    public void isLeftChild() throws RootE {
        assertTrue(wh1.isLeftChild(2));
        assertTrue(wh1.isLeftChild(4));
        assertTrue(wh1.isLeftChild(6));
        assertFalse(wh1.isLeftChild(1));
        assertFalse(wh1.isLeftChild(3));
        assertFalse(wh1.isLeftChild(5));
        assertFalse(wh1.isLeftChild(7));
    }

    @Test
    public void isRightChild() throws RootE {
        assertTrue(wh1.isRightChild(1));
        assertTrue(wh1.isRightChild(3));
        assertTrue(wh1.isRightChild(5));
        assertTrue(wh1.isRightChild(7));
        assertFalse(wh1.isRightChild(2));
        assertFalse(wh1.isRightChild(4));
        assertFalse(wh1.isRightChild(6));
    }

    @Test
    public void getDAncestorIndex() throws RootE {
        assertEquals(0, wh1.getDAncestorIndex(1));
        assertEquals(0, wh1.getDAncestorIndex(2));
        assertEquals(1, wh1.getDAncestorIndex(3));
        assertEquals(0, wh1.getDAncestorIndex(4));
        assertEquals(2, wh1.getDAncestorIndex(5));
        assertEquals(1, wh1.getDAncestorIndex(6));
        assertEquals(3, wh1.getDAncestorIndex(7));
    }

    @Test
    public void getLeftMostChildIndex() throws NoLeftChildE {
        assertEquals(4,wh1.getLeftMostChildIndex());
    }

    @Test
    public void swap() {
        assertEquals(50, wh1.getValue(1));
        assertEquals(40, wh1.getValue(2));
        wh1.swap(1,2);
        assertEquals(50, wh1.getValue(2));
        assertEquals(40, wh1.getValue(1));
    }

    @Test
    public void join() {
        assertTrue(wh1.join(0,4));

        wh1.getElem(4).setValue(10);
        assertFalse(wh1.join(0,4));
        assertEquals(10, wh1.getValue(0));
        assertEquals(50, wh1.getValue(1));
        assertEquals(40, wh1.getValue(2));
        assertEquals(70, wh1.getValue(3));
        assertEquals(30, wh1.getValue(4));
        assertEquals(60, wh1.getValue(5));
        assertEquals(170, wh1.getValue(6));
        assertEquals(110, wh1.getValue(7));

        wh1.getElem(2).setValue(5);
        assertFalse(wh1.join(0,2));
        assertEquals(5, wh1.getValue(0));
        assertEquals(50, wh1.getValue(1));
        assertEquals(10, wh1.getValue(2));
        assertEquals(70, wh1.getValue(3));
        assertEquals(30, wh1.getValue(4));
        assertEquals(60, wh1.getValue(5));
        assertEquals(170, wh1.getValue(6));
        assertEquals(110, wh1.getValue(7));
        assertEquals(1, wh1.getFlip(2));
    }

    @Test
    public void weakHeapify() {
        Random r = new Random(1);
        ArrayList<Item> items = new ArrayList<>();
        for (int i=0; i<14; i++) {
            items.add(new Item(r.nextInt(100)));
        }
        WeakHeap wh = new WeakHeap(items);
        TreePrinter.print(wh.findMin());
        assertEquals(4, wh.getValue(0));
        assertEquals(6, wh.getValue(1));
        assertEquals(48, wh.getValue(2));
        assertEquals(17, wh.getValue(3));
        assertEquals(78, wh.getValue(4));
        assertEquals(47, wh.getValue(5));
        assertEquals(34, wh.getValue(6));
        assertEquals(13, wh.getValue(7));
        assertEquals(85, wh.getValue(8));
        assertEquals(54, wh.getValue(9));
        assertEquals(69, wh.getValue(10));
        assertEquals(73, wh.getValue(11));
        assertEquals(88, wh.getValue(12));
        assertEquals(63, wh.getValue(13));
        assertEquals(1, wh.getFlip(2));
        assertEquals(1, wh.getFlip(3));
        assertEquals(1, wh.getFlip(4));
        assertEquals(1, wh.getFlip(5));
        assertTrue(wh.checkOrder());
    }

    @Test
    public void moveUp() {
        wh1.getElem(7).setValue(0);
        wh1.moveUp(7);
        assertEquals(0, wh1.getValue(0));
        assertEquals(30, wh1.getValue(1));
        assertEquals(40, wh1.getValue(2));
        assertEquals(50, wh1.getValue(3));
        assertEquals(80, wh1.getValue(4));
        assertEquals(60, wh1.getValue(5));
        assertEquals(170, wh1.getValue(6));
        assertEquals(70, wh1.getValue(7));
        assertEquals(1,wh1.getFlip(1));
        assertEquals(1,wh1.getFlip(3));
        assertTrue(wh1.checkOrder());

        wh1.getElem(4).setValue(2);
        wh1.moveUp(4);
        assertEquals(0, wh1.getValue(0));
        assertEquals(2, wh1.getValue(1));
        assertEquals(40, wh1.getValue(2));
        assertEquals(50, wh1.getValue(3));
        assertEquals(30, wh1.getValue(4));
        assertEquals(60, wh1.getValue(5));
        assertEquals(170, wh1.getValue(6));
        assertEquals(70, wh1.getValue(7));
        assertEquals(1,wh1.getFlip(1));
        assertEquals(1,wh1.getFlip(3));
        assertTrue(wh1.checkOrder());
    }

    @Test
    public void insert() {
        wh1.insert(new Item(10));
        assertTrue(wh1.checkOrder());
        wh1.insert(new Item(11));
        assertTrue(wh1.checkOrder());
        wh1.insert(new Item(12));
        assertTrue(wh1.checkOrder());
        wh1.insert(new Item(13));
        assertTrue(wh1.checkOrder());
        wh1.insert(new Item(14));
        assertTrue(wh1.checkOrder());
    }

    @Test
    public void extractMin() {
        WeakHeap wh = new WeakHeap();
        wh.insert(new Item(1));
        wh.insert(new Item(10));
        wh.insert(new Item(50));
        wh.insert(new Item(11));
        wh.insert(new Item(80));
        wh.insert(new Item(60));
        wh.insert(new Item(12));
        wh.insert(new Item(15));
        wh.insert(new Item(75));
        wh.insert(new Item(90));
        wh.insert(new Item(55));
        wh.insert(new Item(70));
        wh.insert(new Item(13));
        wh.insert(new Item(14));
        wh.insert(new Item(16));
        wh.insert(new Item(100));
        wh.checkOrder();

        wh.extractMin();
        assertEquals(10, wh.getValue(0));
        assertEquals(50, wh.getValue(1));
        assertEquals(75, wh.getValue(2));
        assertEquals(11, wh.getValue(3));
        assertEquals(80, wh.getValue(4));
        assertEquals(60, wh.getValue(5));
        assertEquals(12, wh.getValue(6));
        assertEquals(15, wh.getValue(7));
        assertEquals(100, wh.getValue(8));
        assertEquals(90, wh.getValue(9));
        assertEquals(55, wh.getValue(10));
        assertEquals(70, wh.getValue(11));
        assertEquals(13, wh.getValue(12));
        assertEquals(14, wh.getValue(13));
        assertEquals(16, wh.getValue(14));
        assertEquals(1,wh.getFlip(1));
        assertEquals(1,wh.getFlip(2));
        assertEquals(1,wh.getFlip(8));
    }
}