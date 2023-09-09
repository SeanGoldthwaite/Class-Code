<<<<<<< HEAD
=======
<<<<<<< HEAD
import java.util.*;
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8

//-----------------------------------------------------------------------
// Empty BST exception

class EmptyBSTE extends Exception {}

//-----------------------------------------------------------------------
// Abstract BST class

abstract class BST implements TreePrinter.PrintableNode, Iterable<Integer> {

    public static void main(String[] args) {
        BST bst = new EmptyBST();
        int[] nums = {0, -5, 5, -3, 3, -7, 7, -4};
        for (Integer e : nums) {
            bst = bst.BSTinsert(e);
        }
        TreePrinter.print(bst);
        Arrays.sort(nums);
        for (Integer e : nums) {
            System.out.print(e + " ");
        }
        System.out.println();
        for (Integer integer : bst) {
            System.out.print(integer + " ");
        }
    }

    //--------------------------
    // Static fields and methods
    //--------------------------

    static EmptyBSTE EBSTX = new EmptyBSTE();

    static BST EBST = new EmptyBST();

    // A leaf is a tree with empty left and right children
    static BST BSTLeaf(int elem) {
<<<<<<< HEAD
        return new BSTNode(elem, EBST, EBST); // TODO
=======
<<<<<<< HEAD
        return new BSTNode(elem, EBST, EBST);
=======
        return new BSTNode(elem, EBST, EBST); // TODO
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }

    // Use the iterator (that you need to define below) to get the BST nodes
    // one-by-one and insert them into the resulting AVL tree.
    static AVL toAVL (BST bst) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        Iterator<Integer> iter = bst.iterator();
        AVL result = new EmptyAVL();
        while (iter.hasNext()) {
            result.AVLinsert(iter.next());
        }
        return result;
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8

        try {
            Iterator<Integer> bstIterator = bst.iterator();
            AVLNode avl = new AVLNode(bstIterator.next(), new EmptyAVL(), new EmptyAVL());
            while (bstIterator.hasNext()) {
                avl.AVLinsert(bstIterator.next());
            }
            return avl;
        }catch(EmptyStackException e){
            return AVL.EAVL;
        }

<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
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

    abstract BST flip();
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

    BST BSTdelete(int key) throws EmptyBSTE { throw EBSTX; }

    Pair<Integer, BST> BSTdeleteLeftMostLeaf() throws EmptyBSTE { throw EBSTX; }

    @Override
    BST flip() {
        return this;
    }

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
        return new Iterator<Integer>() {
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
    private Stack<BST> stack;

    public BSTNode(int data, BST left, BST right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = 1 + (Math.max(left.BSTHeight(), right.BSTHeight()));
    }

    public BSTNode(int data, BST left, BST right){
        this.data = data;
        this.left = left;
        this.right = right;
        height = Math.max(left.BSTHeight(),right.BSTHeight())+1;
    }

    public BSTNode(int data, BST left, BST right){
        this.data = data;
        this.left = left;
        this.right = right;
        height = Math.max(left.BSTHeight(),right.BSTHeight())+1;
    }

    int BSTData() throws EmptyBSTE {
        return data;
    }

    BST BSTLeft() throws EmptyBSTE {
        return left;
    }

    BST BSTRight() throws EmptyBSTE {
        return right;
    }

    int BSTHeight() {
<<<<<<< HEAD
        return height; // TODO
=======
<<<<<<< HEAD
        return height;
=======
        return height; // TODO
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }

    boolean isEmpty() {
        return false;
    }

    //--------------------------
    // Main methods
    //--------------------------

    boolean BSTfind(int key) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        if (data == key)
            return true;
        else if (key < data)
            return left.BSTfind(key);
        else
            return right.BSTfind(key);
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8

        boolean found = false;
        if (key == this.data){
            found = true;
        }
        else if(key<this.data){
            found = left.BSTfind(key);
        }
        else{
            found = right.BSTfind(key);
        }

        return found;
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }

    /** @noinspection Duplicates*/
    BST BSTinsert(int key) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        if (key < data)
            return new BSTNode(data, left.BSTinsert(key), right);
        else
            return new BSTNode(data, left, right.BSTinsert(key));
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8

