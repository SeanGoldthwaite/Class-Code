// Complete the implementation of this class

import java.util.ArrayList;

class StackList<E> implements StackI<E> {
    private List<E> elements;

    StackList() {
        elements = new EmptyL<>();
    }

    public void clear() {
        elements = new EmptyL<>();
    }

    public boolean empty() {
        return elements instanceof EmptyL;
    }

    public E peek() throws NoSuchElementE {
        return elements.getFirst();
    }

    public E pop() throws NoSuchElementE {
        E temp = elements.getFirst();
        int length = elements.length();
        if (length > 2)
            elements = new NodeL<>(elements.getRest().getFirst(), elements.getRest().getRest());
        else if (length == 2)
            elements = new NodeL<>(elements.getRest().getFirst(), new EmptyL<>());
        else
            elements = new EmptyL<>();
        return temp;
    }
    public void push(E item) {
        elements = new NodeL<>(item, elements);
    }
    public int size() {
        return elements.length();
    }
    public boolean equals(Object other) {
        return this.toString().equals(other.toString());
    }
    public String toString() {
        return elements.toString();
    }
    public static void main(String[] args) {
        ArrayList<Integer> vals = new ArrayList<>();
        StackList<Integer> stack = new StackList<>();
        StackList<Integer> stack2 = new StackList<>();
        for (int i = 0; i < 100; i++) {
            vals.add((int)(Math.random() * 1000));
            stack.push(vals.get(i));
            stack2.push(vals.get(i));
        }
        System.out.println(stack);
        System.out.println(stack2);
        System.out.println(stack.equals(stack2));
    }
}
