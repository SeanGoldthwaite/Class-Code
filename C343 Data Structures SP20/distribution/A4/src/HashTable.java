import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.DrbgParameters;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.BiFunction;

// -------------------------------------------------------

/**
 *
 * The abstract class for the four hash tables we will implement. The
 * file HashTableTest has four test cases that should produce the same
 * information as Figures 5.5, 5.11, 5.13, and 5.18 in the book. You
 * should implement many more test cases!!!
 *
 */
abstract class HashTable {
    abstract int getCapacity();
    abstract void setCapacity(int capacity);

    abstract void insert (int key);
    abstract void delete (int key);
    abstract boolean search (int key);

    abstract void rehash ();
    private static boolean isPrime(int n) {
        if (n % 2 == 0 || n % 3 == 0) return false;

        for (int i = 5; i * i <= n; i = i + 6)
            if (n % i == 0 || n % (i + 2) == 0)
                return false;

        return true;
    }
    protected static int nextPrime(int n) {
        int prime = n;
        boolean found = false;

        while (!found) {
            prime++;
            if (isPrime(prime))
                found = true;
        }
        return prime;
    }
}

// -------------------------------------------------------

/**
 *
 * An implementation of a hash table that uses separate chaining. The
 * constructor should take one arguments 'hf' of type HashFunction.
 * The bound should be extracted from the HashFunction and an ArrayList
 * of that bound should be created in the constructor. Each
 * entry in that ArrayList should be initialized to an empty linked list.
 * The three methods to insert, delete, and search
 * should be implemented as described in the book. You should also
 * implement a method rehash that resizes the hash table to the next prime
 * after doubling the capacity.
 */
class HashSeparateChaining extends HashTable {

    private int capacity;
    private HashFunction hf;
    private ArrayList<LinkedList<Integer>> chains;

    HashSeparateChaining (HashFunction hf) {
        this.capacity = hf.getBound();
        this.hf = hf;
        this.chains = new ArrayList<>(capacity);
        for (int i=0; i<capacity; i++)
            chains.add(i, new LinkedList<>());
    }
    int getCapacity() {
        return capacity;
    }
    void setCapacity(int capacity) {
        chains.ensureCapacity(capacity);
        for (int i = this.capacity; i < capacity; i++)
            this.chains.add(i, new LinkedList<>());
        this.capacity = capacity;
    }
    void insert(int key) {
        chains.get(hf.apply(key)).add(key);
    }
    void delete(int key) {
        LinkedList<Integer> list = chains.get(hf.apply(key));
        if (list.contains(key))
            list.remove(list.indexOf(key));
    }
    boolean search(int key) {
        return chains.get(hf.apply(key)).contains(key);
    }
    void rehash() {
        ArrayList<Integer> elements = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            elements.addAll(chains.get(i));
            chains.set(i, new LinkedList<>());
        }
        int newCapacity = nextPrime(capacity * 2);
        setCapacity(newCapacity);
        hf.setBound(newCapacity);
        /*for (int i = 0; i < elements.size(); i++) {
            insert(elements.get(i));
        }*/
        for (Integer e : elements)
            insert(e);
    }
    public String toString () {
        String result = "";
        for (int i=0; i<capacity; i++) {
            result += "chain[" + i + "] = ";
            result += chains.get(i).toString();
            result += "\n";
        }
        return result;
    }
}

// -------------------------------------------------------

/**
 *
 * Before implementing the next three variants of hash tables, we will
 * implement a small class hierarchy of hash tables entries. There are
 * three kinds of entries: an entry that contains an 'int' value, an
 * entry that is empty and hence available, and an entry that is
 * marked as deleted. A deleted entry is available for insertions but
 * cannot be marked as empty. See the book for details.
 *
 */

abstract class Entry {
    static Entry EMPTY = new Empty();
    static Entry DELETED = new Deleted();
    abstract boolean available ();
}

class Empty extends Entry {
    boolean available () { return true; }
    public String toString () { return ""; }
}

class Deleted extends Entry {
    boolean available () { return true; }
    public String toString () { return "X"; }
}

class Value extends Entry {
    private int i;
    Value (int i) { this.i = i; }
    int get () { return this.i; }
    boolean available () { return false; }
    public String toString () { return String.valueOf(this.i); }
}

// -------------------------------------------------------