        BSTNode b;
        if(key<this.data){
           b = new BSTNode(data,left.BSTinsert(key),right);
        }
        else{
           b = new BSTNode(data, left, right.BSTinsert(key));
        }

return b;
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }



    /** @noinspection Duplicates*/
    BST BSTdelete(int key) throws EmptyBSTE {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        if (key == data) {
            try {
                Pair<Integer, BST> pair = right.BSTdeleteLeftMostLeaf();
                return new BSTNode(pair.getFirst(), left, pair.getSecond());
            }
            catch (EmptyBSTE e) {
                return left;
            }
        }
        else if (key < data) {
            BST newLeft = left.BSTdelete(key);
            return new BSTNode(data, newLeft, right);
        }
        else {
            BST newRight = right.BSTdelete(key);
            return new BSTNode(data, left, newRight);
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
        if (key == this.data){
            try {
                Pair<Integer, BST> leftMostChildOnRightAndTreeWithoutIt = right.BSTdeleteLeftMostLeaf();
                return new BSTNode(leftMostChildOnRightAndTreeWithoutIt.getFirst(), left, leftMostChildOnRightAndTreeWithoutIt.getSecond());
            }catch(EmptyBSTE e){
                return left;
            }
        }
        else if(key<this.data){
            return new BSTNode(data, left.BSTdelete(key),right);
        }
        else{
            return new BSTNode(data, left,right.BSTdelete(key));
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
        }
    }

    Pair<Integer, BST> BSTdeleteLeftMostLeaf() {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        try {
            Pair<Integer, BST> pair = left.BSTdeleteLeftMostLeaf();
            return new Pair<>(pair.getFirst(), new BSTNode(data, pair.getSecond(), right));
        }
        catch (EmptyBSTE e) {
            return new Pair<>(data, right);
        }
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
        try{
            Pair<Integer, BST> alpha = left.BSTdeleteLeftMostLeaf();
            return new Pair<Integer,BST>(alpha.getFirst(),new BSTNode(data, alpha.getSecond() ,right));
        }catch(EmptyBSTE e){
            return new Pair<>(data, right);
        }
    }

    @Override
    BST flip() {
        return new BSTNode(data, right.flip(), left.flip())
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
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
    public String toString() {
        return String.valueOf(data);
    }

    //--------------------------
    // Iterable interface
    //--------------------------

    public Iterator<Integer> iterator() {
<<<<<<< HEAD
=======
<<<<<<< HEAD
        stack = new Stack<>();
        BST current = this;
        while (current instanceof BSTNode) {
            stack.push(current);
            try {
                current = current.BSTLeft();
            }
            catch (EmptyBSTE e) {
                break;
            }
        }
        return new Iterator<Integer>() {
            @Override
            public boolean hasNext() {
                return !stack.isEmpty();
            }
            @Override
            public Integer next() {
                //System.out.println(stack);
                if (stack.isEmpty())
                    throw new NoSuchElementException();
                BST current = stack.pop();
                try {
                    if (current.BSTRight() instanceof BSTNode)
                        stack.push(current.BSTRight());
                    BST stackFiller = current.BSTRight().BSTLeft(); //current.BSTRight() has already been added
                    while (stackFiller instanceof BSTNode) {
                        stack.push(stackFiller);
                            stackFiller = stackFiller.BSTLeft();
                    }
                }
                catch (EmptyBSTE ignored) {}
                try {
                    return current.BSTData();
                }
                catch (EmptyBSTE e) {
                    throw new NoSuchElementException();
                }
            }
        };
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
        return new TreeIterator(this);
    }


}

class TreeIterator implements Iterator<Integer>{


    private Stack<BST> stack = new Stack<>();

    public TreeIterator(BST b){
        loadStack(b);
<<<<<<< HEAD
    }




    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Integer next() {
        try {
            BST s = stack.pop();
            int t = 0;
            try {
                t = s.BSTData();
                this.loadStack(s.BSTRight());
            } catch (EmptyBSTE emptyBSTE) {
                emptyBSTE.printStackTrace();
            }
            return t;
        } catch (EmptyStackException e){
            throw new NoSuchElementException();
        }

    }

    private void loadStack(BST root){
        if (!root.isEmpty()){
            stack.push(root);
            try {
                loadStack(root.BSTLeft());
            } catch (EmptyBSTE emptyBSTE) {
                emptyBSTE.printStackTrace();
            }
        }
    }

=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
    }




    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public Integer next() {
        try {
            BST s = stack.pop();
            int t = 0;
            try {
                t = s.BSTData();
                this.loadStack(s.BSTRight());
            } catch (EmptyBSTE emptyBSTE) {
                emptyBSTE.printStackTrace();
            }
            return t;
        } catch (EmptyStackException e){
            throw new NoSuchElementException();
        }

    }

    private void loadStack(BST root){
        if (!root.isEmpty()){
            stack.push(root);
            try {
                loadStack(root.BSTLeft());
            } catch (EmptyBSTE emptyBSTE) {
                emptyBSTE.printStackTrace();
            }
        }
    }

>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
}

//-----------------------------------------------------------------------
//-----------------------------------------------------------------------
