// Complete the implementation of this class


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class LinkedList<E> implements LinkedListI<E> {
    private List<E> elements;

    LinkedList () {
        elements = new EmptyL<>();
    }

    public void clear() {
        elements = new EmptyL<>();
    }

    public int size() {
        return elements.length();
    }

    public void addFirst(E elem) {
        elements = new NodeL<>(elem, elements);
    }

    public void addLast(E elem) {
        elements = elements.addLast(elem);
    }

    public E getFirst() throws NoSuchElementE {
        return elements.getFirst();
    }

    public E getLast() throws NoSuchElementE {
        return elements.getLast();
    }

    public E removeFirst() throws NoSuchElementE {
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
    public boolean equals(Object other) {
        return this.toString().equals(other.toString());
    }
    public String toString() {
        return elements.toString();
    }
    public static void main(String[] args) throws NoSuchElementE {
        LinkedList<Integer> seven = new LinkedList<>();
        seven.addFirst(11);
        System.out.println(seven);
        seven.addLast(14);
        System.out.println(seven);
        seven.addFirst(10);
        System.out.println(seven);
        seven.addFirst(7);
        System.out.println(seven);
        seven.addLast(16);
        System.out.println(seven.getLast());
    }
}