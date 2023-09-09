import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HashTableTest {

    HashFunction hf;
    HashTable hashLinear, hashQuad, hashDouble;

    @Before
    public void before() {
        hf = new HashMod(23);
        hashLinear = new HashLinearProbing(hf);
        hashQuad = new HashQuadProbing(hf);
        hashDouble = new HashDouble(hf, hf);

    }
    @After
    public void after() {
        hf = null;
        hashLinear = null;
        hashQuad = null;
        hashDouble = null;
    }
    @Test //bound 23
    public void hashLinearProbing() {
        hashLinear.insert(15);
        hashLinear.insert(38);
        hashLinear.insert(37);
        hashLinear.insert(14);
        assertTrue(hashLinear.search(14));
        assertTrue(hashLinear.search(38));
        hashLinear.delete(37);
        assertFalse(hashLinear.search(37));
        assertTrue(hashLinear.search(14));
        hashLinear.delete(15);
        hashLinear.delete(38);
        assertFalse(hashLinear.search(15));
        assertFalse(hashLinear.search(38));
        assertTrue(hashLinear.search(14));
        hashLinear.insert(15);
        assertTrue(hashLinear.search(14));
        hashLinear.insert(37);
        assertTrue(hashLinear.search(14));
    }
    @Test //bound 23
    public void hashQuadProbing() {
        hashQuad.insert(5); //5
        hashQuad.insert(28); //28
        hashQuad.insert(6); //6
        hashQuad.insert(29); //29
        assertTrue(hashQuad.search(29));
        assertTrue(hashQuad.search(28));
        hashQuad.delete(6);
        assertFalse(hashQuad.search(6));
        assertTrue(hashQuad.search(29));
        hashQuad.delete(5);
        hashQuad.delete(28);
        assertFalse(hashQuad.search(5));
        assertFalse(hashQuad.search(28));
        assertTrue(hashQuad.search(29));
        hashQuad.insert(5);
        assertTrue(hashQuad.search(29));
        hashQuad.insert(6);
        assertTrue(hashQuad.search(29));
    }
    @Test
    public void hashSeparateChaining () {
        Random r = new Random(1);
        HashFunction hf = new HashUniversal(r,31, 10);
        HashTable ht = new HashSeparateChaining(hf);
        ht.insert(1);
        ht.insert(12);
        assertTrue(ht.search(12));
        ht.delete(12);
        assertFalse(ht.search(12));
        assertTrue(ht.search(1));
        assertFalse(ht.search(2));
        ht.insert(22);
        System.out.println("Before rehashing");
        System.out.println(ht);
        ht.rehash();
        System.out.println("After rehashing");
        System.out.println(ht);
    }

    @Test
    public void fig55 () {
        HashFunction hf = new HashMod(10);
        HashTable ht = new HashSeparateChaining(hf);
        ht.insert(0);
        ht.insert(81);
        ht.insert(64);
        ht.insert(49);
        ht.insert(9);
        ht.insert(36);
        ht.insert(25);
        ht.insert(16);
        ht.insert(4);
        ht.insert(1);
        System.out.println("Fig. 5.5");
        System.out.println(ht);
    }

    @Test
    public void fig511 () {
        HashFunction hf = new HashMod(10);
        HashTable ht = new HashLinearProbing(hf);
        ht.insert(89);
        ht.insert(18);
        ht.insert(49);
        ht.insert(58);
        ht.insert(69);
        System.out.println("Fig. 5.11");
        System.out.println(ht);
    }

    @Test
    public void fig513 () {
        HashFunction hf = new HashMod(10);
        HashTable ht = new HashQuadProbing (hf);
        ht.insert(89);
        ht.insert(18);
        ht.insert(49);
        ht.insert(58);
        ht.insert(69);
        System.out.println("Fig. 5.13");
        System.out.println(ht);
        ht.insert(26);
        ht.insert(72);
        ht.insert(95);
        System.out.println("Before rehash");
        System.out.println(ht);
        ht.rehash();
        System.out.println("After rehash");
        System.out.println(ht);
    }

    @Test
    public void fig518 () {
        HashFunction hf = new HashMod(10);
        HashFunction hf2 = new HashModThen(7, h -> 7 - h);
        HashTable ht = new HashDouble(hf, hf2);
        ht.insert(89);
        ht.insert(18);
        ht.insert(49);
        ht.insert(58);
        ht.insert(69);
        System.out.println("Fig. 5.18");
        System.out.println(ht);
    }
}