import org.junit.Test;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.Assert.*;

public class DPTest {

    // minDistance tests --> small and large

    @Test
    public void minDistance() {
        List<minDistance.BASE> dna1 =
                new Node<>(minDistance.BASE.A, new Node<>(minDistance.BASE.C, new Empty<>()));
        List<minDistance.BASE> dna2 =
                new Node<>(minDistance.BASE.A, new Node<>(minDistance.BASE.G, new Empty<>()));
        assertEquals(1, minDistance.buminDistance(dna1, dna2));
    }

    @Test
    public void minDistance2() {
        Random r = new Random(1);
        Function<Void, minDistance.BASE> g = v -> minDistance.BASE.class.getEnumConstants()[r.nextInt(4)];
        List<minDistance.BASE> dna1 = List.MakeList(g, 521);
        List<minDistance.BASE> dna2 = List.MakeList(g, 450);
        assertEquals(337, minDistance.buminDistance(dna1, dna2));
    }
}
