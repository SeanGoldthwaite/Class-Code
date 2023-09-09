import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Words {
    private static String filename = "commonwords.txt";
    // from http://www.mieliestronk.com/corncob_lowercase.txt
    private static List<String> lines = Collections.emptyList();

    public static void readFileIntoList () throws IOException {
        lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8);
    }

    public static void main (String[] args) throws IOException {
        readFileIntoList();

        int size = lines.size();
        Random r = new Random();
        Function<String,Integer> hf0 = s -> Math.floorMod(s.hashCode(), 13);
        HashFunction hf1 = new LenMod(13);
        HashFunction hf2 = new CharMod(13);
        HashFunction hf3 = new RollingMod(13);

        for (int i=0; i<10; i++) {
            String w = lines.get(r.nextInt(size));
            System.out.printf("%d\t%d\t%d\t%s\t%s%n",
                    hf0.apply(w), hf1.apply(w), hf2.apply(w), hf3.apply(w), w);
        }

    }
}
