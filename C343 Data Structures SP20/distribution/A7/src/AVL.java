//-----------------------------------------------------------------------
// Empty AVL exception

<<<<<<< HEAD
=======
<<<<<<< HEAD
class EmptyAVLE extends Exception {}
enum Balance {
    LEFT, BALANCED, RIGHT;
}
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
import java.util.NoSuchElementException;

class EmptyAVLE extends Exception {
}

>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
//-----------------------------------------------------------------------
// Abstract AVL class

abstract class AVL implements TreePrinter.PrintableNode {

    public static void main(String[] args) {
        AVL avl = new EmptyAVL();
        avl = avl.AVLinsert(-1);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        avl = avl.AVLinsert(-13);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        avl = avl.AVLinsert(-15);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        avl = avl.AVLinsert(7);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        avl = avl.AVLinsert(-17);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

        avl = avl.AVLinsert(4);
        TreePrinter.print(avl);
        System.out.printf("%n%n%n");

    }

    //--------------------------
    // Static fields and methods
    //--------------------------

    static EmptyAVLE EAVLX = new EmptyAVLE();

    static AVL EAVL = new EmptyAVL();
    static AVL AVLLeaf(int elem) {
        return new AVLNode(elem, EAVL, EAVL);
    }

    // Recursively copy the tree changing AVL nodes to BST nodes
<<<<<<< HEAD
=======
<<<<<<< HEAD
    static BST toBST (AVL avl) {
        try {
            BST newLeft = toBST(avl.AVLLeft());
            BST newRight = toBST(avl.AVLRight());
            return new BSTNode(avl.AVLData(), newLeft, newRight);
        }
        catch (EmptyAVLE e) {
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    static BST toBST(AVL avl) {
        try {
            return new BSTNode(avl.AVLData(), toBST(avl.AVLLeft()), toBST(avl.AVLRight()));
        } catch (EmptyAVLE emptyAVLE) {
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
            return new EmptyBST();
        }
    }

    //--------------------------
    // Getters and simple methods
    //--------------------------

    abstract int AVLData() throws EmptyAVLE;

    abstract AVL AVLLeft() throws EmptyAVLE;

    abstract AVL AVLRight() throws EmptyAVLE;

    abstract int AVLHeight();

    abstract boolean isEmpty();

    //--------------------------
    // Main methods
    //--------------------------

    abstract boolean AVLfind(int key);

    abstract AVL AVLinsert(int key);

    abstract AVL AVLeasyRight();

    abstract AVL AVLrotateRight();

    abstract AVL AVLeasyLeft();

    abstract AVL AVLrotateLeft();

    abstract AVL AVLdelete(int key) throws EmptyAVLE;

    abstract Pair<Integer, AVL> AVLshrink() throws EmptyAVLE;

}

//-----------------------------------------------------------------------

class EmptyAVL extends AVL {

    //--------------------------
    // Getters and simple methods
    //--------------------------

    int AVLData() throws EmptyAVLE {
        throw EAVLX;
    }

    AVL AVLLeft() throws EmptyAVLE {
        throw EAVLX;
    }

    AVL AVLRight() throws EmptyAVLE {
        throw EAVLX;
    }

    int AVLHeight() {
        return 0;
    }

    boolean isEmpty() {
        return true;
    }

    ;

    //--------------------------
    // Main methods
    //--------------------------

    boolean AVLfind(int key) {
        return false;
    }

    AVL AVLinsert(int key) {
        return AVLLeaf(key);
    }

    AVL AVLeasyRight() {
        throw new Error("Internal bug: should never call easyRight on empty tree");
    }

    AVL AVLrotateRight() {
        throw new Error("Internal bug: should never call rotateRight on empty tree");
    }

    AVL AVLeasyLeft() {
        throw new Error("Internal bug: should never call easyLeft on empty tree");
    }

    AVL AVLrotateLeft() {
        throw new Error("Internal bug: should never call rotateLeft on empty tree");
    }

    AVL AVLdelete(int key) throws EmptyAVLE {
        throw EAVLX;
    }

    Pair<Integer, AVL> AVLshrink() throws EmptyAVLE {
        throw EAVLX;
    }

    //--------------------------
    // Override
    //--------------------------

    public boolean equals(Object o) {
        return (o instanceof EmptyAVL);
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
}

//-----------------------------------------------------------------------

class AVLNode extends AVL {
    private int data;
    private AVL left, right;
    private int height;

    public AVLNode(int data, AVL left, AVL right) {
        this.data = data;
        this.left = left;
        this.right = right;
<<<<<<< HEAD
        height = 1 + Math.max(left.AVLHeight(), right.AVLHeight());
=======
<<<<<<< HEAD
        this.height = 1 + Math.max(left.AVLHeight(), right.AVLHeight());
=======
        height = 1 + Math.max(left.AVLHeight(), right.AVLHeight());
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }

    //--------------------------
    // Getters and simple methods
    //--------------------------

    int AVLData() {
        return data;
    }

    AVL AVLLeft() {
        return left;
    }

    AVL AVLRight() {
        return right;
    }

    int AVLHeight() {
        return height;
    }

    boolean isEmpty() {
        return false;
    }

    ;

    //--------------------------
    // Main methods
    //--------------------------

    /**
     * @noinspection Duplicates
     */
    boolean AVLfind(int key) {
<<<<<<< HEAD
        boolean found = false;

        if (key == this.data) {
            found = true;
        } else if (key < this.data) {
            found = left.AVLfind(key);
        } else {
            found = right.AVLfind(key);
        }
        return found;
    }

    AVL AVLinsert(int key) {
        AVL b;
        if (key < this.data) {
            AVL newLeft = left.AVLinsert(key);
            b = new AVLNode(data, newLeft, right);

            if (right.AVLHeight()+1 < newLeft.AVLHeight()) {
                b = b.AVLrotateRight();
            }
        } else {
            AVL newRight = right.AVLinsert(key);
            b = new AVLNode(data, left, newRight);
            if (left.AVLHeight()+1 < newRight.AVLHeight() ) {
                b = b.AVLrotateLeft();
            }
        }




        return b;
    }

    AVL AVLeasyRight() {
        try {
            AVL newRoot = left;
            AVL danglingMan = newRoot.AVLRight();

            return new AVLNode(newRoot.AVLData(), newRoot.AVLLeft(),  new AVLNode(data, danglingMan,right));


        } catch (EmptyAVLE e) {
            throw new Error("Why are you rotating something that's empty?");
        }
    }


    AVL AVLrotateRight() {
        try {
            AVL finalTree;
            if (left.AVLLeft().AVLHeight() >= left.AVLRight().AVLHeight()) {
                finalTree = this.AVLeasyRight();
            } else {
                AVL newLeft = left.AVLeasyLeft();
                AVL midwayTree = new AVLNode(data, newLeft, right);
                finalTree = midwayTree.AVLeasyRight();

            }


            return finalTree;
        } catch (EmptyAVLE E) {
            throw new Error ("Why are you rotating something that's empty?");
        }

    }

    AVL AVLeasyLeft() {
        try {
            AVL newRoot = right;
            AVL danglingMan = newRoot.AVLLeft();

            return new AVLNode(newRoot.AVLData(), new AVLNode(data, left, danglingMan), newRoot.AVLRight());


        } catch (EmptyAVLE e) {
            throw new Error("Why are you rotating something that's empty?");
        }
    }

    AVL AVLrotateLeft() {
       try{
           AVL finalTree;
           if(right.AVLRight().AVLHeight() >= right.AVLLeft().AVLHeight()){
               finalTree = this.AVLeasyLeft();
           }
           else{
               AVL newRight = right.AVLeasyRight();
               AVL midwayTree = new AVLNode(data, left, newRight);
               finalTree = midwayTree.AVLeasyLeft();

           }

           return finalTree;
       }catch(EmptyAVLE e){
           throw new Error("What are you doing rotating something empty?");
       }
    }

    AVL AVLdelete(int key) throws EmptyAVLE {
        AVL finalTree;

            if (key < data) {
                AVL newLeft = left.AVLdelete(key);
                finalTree = new AVLNode(data, newLeft, right);
                if (newLeft.AVLHeight() + 1 < right.AVLHeight()) {
                    finalTree = finalTree.AVLrotateLeft();
                }

            } else if (key > data) {
                AVL newRight = right.AVLdelete(key);
                finalTree = new AVLNode(data, left, newRight);
                if (newRight.AVLHeight() + 1 < left.AVLHeight()) {
                    finalTree = finalTree.AVLrotateRight();
                }
            } else {
                try {
                    Pair<Integer, AVL> largestElementOnLeftAndBalancedLeft = left.AVLshrink();
                    finalTree = new AVLNode(largestElementOnLeftAndBalancedLeft.getFirst(), largestElementOnLeftAndBalancedLeft.getSecond(), right);
                    if (largestElementOnLeftAndBalancedLeft.getSecond().AVLHeight() + 1 < right.AVLHeight()) {
                        finalTree = finalTree.AVLrotateLeft();
                    }

                } catch (EmptyAVLE e) {
                    //return right if "this" doesnt have a left
                    finalTree = right;
                }
            }


        return finalTree;

    }

    Pair<Integer, AVL> AVLshrink() throws EmptyAVLE {
=======
<<<<<<< HEAD
        if (data == key)
            return true;
        else if (key < data)
            return left.AVLfind(key);
        else
            return right.AVLfind(key);
    }

    AVL AVLinsert(int key) {
        AVL newTree;
        if (key < data) {
            newTree = new AVLNode(data, left.AVLinsert(key), right);
        }
        else {
            ; //newTree is new right subtree
            newTree = new AVLNode(data, left, right.AVLinsert(key));
        }

        try {
            if (newTree.AVLLeft().AVLHeight() - newTree.AVLRight().AVLHeight() > 1)
                return newTree.AVLrotateRight();
        }
        catch (EmptyAVLE ignored) {}
        try {
            if (newTree.AVLLeft().AVLHeight() - newTree.AVLRight().AVLHeight() < -1)
                return newTree.AVLrotateLeft();
        }
        catch (EmptyAVLE ignored) {}

        return newTree;
    }

    AVL AVLeasyRight() {
        int newData;
        AVL newLeft, newRight;
        try {
            newData = left.AVLData();
        }
        catch (EmptyAVLE e) {
            throw new Error("No left subtree to rotate");
        }
        try {
            newLeft = left.AVLLeft();
        }
        catch (EmptyAVLE e) {
            newLeft = EAVL;
        }
        try {
            newRight = new AVLNode(data, left.AVLRight(), right);
        }
        catch (EmptyAVLE e) {
            newRight = new AVLNode(data, EAVL, right);
        }
        return new AVLNode(newData, newLeft, newRight);
=======
        boolean found = false;

        if (key == this.data) {
            found = true;
        } else if (key < this.data) {
            found = left.AVLfind(key);
        } else {
            found = right.AVLfind(key);
        }
        return found;
    }

    AVL AVLinsert(int key) {
        AVL b;
        if (key < this.data) {
            AVL newLeft = left.AVLinsert(key);
            b = new AVLNode(data, newLeft, right);

            if (right.AVLHeight()+1 < newLeft.AVLHeight()) {
                b = b.AVLrotateRight();
            }
        } else {
            AVL newRight = right.AVLinsert(key);
            b = new AVLNode(data, left, newRight);
            if (left.AVLHeight()+1 < newRight.AVLHeight() ) {
                b = b.AVLrotateLeft();
            }
        }




        return b;
    }

    AVL AVLeasyRight() {
        try {
            AVL newRoot = left;
            AVL danglingMan = newRoot.AVLRight();

            return new AVLNode(newRoot.AVLData(), newRoot.AVLLeft(),  new AVLNode(data, danglingMan,right));


        } catch (EmptyAVLE e) {
            throw new Error("Why are you rotating something that's empty?");
        }
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
    }


    AVL AVLrotateRight() {
<<<<<<< HEAD
        int leftleftHeight, leftrightHeight;
        AVL newLeft = left;

        try {
            leftleftHeight = left.AVLLeft().AVLHeight();
        }
        catch (EmptyAVLE e) {
            leftleftHeight = 0;
        }
        try {
            leftrightHeight = left.AVLRight().AVLHeight();
        }
        catch (EmptyAVLE e) {
            leftrightHeight = 0;
        }

        if (leftleftHeight - leftrightHeight < 0) {
            newLeft = left.AVLeasyLeft();
        }
        return new AVLNode(data, newLeft, right).AVLeasyRight();
    }

    AVL AVLeasyLeft() {
        int newData;
        AVL newLeft, newRight;
        try {
            newData = right.AVLData();
        }
        catch (EmptyAVLE e) {
            throw new Error("No right subtree to rotate");
        }
        try {
            newLeft = new AVLNode(data, left, right.AVLLeft());
        }
        catch (EmptyAVLE e) {
            newLeft = new AVLNode(data, left, EAVL);
        }
        try {
            newRight = right.AVLRight();
        }
        catch (EmptyAVLE e) {
            newRight = EAVL;
        }
        return new AVLNode(newData, newLeft, newRight);
    }

    AVL AVLrotateLeft() {
        int rightleftHeight, rightrightHeight;
        AVL newRight = right;

        try {
            rightleftHeight = right.AVLLeft().AVLHeight();
        }
        catch (EmptyAVLE e) {
            rightleftHeight = 0;
        }
        try {
            rightrightHeight = right.AVLRight().AVLHeight();
        }
        catch (EmptyAVLE e) {
            rightrightHeight = 0;
        }

        if (rightleftHeight - rightrightHeight > 0) {
            newRight = right.AVLeasyRight();
        }
        return new AVLNode(data, left, newRight).AVLeasyLeft();
    }

    AVL AVLdelete(int key) throws EmptyAVLE {
        AVL result;

        if (key == data) {
            try {
                Pair<Integer, AVL> pair = left.AVLshrink();
                result = new AVLNode(pair.getFirst(), pair.getSecond(), right);
            }
            catch (EmptyAVLE e) {
                return left;
            }
        }
        else if (key < data) {
            result = new AVLNode(data, left.AVLdelete(key), right);

        }
        else {
            result = new AVLNode(data, left, right.AVLdelete(key));
        }

        try {
            if (result.AVLLeft().AVLHeight() - result.AVLRight().AVLHeight() > 1) {
                result = result.AVLrotateLeft();
            }
            else if (result.AVLLeft().AVLHeight() - result.AVLRight().AVLHeight() < -1) {
                result = result.AVLrotateRight();
            }
        }
        catch (EmptyAVLE ignored) { }

        return result;
    }

    Pair<Integer, AVL> AVLshrink() throws EmptyAVLE {
        try {
            Pair<Integer, AVL> pair = right.AVLshrink();
            AVL result = new AVLNode(data, left, pair.getSecond());
            try {
                if (result.AVLLeft().AVLHeight() - result.AVLRight().AVLHeight() > 1) {
                    result = result.AVLrotateRight();
                }
                else if (result.AVLLeft().AVLHeight() - result.AVLRight().AVLHeight() < -1) {
                    result = result.AVLrotateRight();
                }
            }
            catch (EmptyAVLE ignored) { }
            return new Pair<>(pair.getFirst(), result);
        }
        catch (EmptyAVLE e) {
            return new Pair<>(data, left);
        }
=======
        try {
            AVL finalTree;
            if (left.AVLLeft().AVLHeight() >= left.AVLRight().AVLHeight()) {
                finalTree = this.AVLeasyRight();
            } else {
                AVL newLeft = left.AVLeasyLeft();
                AVL midwayTree = new AVLNode(data, newLeft, right);
                finalTree = midwayTree.AVLeasyRight();

            }


            return finalTree;
        } catch (EmptyAVLE E) {
            throw new Error ("Why are you rotating something that's empty?");
        }

    }

    AVL AVLeasyLeft() {
        try {
            AVL newRoot = right;
            AVL danglingMan = newRoot.AVLLeft();

            return new AVLNode(newRoot.AVLData(), new AVLNode(data, left, danglingMan), newRoot.AVLRight());


        } catch (EmptyAVLE e) {
            throw new Error("Why are you rotating something that's empty?");
        }
    }

    AVL AVLrotateLeft() {
       try{
           AVL finalTree;
           if(right.AVLRight().AVLHeight() >= right.AVLLeft().AVLHeight()){
               finalTree = this.AVLeasyLeft();
           }
           else{
               AVL newRight = right.AVLeasyRight();
               AVL midwayTree = new AVLNode(data, left, newRight);
               finalTree = midwayTree.AVLeasyLeft();

           }

           return finalTree;
       }catch(EmptyAVLE e){
           throw new Error("What are you doing rotating something empty?");
       }
    }

    AVL AVLdelete(int key) throws EmptyAVLE {
        AVL finalTree;

            if (key < data) {
                AVL newLeft = left.AVLdelete(key);
                finalTree = new AVLNode(data, newLeft, right);
                if (newLeft.AVLHeight() + 1 < right.AVLHeight()) {
                    finalTree = finalTree.AVLrotateLeft();
                }

            } else if (key > data) {
                AVL newRight = right.AVLdelete(key);
                finalTree = new AVLNode(data, left, newRight);
                if (newRight.AVLHeight() + 1 < left.AVLHeight()) {
                    finalTree = finalTree.AVLrotateRight();
                }
            } else {
                try {
                    Pair<Integer, AVL> largestElementOnLeftAndBalancedLeft = left.AVLshrink();
                    finalTree = new AVLNode(largestElementOnLeftAndBalancedLeft.getFirst(), largestElementOnLeftAndBalancedLeft.getSecond(), right);
                    if (largestElementOnLeftAndBalancedLeft.getSecond().AVLHeight() + 1 < right.AVLHeight()) {
                        finalTree = finalTree.AVLrotateLeft();
                    }

                } catch (EmptyAVLE e) {
                    //return right if "this" doesnt have a left
                    finalTree = right;
                }
            }


        return finalTree;

    }

    Pair<Integer, AVL> AVLshrink() throws EmptyAVLE {
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
       try{
           Pair<Integer, AVL> rightMostChildAndTreeWithoutIt = right.AVLshrink();

           AVL newLeft = new AVLNode(data, left, rightMostChildAndTreeWithoutIt.getSecond());

           if(left.AVLHeight() > rightMostChildAndTreeWithoutIt.getSecond().AVLHeight()+1){
               newLeft = newLeft.AVLrotateRight();
           }
           return new Pair<>(rightMostChildAndTreeWithoutIt.getFirst(), newLeft);

       }catch(EmptyAVLE e){
           return new Pair<Integer,AVL>(data, left);

       }
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }


    //--------------------------
    // Override
    //--------------------------

    public boolean equals(Object o) {
        if (o instanceof AVLNode) {
            AVLNode other = (AVLNode) o;
            return data == other.data && left.equals(other.left) && right.equals(other.right);
        }
        return false;
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
}

//-----------------------------------------------------------------------
//-----------------------------------------------------------------------
