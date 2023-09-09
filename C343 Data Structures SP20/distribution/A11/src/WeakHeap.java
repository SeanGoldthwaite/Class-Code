import java.util.ArrayList;
import java.util.List;

class RootE extends Exception {
}

class NoLeftChildE extends Exception {
}

class NoRightChildE extends Exception {
}

public class WeakHeap {
    private int size;
    private ArrayList<Item> elems;
    private ArrayList<Integer> flips;
    /*
     * Position 0 in elems is for the root.
     * The root has no left child.
     * Position 1 in elems is for the right child of the root
     * After that the left child of an item at position i is at position 2i+flips(i)
     * and the right child is at position 2i+1-flips(i)
     * The parent of a child at position i is at position i/2
     * flips(0) should never be set to 1 because that would give the root
     * a left child instead of a right one
     */

    WeakHeap() {
        this.size = 0;
        this.elems = new ArrayList<>();
        this.flips = new ArrayList<>();
    }

    WeakHeap (ArrayList<Item> items) {
        this.size = items.size();
        this.elems = new ArrayList<>();
        this.flips = new ArrayList<>();
        for (int i=0; i<size; i++) {
            Item item = items.get(i);
            item.setPosition(i);
            item.setHeap(this);
            elems.add(item);
            flips.add(0);
        }
        weakHeapify ();
    }

    void weakHeapify () {
        try {
            for (int j = size - 1; j >= 1; j--) {
                int i = getDAncestorIndex(j);
                join(i,j);
            }
        }
        catch (RootE e) {
            throw new Error("Internal bug: weakHeapify");
        }
    }

    // Trivial methods

    boolean isEmpty() {
        return size == 0;
    }

    int getSize() {
        return size;
    }

    Item findMin() {
        return elems.get(0);
    }

    List<Item> getElems() {
        return elems;
    }

    Item getElem (int i) {
        return elems.get(i);
    }

    int getValue(int i) {
        return elems.get(i).getValue();
    }

    int getFlip (int i) {
        return flips.get(i);
    }

    public String toString() {
        return getElems().toString();
    }

    // Computations with indices

    int getParentIndex(int i) throws RootE {
        if (i == 0) throw new RootE();
        else return i / 2;
    }

    int getLeftChildIndex(int i) throws NoLeftChildE {
        if (i == 0) throw new NoLeftChildE();
        int li = 2 * i + flips.get(i);
        if (li >= size) throw new NoLeftChildE();
        return li;
    }

    int getRightChildIndex(int i) throws NoRightChildE {
        if (i == 0) return 1;
        int ri = 2 * i + 1 - flips.get(i);
        if (ri >= size) throw new NoRightChildE();
        return ri;
    }

    boolean isLeftChild (int i) throws RootE {
        return i != 0 && i % 2 == flips.get(getParentIndex(i));
    }

    boolean isRightChild (int i) throws RootE {
        return i != 0 && i % 2 != flips.get(getParentIndex(i));
    }

    int getDAncestorIndex(int i) throws RootE {
        if (isRightChild(i)) return getParentIndex(i);
        return getDAncestorIndex(getParentIndex(i));
    }

    int getLeftMostChildIndex () throws NoLeftChildE {
        if (size <= 1) throw new NoLeftChildE();
        int k = 1;
        try {
            while (true) {
                k = getLeftChildIndex(k);
            }
        }
        catch (NoLeftChildE e) {
            return k;
        }
    }

    // Helpers for main methods

    void swap(int i, int j) {
        Item tempi = elems.get(i);
        Item tempj = elems.get(j);
        elems.set(i, tempj);
        elems.set(j, tempi);
        tempi.setPosition(j);
        tempj.setPosition(i);
    }

    boolean join (int i, int j) {
        Item hi = elems.get(i);
        Item hj = elems.get(j);
        if (hj.getValue() < hi.getValue()) {
            swap(i,j);
            flips.set(j,1-flips.get(j));
            return false;
        }
        return true;
    }

    void moveUp (int j) {
        try {
            int i = getDAncestorIndex(j);
            if (join(i,j)) return;
            moveUp(i);
        }
        catch (RootE e) {
            return;
        }
    }

    void moveDown (int j) {
        try {
            int k = getLeftMostChildIndex();
            while (j != k) {
                join(j, k);
                k = getParentIndex(k);
            }
        }
        catch (NoLeftChildE e) {
            throw new Error("Internal bug: NoLeftChildE in moveDown");
        }
        catch (RootE e) {
            throw new Error("Internal bug: RootE in moveDown");
        }
    }

    void updateKey (int i, int value) {
	assert value < elems.get(i).getValue();
        elems.get(i).setValue(value);
        moveUp(i);
    }

    // Main methods: insert and extractMin

    void insert (Item item) {
        try {
            size++;
            item.setPosition(size - 1);
            item.setHeap(this);
            elems.add(item);
            flips.add(0);
            if (isLeftChild(size - 1)) flips.set(getParentIndex(size-1),0);
            moveUp(size-1);
        }
        catch (RootE e) {
            e.printStackTrace();
            throw new Error("Internal bug: insert");
        }
    }

    Item extractMin () {
        Item result = findMin();
        elems.set(0, elems.get(size-1));
        elems.get(0).setPosition(0);
        elems.remove(size-1);
        size--;
        if (size > 1) moveDown(0);
        return result;
    }

    // For debugging

    boolean checkOrder () {
        try {
            for (int j = size - 1; j >= 1; j--) {
                int i = getDAncestorIndex(j);
                if (getValue(j) < getValue(i)) return false;
            }
            return true;
        }
        catch (RootE e) {
            throw new Error("Internal bug: checkOrder");
        }

    }

}


