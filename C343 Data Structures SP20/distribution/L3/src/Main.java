import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Main {

    /*
    Working with collections in java.util
    Here is an excerpt from the class/interface hierarchy:

                        Iterable<E>
                            |
                            |
                       Collection<E>
                       /  |   |   |  \
                      /   |   |   |   \
                     /    |   |   |    \
                    /     |   |   |     \
               Deque   List Queue Set   Stack


    What's the best way to iterate over a collection? We compare the following:
      loops
      iterators
      map-reduce
     */

    public static void main (String[] args) {
        // A small example: adding the squares of the elements of a list
        List<Integer> ints = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));

        // using for-loops
        // ---------------
        int sumFor = 0;
        for (int i=0; i<ints.size(); i++) {
            int k = ints.get(i);
            sumFor += k*k;
        }
        System.out.printf("Sum using for loop = %d%n", sumFor);

        // using foreach loops
        // -------------------
        int sumForeach = 0;
        for (int k : ints) {
            sumForeach += k*k;
        }
        System.out.printf("Sum using foreach loop = %d%n", sumForeach);

        // using iterators
        // ---------------
        ListIterator<Integer> iterator = ints.listIterator();
        int sumIterator = 0;
        while (iterator.hasNext()) {
            int k = iterator.next();
            sumIterator += k*k;
        }
        System.out.printf("Sum using iterator = %d%n", sumIterator);

        // using map-reduce
        // ----------------
        int sumMapreduce = ints.stream().map(k -> k*k).reduce(0, (sum,k) -> sum + k);
        System.out.printf("Sum using map-reduce = %d%n", sumMapreduce);

        // using reduce
        // ----------------
        int sumReduce = ints.stream().reduce(0, (sum,k) -> sum + k*k);
        System.out.printf("Sum using reduce = %d%n", sumReduce);

    }
}
