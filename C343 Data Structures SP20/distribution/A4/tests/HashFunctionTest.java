import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class HashFunctionTest {

    void assertEqualsL (long i, long j) {
        assertEquals(i,j);
    }

    @Test
    public void hashMod () {
        HashFunction hf = new HashMod(13);
        assertEqualsL(5, hf.apply(5));
        assertEqualsL(5, hf.apply(18));
        assertEqualsL(5, hf.apply(5+13*9));
        assertEqualsL(0, hf.apply(13));
        assertEqualsL(1, hf.apply(27));
        assertEqualsL(5 + hf.apply(5), hf.apply(10));
        hf.setBound(23);
        assertEqualsL(0, hf.apply(23));
        assertEqualsL(0, hf.apply(46));
        assertEqualsL(0, hf.apply(69));
        assertEqualsL(10, hf.apply(33));
        assertEqualsL(5 + hf.apply(15), hf.apply(20));
    }

    @Test
    public void hashModAfter () {
        HashFunction hf = new HashModThen(13, h -> 7 - h);
        assertEqualsL(2, hf.apply(5));
        assertEqualsL(2, hf.apply(18));
        assertEqualsL(2, hf.apply(5+13*9));
        assertEqualsL(7, hf.apply(13));
        assertEqualsL(6, hf.apply(27));
        hf.setBound(17);
        assertEqualsL(2, hf.apply(5));
        assertEqualsL(6, hf.apply(18));
        assertEqualsL(2, hf.apply(5+17*9));
        assertEqualsL(11, hf.apply(13));
        assertEqualsL(14, hf.apply(27));
    }

    @Test
    public void hashUniversal () {
        Random r = new Random(1);
        HashFunction hf = new HashUniversal(r, 31, 10);
        assertEqualsL(0, hf.apply(0));
        assertEqualsL(6, hf.apply(1));
        assertEqualsL(1, hf.apply(2));
        assertEqualsL(7, hf.apply(3));
        assertEqualsL(2, hf.apply(4));
        assertEqualsL(8, hf.apply(5));
        assertEqualsL(3, hf.apply(6));
        assertEqualsL(9, hf.apply(7));
        assertEqualsL(4, hf.apply(8));
        assertEqualsL(0, hf.apply(9));
        assertEqualsL(5, hf.apply(10));
        assertEqualsL(1, hf.apply(11));
        assertEqualsL(6, hf.apply(12));
        assertEqualsL(2, hf.apply(13));
        assertEqualsL(7, hf.apply(14));
        assertEqualsL(3, hf.apply(15));
    }

}