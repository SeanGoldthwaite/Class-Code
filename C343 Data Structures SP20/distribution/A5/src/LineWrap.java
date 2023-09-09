import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

class LineWrap {
    private int lineWidth;

    LineWrap (int lineWidth) {
      this.lineWidth = lineWidth;
  }
    public static void main(String[] args) throws NoSuchElementE{
        /*String s = "AAA BB CC DDDDD";
        String s1 = "AA BB CC DD EEE F GGGGGGGGGGG GGGGGGGG HHHHHHH";
        String s2 = "This paper is a reflection of my experience in I101 this semester and includes areas where I have learned a lot and how my new knowledge in those areas could be applied to my future as a CS major";
        //System.out.println(s);
        System.out.println(LineWrap.runGreedy(s, 6));
        System.out.println();
        System.out.println(LineWrap.runDP(s, 6));
        System.out.println();
        System.out.println(LineWrap.dpBU(s, 6));
        System.out.println();
        System.out.println(LineWrap.runGreedy(s1, 17));
        System.out.println();
        System.out.println(LineWrap.runDP(s1, 17));
        System.out.println();
        System.out.println(LineWrap.dpBU(s1, 17));
        System.out.println();
        System.out.println(LineWrap.runGreedy(s2, 56));
        System.out.println();
        System.out.println(LineWrap.runDP(s2, 56));*/
        String s = "In olden times when wishing still helped one, there lived a king whose daughters were all beautiful, but the youngest was so beautiful that the sun itself, which has seen so much, was astonished whenever it shone-in-her-face.  Close-by-the-king's castle lay a great dark forest, and under an old lime-tree in the forest was a well, and when the day was very warm, the king's child went out into the forest and sat down by the side of the cool-fountain, and when she was bored she took a golden ball, and threw it up on high and caught it, and this ball was her favorite plaything.";
        System.out.println(LineWrap.runGreedy(s, 80));
        System.out.println();
        System.out.println(LineWrap.runDP(s, 80));
    }

    //-------------------------------------------------------------------------

    /**
     * The greedy implementation keeps consuming words from the given list
     * of words while keeping track of how much space is left on the current line.
     * If the current word with a preceding space would fit on the current line, the
     * algorithm continues with the remaining words and an adjusted space.
     * If the word preceded by a space does not fit on the current line, a new line
     * is inserted instead and the algorithm continues with the rest of the words and
     * an adjusted space.
     */
    String greedy (List<String> words, int space) {
        int val = 0;
        StringBuilder result = new StringBuilder();
        try {
            while (true) {
                String current = words.getFirst();
                if (current.length() + 1 <= space) {
                    if (space != lineWidth) {
                        result.append(" ");
                        space--;
                    }
                    result.append(current);
                    space -= current.length();
                    words = words.getRest();
                }
                else {
                    val += (int)Math.pow(space, 3);
                    space = lineWidth;
                    result.append("\n");
                }
            }
        }
        catch (NoSuchElementE e) {
            //System.out.println(val);
            return result.toString();
        }
    }

    /**
     * A simple way of running the greedy algorithm
     */

    static String runGreedy (String s, int lineWidth) throws NoSuchElementE {
        List<String> words = List.fromArray(s.split("\\s"));
        LineWrap wrap = new LineWrap(lineWidth);
        String w = words.getFirst();
        String para = wrap.greedy(words, lineWidth);
        return para;
    }

    //-------------------------------------------------------------------------

    /** A hash table for use with the top down dynamic programming solution */
    static Map<Pair<List<String>,Integer>,Pair<String,Integer>> hash = new HashMap<>();

    /**
     * The greedy algorithm always adds words to the current line as long as they
     * would fit. The dynamic programming algorithm instead considers two options
     * for each word: add it to the current line, or insert a new line before
     * the word. For each possibility, an estimate of "badness" is calculated
     * and the best option is chosen. The "badness" of a solution is the sum
     * of the cubes of the amount of space left on each line (except the last line).
     * For example, if the line width is 6 and we have the following text:
     *
     * 1234
     * 12 45
     * 123
     * 12
     *
     * then we calculate the badness as follows: the first line has 2 unused spaces
     * at the end, the next line has 1, and the third has 3. The final line is perfect
     * by definition. So the "badness" estimate is:
     * 2^3 + 1^3 + 3^3 = 8 + 1 + 27 = 36
     *
     * In contrast if the text was:
     *
     * 1234
     * 12 45
     * 123 12
     *
     * the "badness" would be: 2^3 + 1^3 = 8 + 1 = 9
     *
     * so we prefer the second arrangement.
     *
     * I strongly suggest you first write a plain recursive solution and test it on the small
     * example (test1). Once that words properly, you can add the hash table management to
     * get your final dynamic programming solution. Without the hash table, the algorithm
     * will really not work on even a moderate size paragraph.
     */
    Pair<String,Integer> dpTD(List<String> words, int space) {
        if (words.length() == 0)
            hash.put(new Pair<>(words, space), new Pair<>("", 0));
        if (space < 0)
            hash.put(new Pair<>(words, space), new Pair<>("", Integer.MAX_VALUE));
        return hash.computeIfAbsent(new Pair<>(words, space), p -> {
            List<String> list = p.getFst();
            int spaceLeft = p.getSnd();
            try {
                String adding = list.getFirst();
                if (spaceLeft < lineWidth)
                    adding = " " + adding;

                Pair<String, Integer> one = dpTD(list.getRest(), spaceLeft - adding.length());
                Pair<String, Integer> two = dpTD(list.getRest(), (lineWidth - adding.length()));

                int valOne = one.getSnd();
                int valTwo = two.getSnd() + (int)Math.pow(spaceLeft, 3);

                String strOne = adding + one.getFst();
                String strTwo = "\n" + list.getFirst() + two.getFst();

                if (spaceLeft < list.getFirst().length())
                    return new Pair<>(strTwo, valTwo);

                return valOne < valTwo ? (new Pair<>(strOne, valOne)) : (new Pair<>(strTwo, valTwo));
            }
            catch (NoSuchElementE e) {
                return new Pair<>("", 0);
            }
        });
    }

    /**
     * A simple way of running the dynamic programming algorithm
     */
    static String runDP(String s, int lineWidth) throws NoSuchElementE {
        hash = new WeakHashMap<>();
        List<String> words = List.fromArray(s.split("\\s"));
        LineWrap wrap = new LineWrap(lineWidth);
        String w = words.getFirst();
        Pair<String, Integer> sub = wrap.dpTD(words, lineWidth);
        String para = sub.getFst();
        return para;
    }


    //-------------------------------------------------------------------------


    /**
     * Here you are to implement the dynamic programming algorithm in a bottom-up fashion.
     *  You should still use a hash table as shown below but its management is done
     *  "manually" not implicitly when entering / exiting recursive calls.
     */

    //I honestly have no idea how to do this
    //I can't even get TD working properly and that's way more straightforward than this
    static String dpBU(String s, int lineWidth) {
        Map<Pair<Integer,Integer>,Pair<String,Integer>> hash = new HashMap<>();
        String[] words = s.split("\\s+");
        return "";
    }
}
