import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BSTTest {
    BST bst;

    @Before
    public void setUp() {
        List<Integer> nums = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        Collections.shuffle(nums);
        bst = BST.EBST;
        for (int i : nums) bst = bst.BSTinsert(i);
    }

    @Test
    public void BSTLeaf () throws EmptyBSTE {
        bst = BST.BSTLeaf(3);
        assertEquals(3,bst.BSTData());
        assertTrue(bst.BSTLeft().isEmpty());
        assertTrue(bst.BSTRight().isEmpty());
        assertEquals(1, bst.BSTHeight());
        assertFalse(bst.isEmpty());
        Iterator<Integer> iter = bst.iterator();
        assertTrue(iter.hasNext());
        assertEquals(3, (int)iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void BSTfind() {
        bst = bst.BSTinsert(100);
        bst = bst.BSTinsert(-100);
        assertFalse(bst.BSTfind(1000));
        assertTrue(bst.BSTfind(100));
        assertTrue(bst.BSTfind(-100));
    }

    @Test
    public void BSTinsert() {
        Iterator<Integer> iter = bst.iterator();
        for (int i=0; i<100; i++) assertEquals(i,(int)iter.next());
    }

    @Test
    public void BSTdelete() throws EmptyBSTE {
        int d = bst.BSTData();
        BST smallerBST = bst.BSTdelete(d);
        assertFalse(smallerBST.BSTfind(d));
        int leftMost = bst.BSTRight().BSTdeleteLeftMostLeaf().getFirst();
        assertEquals(leftMost,(int)smallerBST.BSTData());
    }

    @Test
    public void BSTdeleteLeftMostLeaf() throws EmptyBSTE {
        bst = bst.BSTinsert(-100);
        assertTrue(bst.BSTfind(-100));
        Pair<Integer,BST> intBSTPair = bst.BSTdeleteLeftMostLeaf();
        assertEquals(-100,(int)intBSTPair.getFirst());
        assertFalse(intBSTPair.getSecond().BSTfind(-100));
    }

    @Test
    public void doc () throws EmptyBSTE, EmptyAVLE {

        // ideal tree

        bst = BST.BSTLeaf(10).BSTinsert(5).BSTinsert(15)
                .BSTinsert(2).BSTinsert(7).BSTinsert(12).BSTinsert(17)
                .BSTinsert(1).BSTinsert(3).BSTinsert(6).BSTinsert(8)
                .BSTinsert(11).BSTinsert(13).BSTinsert(16).BSTinsert(18);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        // badly unbalanced tree

        bst = BST.BSTLeaf(10).BSTinsert(15).BSTinsert(17).BSTinsert(18);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        // Easy right demo

        bst = BST.BSTLeaf(40).BSTinsert(20).BSTinsert(50).BSTinsert(10).BSTinsert(30);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        bst = bst.BSTinsert(15);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        AVL avl;

        avl = AVL.AVLLeaf(40).AVLinsert(20).AVLinsert(50).AVLinsert(10).AVLinsert(30).AVLinsert(15);
        System.out.printf("%n%n%n");
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        // rotate right demo

        bst = BST.BSTLeaf(40).BSTinsert(20).BSTinsert(50).BSTinsert(10).BSTinsert(30).BSTinsert(25);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        bst = BST.BSTLeaf(20).BSTinsert(10).BSTinsert(40).BSTinsert(30).BSTinsert(50).BSTinsert(25);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        bst = BST.BSTLeaf(40).BSTinsert(30).BSTinsert(50).BSTinsert(20).BSTinsert(25).BSTinsert(10);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        avl = AVL.AVLLeaf(40).AVLinsert(20).AVLinsert(50).AVLinsert(10).AVLinsert(30).AVLinsert(25);
        System.out.printf("%n%n%n");
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        // delete demo easy

        bst = BST.BSTLeaf(30).BSTinsert(20).BSTinsert(40).BSTinsert(10).BSTinsert(25).BSTinsert(50);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        bst = BST.BSTLeaf(25).BSTinsert(20).BSTinsert(40).BSTinsert(10).BSTinsert(50);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        // delete demo hard

        bst = BST.BSTLeaf(35).BSTinsert(20).BSTinsert(40)
                .BSTinsert(7).BSTinsert(30).BSTinsert(50)
                .BSTinsert(5).BSTinsert(10);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");

        bst = BST.BSTLeaf(30).BSTinsert(20).BSTinsert(40)
                .BSTinsert(7).BSTinsert(50)
                .BSTinsert(5).BSTinsert(10);
        System.out.printf("%n%n%n");
        TreePrinter.print(bst);
        System.out.printf("%n%n%n");
    }


    @Test(timeout = 500)

    public void BSTiteratorTest() {
        String dep = "\nNote: This test depends on BSTinsert()";
        BST bst = new EmptyBST().BSTinsert(2).BSTinsert(1).BSTinsert(3);
        Iterator<Integer> iter = bst.iterator();
        String err = "After adding 2, 1, and 3, ";
        try {
            assertEquals(err + "got an incorrect item after calling .next()" + dep, 1, (int) iter.next());
            assertEquals(err + "got an incorrect item after calling .next() twice" + dep, 2, (int) iter.next());
            assertEquals(err + "got an incorrect item after calling .next() thrice" + dep, 3, (int) iter.next());
        } catch (NoSuchElementException e) {
            fail(err + "next() should not throw any expection when called 3 times when a BST has 3 items.");
        }
        //inserting 50 random numbers that are close to each other, then iterating over them.
        int n = 50;
        int l = -20;
        int g = 20;
        bst = new EmptyBST();
        List<Integer> nums = new ArrayList<>(n);
        List<Integer> numc = new ArrayList<>(n);
        Random rand = new Random();
        for (int i = 0; i < n; ++i) {
            int r = l + rand.nextInt(g - l);
            nums.add(r);
            numc.add(r);
        }
        for (int i = 0; i < n; ++i) {
            bst = bst.BSTinsert(nums.get(i));
        }
        Collections.sort(numc);
        iter = bst.iterator();
        List<Integer> currNums = new ArrayList<>(n);
        for (int i = 0; i < numc.size(); ++i) {
            int curr = iter.next();
            currNums.add(curr);
            if (curr != numc.get(i)) {
                err = "After inserting the following numbers in an empty tree:\n";
                err += nums.toString() + "\nand after calling .next() " + (i + 1) + " times, expected the following:\n";
                err += firstN(numc, i + 1).toString() + "\nbut got the following:\n";
                err += firstN(currNums, i + 1).toString() + dep;
                fail(err);
            }
        }
        assertFalse("Iterator's hasNext() doesn't return false when all items are traversed", iter.hasNext());
        try {
            iter.next();
            fail("Iterator doesn't throw an exception when next() is called and there's nothing to traverse.");
        } catch (NoSuchElementException e) {
        }
    }



    static List<Integer> firstN(List<Integer> ls, int n) {
        List<Integer> ns = new LinkedList<>();
        int i = 0;
        for (int num : ls) {
            ns.add(num);
            if (++i >= ls.size()) break;
        }
        return ns;
    }
}