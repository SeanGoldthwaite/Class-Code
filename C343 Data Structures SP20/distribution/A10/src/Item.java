public class Item implements TreePrinter.PrintableNode {
    private int value;
    private int position;
    private WeakHeap wh;

    Item(int value) {
        this.value = value;
    }

    int getValue() { return value; }

    void setValue (int value) { this.value = value; }

    int getPosition() { return position; }

    void setPosition(int position) {
        this.position = position;
    }

    void setHeap (WeakHeap wh) {
        this.wh = wh;
    }

    public String toString() {
        return String.format("%d:%d", value, position);
    }

    // PrintableNode interface

    public TreePrinter.PrintableNode getLeft() {
        try {
            return wh.getElem(wh.getLeftChildIndex(position));
        }
        catch (NoLeftChildE e) { return null; }
    }

    public TreePrinter.PrintableNode getRight() {
        try {
            return wh.getElem(wh.getRightChildIndex(position));
        }
        catch (NoRightChildE e) { return null; }
    }

    public String getText() { return toString(); }
}
