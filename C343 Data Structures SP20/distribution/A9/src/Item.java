public class Item implements TreePrinter.PrintableNode {
    private String name;
    private int position;
    private int value;
    private int revbit;
    private BinaryHeap bh;

    Item(String name, int value) {
        this.name = name;
        this.position = -1;
        this.value = value;
        this.revbit = 0;
    }

    void setPosition(int position) {
        this.position = position;
    }

    void setHeap (BinaryHeap bh) {
        this.bh = bh;
    }

    int getValue() {
        return value;
    }

    int getRevbit () { return revbit; }

    void reverse () {
        revbit = 1 - revbit;
    }

    public String toString() {
        return String.valueOf(value);
    }

    // TreePrinter

    public TreePrinter.PrintableNode getLeft() {
        try {
            return bh.getElem(bh.getLeftChildIndex(position));
        }
        catch (NoLeftChildE e) {
            return null; }
    }

    public TreePrinter.PrintableNode getRight() {
        try {
            return bh.getElem(bh.getRightChildIndex(position));
        }
        catch (NoRightChildE e) {
            return null;
        }
    }

    public String getText() {
        return String.valueOf(value);
    }
}
