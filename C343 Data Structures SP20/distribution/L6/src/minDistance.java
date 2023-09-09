import java.util.*;

import static org.junit.Assert.assertEquals;

class minDistance {
    final static int GAP = 2;
    final static int MATCH = 0;
    final static int MISMATCH = 1;

    enum BASE { A, T, G, C }

    // recursive min distance
    static int minDistance (List<BASE> dna1, List<BASE> dna2) {
        try {
            int current = dna1.getFirst() == dna2.getFirst() ? MATCH : MISMATCH;
            int d1 = current + minDistance(dna1.getRest(), dna2.getRest());
            int d2 = GAP + minDistance(dna1.getRest(), dna2);
            int d3 = GAP + minDistance(dna1, dna2.getRest());
            return d1 < d2 ? d1 : d2;
        }
        catch (EmptyListE e) {
            if (dna1.isEmpty()) return GAP * dna2.length();
            else return GAP * dna1.length();
        }
    }

    static Map<Pair<List<BASE>,List<BASE>>,Integer> minDistanceMemo = new WeakHashMap<>();

    // memoized (top down) min distance
    static int mminDistance (List<BASE> dna11, List<BASE> dna21) {
        return minDistanceMemo.computeIfAbsent(new Pair<>(dna11, dna21), p -> {
            List<BASE> dna1 = p.getFirst();
            List<BASE> dna2 = p.getSecond();
            try {
                int current = dna1.getFirst() == dna2.getFirst() ? MATCH : MISMATCH;
                int d1 = current + mminDistance(dna1.getRest(), dna2.getRest()); //match or mismatch
                int d2 = GAP + mminDistance(dna1.getRest(), dna2); //gap into dna1
                int d3 = GAP + mminDistance(dna1, dna2.getRest()); //gap into dna2
                return Math.min(d1,Math.min(d2,d3));
            }
            catch (EmptyListE e) {
                if (dna1.isEmpty()) return GAP * dna2.length();
                else return GAP * dna1.length();
            }
        });
    }

    // bottom up min distance
    static int buminDistance (List<BASE> dna11, List<BASE> dna21) {
        ArrayList<BASE> dna1 = dna11.toArrayList();
        ArrayList<BASE> dna2 = dna21.toArrayList();
        int[][] map = new int[dna2.size() + 1][dna1.size() + 1];

        for (int i = 0; i < map[0].length; i++)
            map[0][i] = i * GAP;
        for (int i = 0; i < map.length; i++)
            map[i][0] = i * GAP;

        for (int row = 1; row < map.length; row++) {
            for (int col = 1; col < map[row].length; col++) {
                int current = (dna2.get(row - 1) != dna1.get(col - 1)) ? MISMATCH : MATCH;
                int diag = current + map[row - 1][col - 1];
                int up = GAP + map[row - 1][col];
                int left = GAP + map[row][col - 1];

                int min = Math.min(diag, Math.min(up, left));
                map[row][col] = min;
            }
        }
        return map[dna2.size()][dna1.size()];
    }

    public static void main(String[] args) {
        List<minDistance.BASE> dna1 =
                new Node<>(minDistance.BASE.A, new Node<>(minDistance.BASE.C, new Empty<>()));
        List<minDistance.BASE> dna2 =
                new Node<>(minDistance.BASE.A, new Node<>(minDistance.BASE.G, new Empty<>()));
        minDistance.buminDistance(dna1, dna2);
    }
}
