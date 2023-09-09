//-----------------------------------------------------------------------
class EmptyTreeE extends Exception {}
//-----------------------------------------------------------------------
abstract class BinTree<E> implements TreePrinter.PrintableNode {
    abstract E getData() throws EmptyTreeE;
    abstract BinTree<E> getLeftT() throws EmptyTreeE;
    abstract BinTree<E> getRightT() throws EmptyTreeE;
    abstract boolean isEmpty();
    abstract boolean isLeaf();
    abstract int size();
    abstract int height();
    abstract BinTree<E> mirror();
    abstract boolean isBalanced();
    // static methods
    static <E> BinTree<E> makeLeaf(E elem) {
        return new TreeNode<>(elem, new EmptyTree<>(), new EmptyTree<>());
    }
}
//-----------------------------------------------------------------------
class EmptyTree<E> extends BinTree<E> {
    // Printable interface
    public TreePrinter.PrintableNode getLeft() {
        return null;
    }
    public TreePrinter.PrintableNode getRight() {
        return null;
    }
    public String getText() {
        return "";
    }
    //-----
    E getData() throws EmptyTreeE {
        throw new EmptyTreeE();
    }
    BinTree<E> getLeftT() throws EmptyTreeE {
        throw new EmptyTreeE();
    }
    BinTree<E> getRightT() throws EmptyTreeE {
        throw new EmptyTreeE();
    }
    boolean isEmpty() {
        return true;
    }
    int size() {
        return 0;
    }
    int height () {
        return 0;
    }
    boolean isLeaf() { return false; }
    BinTree<E> mirror () { return this; }
    boolean isBalanced () { return true; }
}
//-----------------------------------------------------------------------
class TreeNode<E> extends BinTree<E> {
    private E data;
    private BinTree<E> left, right;
    TreeNode (E data, BinTree<E> left, BinTree<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }
    //--------------------------
    // Printable interface
    //--------------------------
    public TreePrinter.PrintableNode getLeft() {
        return getLeftT();
    }
    public TreePrinter.PrintableNode getRight() {
        return getRightT();
    }
    public String getText() {
        return String.valueOf(getData());
    }
    //-----
    E getData() {
        return data;
    }
    BinTree<E> getLeftT() {
        return left;
    }
    BinTree<E> getRightT() {
        return right;
    }
    boolean isEmpty () { return false; }
    int size () { return 1 + left.size() + right.size(); }
    int height () {
        return 1 + Math.max(left.height(), right.height());
    }
    boolean isLeaf () {
        return left.isEmpty() && right.isEmpty();
    }
    BinTree<E> mirror () {
        return new TreeNode<>(data, right.mirror(), left.mirror());
    }
    boolean isBalanced () {
        int lh = left.height();
        int rh = right.height();
        boolean hcheck = Math.abs(lh - rh) <= 1;
        return left.isBalanced() && right.isBalanced() && hcheck;
    }
}
//-----------------------------------------------------------------------
//-----------------------------------------------------------------------