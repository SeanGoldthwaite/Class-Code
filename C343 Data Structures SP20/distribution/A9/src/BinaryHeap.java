import java.util.ArrayList;
import java.util.List;

/**
 * Binary heap with reverse bits...
 * We can flip left and right subtrees in one operation
 *
 * There is a subtle interaction between the heap and the items it contains.
 * - the heap maintains an arraylist of all items
 * - each item has a reference to the heap and its position within the arraylist
 */

class NoParentE extends Exception {
}

class NoLeftChildE extends Exception {
}

class NoRightChildE extends Exception {
}

public class BinaryHeap {
    private int size;
    private ArrayList<Item> elems;

    BinaryHeap() {
        this.size = 0;
        this.elems = new ArrayList<>();
    }

    /**
     * This constructor performs "heapify". First it copy the incoming
     * elements one-by-one to the arraylist 'elems' stored as an instance variable.
     * For each item copied, the constructor should initialize properly using
     * setPosition and setHeap. When everything is properly initialized and
     * copied to 'elems' the constructor calls 'heapify'.
     */
    BinaryHeap(ArrayList<Item> es) {
        elems = es;
        size = elems.size();
        heapify();
        for (int i = 0; i < size; i++) {
            elems.get(i).setPosition(i);
            elems.get(i).setHeap(this);
        }
        System.out.println();
    }

    /**
     * Implement it as discussed in class...
     */
    void heapify () {
        for (int i = (elems.size() / 2); i >= 0; i--) {
            moveDown(i);
        }
    }

    boolean isEmpty() {
        return size == 0;
    }

    int getSize() {
        return size;
    }

    /**
     * We will location 0 in the array. The minimum is always guaranteed to be there
     * unless of course the array is empty
     */
    Item findMin() {
        if (isEmpty())
            return null;
        else
            return elems.get(0);
    }

    List<Item> getElems() {
        return elems;
    }

    Item getElem (int i) {
        return elems.get(i);
    }

    /**
     * As discussed in class and in the notes, the parent is at index i/2
     * unless of course the current node is the root of the tree
     */
    int getParentIndex(int i) throws NoParentE {
        if (i == 0 || i >= size)
            throw new NoParentE();
        else
            return (i / 2);
    }

    /**
     * Under normal circumstances the left child is at index 2i+1. It is possible
     * the index 2i+1 is outside of the array bounds and in that case the node
     * does not have a left child. It is also possible that the current element
     * has its reverse bit set, which means that the child at index 2i+1 is actually
     * the right child and the child at index 2i+2 is the left child.
     */
    int getLeftChildIndex(int i) throws NoLeftChildE {
        if (i < 0 || (i * 2) + 1 >= size)
            throw new NoLeftChildE();
        if (elems.get(i).getRevbit() == 1) {
            if ((i * 2) + 2 >= size)
                throw new NoLeftChildE();
            else
                return (i * 2) + 2;
        }
        else
            return (i * 2) + 1;
    }

    int getRightChildIndex(int i) throws NoRightChildE {
        if (i < 0 || (i * 2) + 2 >= size)
            throw new NoRightChildE();
        if (elems.get(i).getRevbit() == 1)
            return (i * 2) + 1;
        else
            return (i * 2) + 2;
    }

    /**
     * This method swaps the array entries at the given indices. It also needs
     * to update each element with its new position.
     */
    void swap(int i, int j) {
        Item temp = elems.get(i);
        elems.set(i, elems.get(j));
        elems.set(j, temp);

        elems.get(i).setPosition(i);
        elems.get(j).setPosition(j);
    }

    int getValue(int i) {
        return elems.get(i).getValue();
    }

    /**
     * When an element is inserted, it is inserted in the bottom layer of the
     * tree and then moveUp is recursively called to move it to its proper
     * position.
     */
    void moveUp(int i) {
        try {
            int parent = getParentIndex(i);
            if (getValue(parent) > getValue(i))
                swap(parent, i);
            else
                return;
            moveUp(parent);
        }
        catch (NoParentE ignored) { }
    }

    void insert(Item ek) {
        ek.setHeap(this);
        ek.setPosition(size++);
        elems.add(ek);
        moveUp(size - 1);
    }

    /**
     * When a large element finds itself high in the tree for some reason,
     * we need to move it down. For that we need to compare it to both its
     * children and swap it with the smaller of them
     */
    int minChildIndex(int i) throws NoLeftChildE {
        try {
            int rc = getRightChildIndex(i);
            int lc = getLeftChildIndex(i);

            if (getValue(lc) < getValue(rc))
                return lc;
            else
                return rc;
        }
        catch (NoRightChildE e) {
            try {
                return getLeftChildIndex(i);
            }
            catch (NoLeftChildE ee) {
                throw new NoLeftChildE();
            }
        }
    }

    void moveDown(int i) {
        try {
            int minChild = minChildIndex(i);
            if (getValue(minChild) < getValue(i))
                swap(minChild, i);
            else
                return;
            moveDown(minChild);
        }
        catch (NoLeftChildE ignored) {}
    }

    /**
     * The minimum is at location 0. To remove it we take the last element
     * in the array and move it to location 0 and then recursively apply
     * moveDown.
     */
    Item extractMin() {
        swap(0, size - 1);
        Item result = elems.remove(--size);
        moveDown(0);
        return result;
    }


    public String toString() {
        return getElems().toString();
    }
}