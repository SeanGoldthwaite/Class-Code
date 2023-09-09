import java.util.Iterator;
import java.util.NoSuchElementException;

//-----------------------------------------------------------------------
// Empty BST exception

class EmptyBSTE extends Exception {}

//-----------------------------------------------------------------------
// Abstract BST class

abstract class BST implements TreePrinter.PrintableNode, Iterable<Integer> {

    //--------------------------
    // Static fields and methods
    //--------------------------

    static EmptyBSTE EBSTX = new EmptyBSTE();

    static BST EBST = new EmptyBST();

    static BST BSTLeaf(int elem) {
        return new BSTNode(elem, EBST, EBST);
    }

    //--------------------------
    // Getters and simple methods
    //--------------------------

    abstract int BSTData() throws EmptyBSTE;

    abstract BST BSTLeft() throws EmptyBSTE;

    abstract BST BSTRight() throws EmptyBSTE;

    abstract int BSTHeight();

    abstract boolean isEmpty();

    //--------------------------
    // Main methods
    //--------------------------

    abstract boolean BSTfind (int key);

    abstract BST BSTinsert(int key);

    abstract BST BSTdelete(int key) throws EmptyBSTE;

    abstract Pair<Integer, BST> BSTdeleteLeftMostLeaf() throws EmptyBSTE;

    // convert min tree to max tree
    abstract BST flip ();

    // returns the kth element of the tree
    int kth (int k) throws EmptyBSTE {
        Iterator<Integer> iter = this.iterator();
        for (int i = 0; i < k; i++) {
            if (!iter.hasNext())
                throw EBSTX;
            iter.next();
        }
        if (iter.hasNext())
            return iter.next();
        else
            throw EBSTX;
    }
}

//-----------------------------------------------------------------------

class EmptyBST extends BST {

    //--------------------------
    // Getters and simple methods
    //--------------------------

    int BSTData() throws EmptyBSTE {
        throw EBSTX;
    }

    BST BSTLeft() throws EmptyBSTE {
        throw EBSTX;
    }

    BST BSTRight() throws EmptyBSTE {
        throw EBSTX;
    }

    int BSTHeight() {
        return 0;
    }

    boolean isEmpty () { return true; }

    //--------------------------
    // Main methods
    //--------------------------

    boolean BSTfind(int key) {
        return false;
    }

    BST BSTinsert(int key) {
        return BSTLeaf(key);
    }

    BST BSTdelete(int key) throws EmptyBSTE {
        throw EBSTX;
    }

    Pair<Integer, BST> BSTdeleteLeftMostLeaf() throws EmptyBSTE {
        throw EBSTX;
    }

    // convert min tree to max tree
    BST flip () { return EBST; }

    //--------------------------
    // Printable interface
    //--------------------------

    public TreePrinter.PrintableNode getLeft() {
        return null;
    }

    public TreePrinter.PrintableNode getRight() {
        return null;
    }

    public String getText() {
        return "";
    }

    //--------------------------
    // Iterable interface
    //--------------------------

    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            public boolean hasNext() {
                return false;
            }

            public Integer next() {
                throw new NoSuchElementException();
            }
        };
    }
}

//-----------------------------------------------------------------------
// Non-empty tree case

class BSTNode extends BST {
    private int data;
    private BST left, right;
    private int height;

    BSTNode(int data, BST left, BST right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = 1 + Math.max(left.BSTHeight(), right.BSTHeight());
    }

    //--------------------------
    // Getters and simple methods
    //--------------------------

    int BSTData() {
        return data;
    }

    BST BSTLeft() {
        return left;
    }

    BST BSTRight() {
        return right;
    }

    int BSTHeight() {
        return height;
    }

    boolean isEmpty () { return false; }

    //--------------------------
    // Main methods
    //--------------------------

    boolean BSTfind(int key) {
        if (data == key) return true;
        else if (key < data) return left.BSTfind(key);
        else return right.BSTfind(key);
    }

    BST BSTinsert(int key) {
        if (key < data) return new BSTNode(data, left.BSTinsert(key), right);
        else return new BSTNode(data, left, right.BSTinsert(key));
    }

    BST BSTdelete(int key) throws EmptyBSTE {
        if (key < data) return new BSTNode(data, left.BSTdelete(key), right);
        else if (key > data) return new BSTNode(data, left, right.BSTdelete(key));
        else {
            try {
                Pair<Integer, BST> dr = right.BSTdeleteLeftMostLeaf();
                return new BSTNode(dr.getFirst(), left, dr.getSecond());
            } catch (EmptyBSTE e) {
                return left;
            }
        }
    }

    Pair<Integer, BST> BSTdeleteLeftMostLeaf() {
        try {
            Pair<Integer, BST> dl = left.BSTdeleteLeftMostLeaf();
            return new Pair<>(dl.getFirst(), new BSTNode(data, dl.getSecond(), right));
        } catch (EmptyBSTE e) {
            return new Pair<>(data, right);
        }
    }

    // convert min tree to max tree
    BST flip () {
        return new BSTNode(data, right.flip(), left.flip());
    }

    //--------------------------
    // Printable interface
    //--------------------------

    public TreePrinter.PrintableNode getLeft() {
        return left.isEmpty() ? null : left;
    }

    public TreePrinter.PrintableNode getRight() {
        return right.isEmpty() ? null : right;
    }

    public String getText() {
        return String.valueOf(data);
    }

    //--------------------------
    // Iterable interface
    //--------------------------

    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            private boolean hasNext = true;
            private boolean visited = false;
            private Iterator<Integer> leftIter = left.iterator();
            private Iterator<Integer> rightIter = null;

            public boolean hasNext() {
                return hasNext;
            }

            public Integer next() {
                if (leftIter.hasNext()) { return leftIter.next(); }
                if (!visited) {
                    visited = true;
                    rightIter = right.iterator();
                    hasNext = rightIter.hasNext();
                    return data;
                }
                if (rightIter.hasNext()) {
                    int next = rightIter.next();
                    hasNext = rightIter.hasNext();
                    return next;
                }
                throw new NoSuchElementException();
            }
        };
    }

}

//-----------------------------------------------------------------------
//-----------------------------------------------------------------------
