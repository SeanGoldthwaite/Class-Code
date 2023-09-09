import java.util.function.Function;

// -------------------------------------------------------

abstract class HashFunction implements Function<String,Integer> {}

// -------------------------------------------------------

/* hash function 0
 *
 * Each java object has a built in hash function that returns a hash of an object.
 * The hash code returned will still need to be put in the correct bound.
 * 
 * Ex: 
 *     String x = "hello";
 *     int hash = x.hashCode() % bound;
 */

// hash function 1
class LenMod extends HashFunction {
    private int bound;
    LenMod (int bound) { this.bound = bound; }

    // Creates a hashcode by finding the module of the key length with the bound
    public Integer apply (String key) { 
	    return key.length() % bound;
    }
}

// hash function 2
class CharMod extends HashFunction {
    private int bound;
    CharMod (int bound) { this.bound = bound; }

    // Creates a hashcode by summing the floormod of
    // the code so far plus the character with the bound.
    public Integer apply (String key) {
	    Integer result = 0;
	    for (char c : key.toCharArray())
            result = Math.floorMod(result + c, bound);
	    return result;
    }
}

// hash function 3
class RollingMod extends HashFunction {
    private int bound;
    private int p = 31;

    RollingMod (int bound) { this.bound = bound; }

    public Integer apply (String key) {
        Integer result = 0;
        Integer pow = 1;
        for (char c : key.toCharArray()) {
            result = Math.floorMod(result + (c - 'a' + 1) * pow,bound);
            pow = (pow * p) % bound;
            }
        return result;
    }
}

// -------------------------------------------------------
// -------------------------------------------------------
