import java.util.HashMap;
import java.util.WeakHashMap;

class NoChange extends Exception {}

public class Main {

    public static void main (String[] args) throws NoChange, NoSuchElementE {
        List<Coin> scoins =
                new NodeL<>(new Coin(25),
                        new NodeL<>(new Coin(10),
                                new NodeL<>(new Coin(5),
                                        new NodeL<>(new Coin(1),
                                                new EmptyL<>()))));

        System.out.println();

        HashMap<Coin,Integer> hash = new HashMap<>();
        makeChangeGreedy(hash,3, scoins);
        System.out.printf("Making change for %d using usual coins: %s%n",3, hash);

        hash.clear();
        makeChangeGreedy(hash,7, scoins);
        System.out.printf("Making change for %d using usual coins: %s%n",7, hash);

        hash.clear();
        makeChangeGreedy(hash,44, scoins);
        System.out.printf("Making change for %d using usual coins: %s%n",44, hash);

        hash.clear();
        makeChangeGreedy(hash,117, scoins);
        System.out.printf("Making change for %d using usual coins: %s%n",117, hash);

        System.out.println();

        List<Coin> wcoins =
                new NodeL<>(new Coin(17),
                        new NodeL<>(new Coin(7),
                                new NodeL<>(new Coin(3),
                                        new EmptyL<>())));

        hash.clear();
        makeChangeGreedy(hash,3, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",3, hash);

        hash.clear();
        makeChangeGreedy(hash,7, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",7, hash);

        hash.clear();
        makeChangeGreedy(hash,44, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",44, hash);

        try {
            hash.clear();
            makeChangeGreedy(hash, 117, wcoins);
            System.out.printf("Making change for %d using weird coins: %s%n", 117, hash);
        }
        catch (NoChange e) {
            System.out.printf("Failed to make change for %d using weird coins%n", 117);
        }

        System.out.println();

        hash.clear();
        makeChangeSearch(hash,3, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",3, hash);

        hash.clear();
        makeChangeSearch(hash,7, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",7, hash);

        hash.clear();
        makeChangeSearch(hash,44, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",44, hash);

        hash.clear();
        makeChangeSearch(hash,117, wcoins);
        System.out.printf("Making change for %d using weird coins: %s%n",117, hash);

    }


    // given list of coins sorted from largest to smallest, add up to the given amount
    // use the greedy approach to do this
    static void makeChangeGreedy(HashMap<Coin,Integer> result, int amount, List<Coin> coins) throws NoChange, NoSuchElementE {
        int changeToGo = amount;
        while (!coins.isEmpty() && changeToGo > 0) {
            if (coins.getFirst().getValue() <= changeToGo) {
                if (result.containsKey(coins.getFirst()))
                    result.put(coins.getFirst(), result.get(coins.getFirst()) + 1);
                else
                    result.put(coins.getFirst(), 1);
                changeToGo -= coins.getFirst().getValue();
            }
            else {
                coins = coins.getRest();
            }
        }
        if (changeToGo > 0)
            throw new NoChange();
    }


    // given list of coins sorted from largest to smallest, add up to the given amount
    // use dynamic programming to do this
    static void makeChangeSearch(HashMap<Coin,Integer> result, int amount, List<Coin> coins) throws NoChange, NoSuchElementE {
        if (amount < 0 || coins.isEmpty())
            throw new NoChange();
        if (amount == 0)
            return;
        try { //take first coin
            if (result.containsKey(coins.getFirst()))
                result.put(coins.getFirst(), result.get(coins.getFirst()) + 1);
            else
                result.put(coins.getFirst(), 1);
            makeChangeSearch(result, (amount - coins.getFirst().getValue()), coins);
        } //discard first coin
        catch (NoChange e) {
            if (result.get(coins.getFirst()) > 1)
                result.put(coins.getFirst(), result.get(coins.getFirst()) - 1);
            else
                result.remove(coins.getFirst());
            makeChangeSearch(result, amount, coins.getRest());
        }
    }
}