/**
 *
 * This class, although abstract, will have a constructor and an
 * implementation of each of the three methods: insert, delete, and
 * search.
 *
 * The constructor should take two arguments: an
 * argument 'hf' of type HashFunction, and an argument 'f' of type
 * BiFunction<Integer,Integer,Integer>. The constructor should create
 * an ArrayList of the given size and initialize each slot in that
 * array with an Empty entry. The constructor should also define a
 * BiFunction<Integer,Integer,Integer> called 'dhf' as follows:
 *
 *   this.dhf = (key,index) -> (hf.apply(key) + f.apply(key,index)) % size;
 *
 * This auxiliary hash function applies the regular hash function 'hf'
 * and then modifies the result using the BiFunction 'f' that depends
 * on the current index in the hash table. The subclasses for linear
 * probing, quadratic probing, and double hashing, will each pass an
 * appropriate function 'f' to control the auxiliary function.
 *
 * Each of the methods insert, delete, and search takes a value 'key'
 * to process. Each of the methods will involve a loop that iterates
 * over all the positions in the ArrayList. At iteration 'i', an
 * integer position is calculated using:
 *
 *   int h = dhf.apply(key,i)
 *
 * For insert: if the position 'h' is available then the value 'key'
 * is stored.
 *
 * For delete: if position 'h' has an entry of kind 'Value' and if the
 * stored integer is identical to 'key' then the entry is marked as
 * deleted.
 *
 * For search: if position 'h' is Empty then the item is not found. If
 * position 'h' has an entry of kind 'Value' and if the stored value
 * is identical to 'key' then the item is found.
 *
 */

abstract class HashTableAux extends HashTable {
    private int capacity;
    private HashFunction hf;
    private BiFunction<Integer,Integer,Integer> f;
    private BiFunction<Integer,Integer,Integer> dhf;
    private ArrayList<Entry> slots;

    HashTableAux (HashFunction hf, BiFunction<Integer,Integer,Integer> f) {
        this.capacity = hf.getBound();
        this.hf = hf;
        this.f = f;
        this.dhf = (key,index) -> Math.floorMod(hf.apply(key) + f.apply(key,index), capacity);
        this.slots = new ArrayList<>(capacity);
        for (int i = 0; i< capacity; i++)
            this.slots.add(i, Entry.EMPTY);
    }

    int getCapacity() {
        return capacity;
    }

    void setCapacity(int capacity) {
        for (int i = this.capacity; i < capacity; i++)
            this.slots.add(i, Entry.EMPTY);
        this.capacity = capacity;
    }

    void insert(int key) {
        for (int i = 0; i < capacity; i++) {
            int h = dhf.apply(key, i);
            if (slots.get(h).available()) {
                slots.set(h, new Value(key));
                return;
            }
        }
        rehash();
        insert(key);
    }

    void delete(int key) {
        for (int i = 0; i < capacity; i++) {
            int h = dhf.apply(key, i);
            Entry item = slots.get(h);
            if (item instanceof Value) {
                Value val = (Value)item;
                if (val.get() == key) {
                    slots.set(h, Entry.DELETED);
                    return;
                }
            }
            else if (item instanceof Empty){ //
                return;
            }
        }
    }

    boolean search(int key) {
        for (int i = 0; i < capacity; i++) {
            int h = dhf.apply(key, i);
            Entry item = slots.get(h);
            if (item instanceof Value) {
                Value val = (Value)item;
                if (val.get() == key) {
                    return true;
                }
            }
            else if (item instanceof Empty){ //
                return false;
            }
            /*if (slots.get(h) instanceof Value)
                if (((Value)slots.get(h)).get() == key)
                    return true;*/
        }
        return false;
    }

    void rehash() {
        ArrayList<Integer> items = new ArrayList<>();
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i) instanceof Value)
                items.add(((Value)slots.get(i)).get());
            slots.set(i, Entry.EMPTY);
        }
        int newCapacity = nextPrime(capacity * 2);
        setCapacity(newCapacity);
        hf.setBound(newCapacity);
        for (Integer e : items)
            insert(e);
    }

    public String toString () {
        String result = "";
        for (int i = 0; i< capacity; i++) {
            result += "entry[" + i + "] = ";
            result += slots.get(i).toString();
            result += "\n";
        }
        return result;
    }
}

// -------------------------------------------------------


class HashLinearProbing extends HashTableAux {
    HashLinearProbing(HashFunction hf) {
        super(hf, (key, index) -> index);
    }
}

// -------------------------------------------------------

class HashQuadProbing extends HashTableAux {
    public HashQuadProbing(HashFunction hf) {
        super(hf, (key, index) -> index * index);
    }
}

// -------------------------------------------------------

class HashDouble extends HashTableAux {
    public HashDouble(HashFunction hf, HashFunction hf2) {
        super(hf, (key, index) -> hf2.apply(index));
    }
}

// -------------------------------------------------------
