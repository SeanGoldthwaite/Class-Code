import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class SeamCarving {
    private int[] pixels;
    private int type, height, width;

    // Field getters

    int[] getPixels() {
        return pixels;
    }

    int getType() {
        return type;
    }

    int getHeight() {
        return height;
    }

    int getWidth() {
        return width;
    }

    // Read and write images

    void readImage(String filename) throws IOException {
        BufferedImage image = ImageIO.read(new File(filename));
        type = image.getType();
        height = image.getHeight();
        width = image.getWidth();
        pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
    }

    void writeImage(String filename) throws IOException {
        BufferedImage image = new BufferedImage(width, height, type);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        ImageIO.write(image, "jpg", new File(filename));
    }

    // Accessing pixels and their neighbors

    Color getColor(int h, int w) {
        int pixel = pixels[w + h * width];
        return new Color(pixel, true);
    }

<<<<<<< HEAD
    ArrayList<Position> getHVneighbors(int h, int w) {
        ArrayList<Position> neighbors = new ArrayList<>();

        if (w == 0) neighbors.add(new Position(h, w + 1));
        else if (w + 1 == width) neighbors.add(new Position(h, w - 1));
        else {
            neighbors.add(new Position(h, w - 1));
            neighbors.add(new Position(h, w + 1));
        }

        if (h == 0) neighbors.add(new Position(h + 1, w));
        else if (h + 1 == height) neighbors.add(new Position(h - 1, w));
        else {
            neighbors.add(new Position(h + 1, w));
            neighbors.add(new Position(h - 1, w));
        }
        return neighbors;
    }

    ArrayList<Position> getBelowNeighbors(int h, int w) {
        ArrayList<Position> neighbors = new ArrayList<>();
=======
<<<<<<< HEAD
    /**
     * This method takes the position of a pixel (h,w) and returns a list of its
     * neighbors' positions in the horizontal and vertical directions.
     * In the general case, these would be at  positions:
     * (h+1,w), (h-1,w), (h,w+1), (h,w-1).
     * Of course, care must be taken when dealing with pixels along the boundaries.
     */

    //can be refactored to reduce length
    ArrayList<Position> getHVneighbors(int h, int w) {
        ArrayList<Position> neighbors = new ArrayList<>();
        if (h == 0) {
            if (w == 0) { //top left corner
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h + 1, w));
            }
            else if (w == width - 1) { //top right corner
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h + 1, w));
            }
            else { //top edge
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h + 1, w));
            }
        }
        else if (h == height - 1) {
            if (w == 0) { //bottom left corner
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h - 1, w));
            }
            else if (w == width - 1) { //bottom right corner
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h - 1, w));
            }
            else { //bottom edge
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h - 1, w));
            }
        }
        else if (h > 0 && h < height - 1) { //not out of bounds
            if (w == 0) { //left edge
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h - 1, w));
            }
            else if (w == width - 1) { //right edge
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h - 1, w));
            }
            else if (w > 0 && w < width - 1) { //somewhere in the middle & not out of bounds
                neighbors.add(new Position(h, w - 1));
                neighbors.add(new Position(h, w + 1));
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h - 1, w));
            }
=======
    ArrayList<Position> getHVneighbors(int h, int w) {
        ArrayList<Position> neighbors = new ArrayList<>();

        if (w == 0) neighbors.add(new Position(h, w + 1));
        else if (w + 1 == width) neighbors.add(new Position(h, w - 1));
        else {
            neighbors.add(new Position(h, w - 1));
            neighbors.add(new Position(h, w + 1));
        }

        if (h == 0) neighbors.add(new Position(h + 1, w));
        else if (h + 1 == height) neighbors.add(new Position(h - 1, w));
        else {
            neighbors.add(new Position(h + 1, w));
            neighbors.add(new Position(h - 1, w));
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
        }
        return neighbors;
    }

    ArrayList<Position> getBelowNeighbors(int h, int w) {
        ArrayList<Position> neighbors = new ArrayList<>();
<<<<<<< HEAD
        if (h == 0) {
            if (w == 0) { //top left corner
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w + 1));
            }
            else if (w == width - 1) { //top right corner
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w - 1));
            }
            else { //top edge
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w + 1));
                neighbors.add(new Position(h + 1, w - 1));
            }
        }
        else if (h == height - 1) { //bottom, no below neighbors
            return neighbors;
        }
        else if (h > 0 && h < height) { //not out of bounds
            if (w == 0) { //left edge
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w + 1));
            }
            else if (w == width - 1) { //right edge
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w - 1));
            }
            else if (w > 0 && w < width){ //somewhere in the middle & not out of bounds
                neighbors.add(new Position(h + 1, w));
                neighbors.add(new Position(h + 1, w + 1));
                neighbors.add(new Position(h + 1, w - 1));
            }
        }
        return neighbors;
=======
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
        if (h + 1 == height) return neighbors;
        neighbors.add(new Position(h + 1, w));
        if (w == 0) {
            neighbors.add(new Position(h + 1, w + 1));
            return neighbors;
        } else if (w + 1 == width) {
            neighbors.add(new Position(h + 1, w - 1));
            return neighbors;
        } else {
            neighbors.add(new Position(h + 1, w + 1));
            neighbors.add(new Position(h + 1, w - 1));
            return neighbors;
        }
<<<<<<< HEAD
=======
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
    }

    // Computing energy at given pixel

    int computeEnergy(int h, int w) {
<<<<<<< HEAD
        Color c = getColor(h, w);
        Function<Integer, Integer> sq = n -> n * n;
        int energy = 0;
        for (Position p : getHVneighbors(h, w)) {
            Color nc = getColor(p.getFirst(), p.getSecond());
            energy += sq.apply(nc.getRed() - c.getRed());
            energy += sq.apply(nc.getGreen() - c.getGreen());
            energy += sq.apply(nc.getBlue() - c.getBlue());
        }
        return energy;
    }

    // Find seam starting from (h,w) going down and return list of positions and cost
    // and then pick best seam starting from some position on the first row

    Map<Position, Pair<List<Position>, Integer>> hash = new WeakHashMap<>();

    Pair<List<Position>, Integer> findSeam(int h, int w) {
        return hash.computeIfAbsent(new Position(h, w), p -> {

                    int energy = computeEnergy(h, w);
                    ArrayList<Position> below = getBelowNeighbors(h, w);
                    if (below.isEmpty()) {
                        return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), new Empty<>()),
                                energy);
                    } else {
                        if (below.size() == 1) {
                            Pair<List<Position>, Integer> s = findSeam(below.get(0).getFirst(), below.get(0).getFirst());
                            return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s.getFirst()),
                                    energy + s.getSecond());
                        } else if (below.size() == 2) {
                            Pair<List<Position>, Integer> s1 = findSeam(below.get(0).getFirst(), below.get(0).getSecond());
                            Pair<List<Position>, Integer> s2 = findSeam(below.get(1).getFirst(), below.get(1).getSecond());
                            if (s1.getSecond() <= s2.getSecond()) {
                                return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s1.getFirst()),
                                        energy + s1.getSecond());
                            } else {
                                return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s2.getFirst()),
                                        energy + s2.getSecond());
                            }
                        } else if (below.size() == 3) {
                            Pair<List<Position>, Integer> s1 = findSeam(below.get(0).getFirst(), below.get(0).getSecond());
                            Pair<List<Position>, Integer> s2 = findSeam(below.get(1).getFirst(), below.get(1).getSecond());
                            Pair<List<Position>, Integer> s3 = findSeam(below.get(2).getFirst(), below.get(2).getSecond());

                            if (s1.getSecond() <= s2.getSecond()) {
                                if (s1.getSecond() <= s3.getSecond()) {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s1.getFirst()),
                                            energy + s1.getSecond());
                                } else {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s3.getFirst()),
                                            energy + s3.getSecond());
                                }
                            } else {
                                if (s2.getSecond() <= s3.getSecond()) {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s2.getFirst()),
                                            energy + s2.getSecond());
                                } else {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s3.getFirst()),
                                            energy + s3.getSecond());
                                }
                            }
                        }
                    }
                    return null;
                });
    }



    Pair<List<Position>, Integer> bestSeam() {
        hash.clear();
        int cost = Integer.MAX_VALUE;
        List<Position> seam = new Empty<>();
        for (int w = 0; w < width; w++) {
            Pair<List<Position>, Integer> r = findSeam(0, w);
            if (r.getSecond() < cost) {
                seam = r.getFirst();
                cost = r.getSecond();
            }
        }
        return new Pair<>(seam, cost);
    }

    // Putting it all together; find best seam and copy pixels without that seam

    void cutSeam() {
        try {
            List<Position> seam = bestSeam().getFirst();
            int[] newPixels = new int[height * (width - 1)];
            for (int h = 0; h < height; h++) {
                int nw = 0;
                for (int w = 0; w < width; w++) {
                    if (seam.isEmpty()) {
                        newPixels[nw + h * (width - 1)] = pixels[w + h * width];
                    }
                    else {
                        Position p = seam.getFirst();
                        if (p.getFirst() == h && p.getSecond() == w) {
                            seam = seam.getRest();
                            nw--;
                        } else {
                            newPixels[nw + h * (width - 1)] = pixels[w + h * width];
                        }
                    }
                    nw++;
                }
            }
            width = width - 1;
            pixels = newPixels;
        } catch (EmptyListE e) {
            throw new Error("Bug");
        }
    }
=======
<<<<<<< HEAD
        int result = 0;
        ArrayList<Position> neighbors = getHVneighbors(h, w);

        //if <h, w> is outside the bounds of the image, returns maximum energy so that path is never favored
        if (w >= width || h < 0 || w < 0)
            return Integer.MAX_VALUE;
        if (neighbors.size() == 0 || h >= height)
            return Integer.MAX_VALUE;
        Color c1 = getColor(h, w);

        for (Position neighbor : neighbors) {
            Color c2 = getColor(neighbor.getFirst(), neighbor.getSecond());
            int r = (int)Math.pow(c1.getRed() - c2.getRed(), 2);
            int g = (int)Math.pow(c1.getGreen() - c2.getGreen(), 2);
            int b = (int)Math.pow(c1.getBlue() - c2.getBlue(), 2);
            result += r + g + b;
        }
        return result;
    }

    /**
     * This next method is the core of our dynamic programming algorithm. We will
     * use the top-down approach with the given hash table (which you should initialize).
     * The key to the hash table is a pixel position. The value stored at each key
     * is the "seam" that starts with this pixel all the way to the bottom
     * of the image and its cost.
     *
     * The method takes the position of a pixel and returns the seam from this pixel
     * and its cost using the following steps:
     *   - compute the energy of the given pixel
     *   - get the list of neighbors below the current pixel
     *   - Base case: if the list of neighbors is empty, return the following pair:
     *       < [<h,w>], energy >
     *     the first component of the pair is a list containing just one position
     *     (the current one); the second component of the pair is the current energy.
     *   - Recursive case: we will consider each of the neighbors below the current
     *     pixel and choose the one with the cheapest seam.
     *
     */
    Map<Pair<Integer, Integer>, Pair<List<Position>, Integer>> hash = new HashMap<>();

    Pair<List<Position>, Integer> findSeam(int h, int w) {
        int currentEnergy = computeEnergy(h, w);
        Pair<Integer, Integer> key = new Pair<>(h, w);
        if (hash.containsKey(key))
            return hash.get(key);
        Pair<List<Position>, Integer> result = null;

        ArrayList<Position> neighbors = getBelowNeighbors(h, w);
        if (neighbors.size() == 0) {
            result = new Pair<>(new Node<>(new Position(h, w), new Empty<>()), computeEnergy(h, w));
            return result;
        }
        else {
            Pair<List<Position>, Integer> left = findSeam(h + 1, w - 1);
            Pair<List<Position>, Integer> down =  findSeam(h + 1, w);
            Pair<List<Position>, Integer> right =  findSeam(h + 1, w + 1);

            int leftEnergy = left.getSecond();
            int downEnergy = down.getSecond();
            int rightEnergy = right.getSecond();

            if (leftEnergy < downEnergy && leftEnergy < rightEnergy)
                result = new Pair<>(new Node<>(new Position(h, w), left.getFirst()), leftEnergy + currentEnergy);
            else if (downEnergy <= rightEnergy)
                result = new Pair<>(new Node<>(new Position(h, w), down.getFirst()), downEnergy + currentEnergy);
            else
                result = new Pair<>(new Node<>(new Position(h, w), right.getFirst()), rightEnergy + currentEnergy);
            hash.put(key, result);
        }
        return hash.get(key);
=======
        Color c = getColor(h, w);
        Function<Integer, Integer> sq = n -> n * n;
        int energy = 0;
        for (Position p : getHVneighbors(h, w)) {
            Color nc = getColor(p.getFirst(), p.getSecond());
            energy += sq.apply(nc.getRed() - c.getRed());
            energy += sq.apply(nc.getGreen() - c.getGreen());
            energy += sq.apply(nc.getBlue() - c.getBlue());
        }
        return energy;
    }

    // Find seam starting from (h,w) going down and return list of positions and cost
    // and then pick best seam starting from some position on the first row

    Map<Position, Pair<List<Position>, Integer>> hash = new WeakHashMap<>();

    Pair<List<Position>, Integer> findSeam(int h, int w) {
        return hash.computeIfAbsent(new Position(h, w), p -> {

                    int energy = computeEnergy(h, w);
                    ArrayList<Position> below = getBelowNeighbors(h, w);
                    if (below.isEmpty()) {
                        return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), new Empty<>()),
                                energy);
                    } else {
                        if (below.size() == 1) {
                            Pair<List<Position>, Integer> s = findSeam(below.get(0).getFirst(), below.get(0).getFirst());
                            return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s.getFirst()),
                                    energy + s.getSecond());
                        } else if (below.size() == 2) {
                            Pair<List<Position>, Integer> s1 = findSeam(below.get(0).getFirst(), below.get(0).getSecond());
                            Pair<List<Position>, Integer> s2 = findSeam(below.get(1).getFirst(), below.get(1).getSecond());
                            if (s1.getSecond() <= s2.getSecond()) {
                                return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s1.getFirst()),
                                        energy + s1.getSecond());
                            } else {
                                return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s2.getFirst()),
                                        energy + s2.getSecond());
                            }
                        } else if (below.size() == 3) {
                            Pair<List<Position>, Integer> s1 = findSeam(below.get(0).getFirst(), below.get(0).getSecond());
                            Pair<List<Position>, Integer> s2 = findSeam(below.get(1).getFirst(), below.get(1).getSecond());
                            Pair<List<Position>, Integer> s3 = findSeam(below.get(2).getFirst(), below.get(2).getSecond());

                            if (s1.getSecond() <= s2.getSecond()) {
                                if (s1.getSecond() <= s3.getSecond()) {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s1.getFirst()),
                                            energy + s1.getSecond());
                                } else {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s3.getFirst()),
                                            energy + s3.getSecond());
                                }
                            } else {
                                if (s2.getSecond() <= s3.getSecond()) {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s2.getFirst()),
                                            energy + s2.getSecond());
                                } else {
                                    return new Pair<List<Position>, Integer>(new Node<Position>(new Position(h, w), s3.getFirst()),
                                            energy + s3.getSecond());
                                }
                            }
                        }
                    }
                    return null;
                });
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
    }


<<<<<<< HEAD
    Pair<List<Position>,Integer> bestSeam () {
        hash.clear();
        ArrayList<Pair<List<Position>, Integer>> seams = new ArrayList<>();
        for (int w = 0; w < width; w++) {
            seams.add(findSeam(0, w));
        }
        Pair<List<Position>, Integer> minSeam = seams.get(0);
        for (int c = 0; c < seams.size(); c++) {
            if (seams.get(c).getSecond() < minSeam.getSecond())
                minSeam = seams.get(c);
        }
        return minSeam;
    }

    /**
     * The last method puts its all together:
     *   - it finds the best seam
     *   - then it creates a new array of pixels representing an image of dimensions
     *     (height,width-1)
     *   - it then copies the old array pixels to the new arrays skipping the pixels
     *     in the seam
     *   - the method does not return anything: instead it updates the width and
     *     pixels instance variables to the new values.
     */
    void cutSeam () {
        Pair<List<Position>, Integer> bestSeam = bestSeam();
        Position[] posArray = List.toArray(Position.class, bestSeam.getFirst());
        ArrayList<Integer> cutList = new ArrayList<>();

        for (Position pos : posArray)
            cutList.add(pos.getSecond() + (pos.getFirst() * width));

        for (Integer pos : cutList)
            pixels[pos] = 0;

        int offset = 0;
        int newWidth = width - 1;
        int[] newPixels = new int[newWidth * height];
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == 0)
                offset++;
            else
                newPixels[i - offset] = pixels[i];
        }
        pixels = newPixels;
        width = newWidth;
=======

    Pair<List<Position>, Integer> bestSeam() {
        hash.clear();
        int cost = Integer.MAX_VALUE;
        List<Position> seam = new Empty<>();
        for (int w = 0; w < width; w++) {
            Pair<List<Position>, Integer> r = findSeam(0, w);
            if (r.getSecond() < cost) {
                seam = r.getFirst();
                cost = r.getSecond();
            }
        }
        return new Pair<>(seam, cost);
    }

    // Putting it all together; find best seam and copy pixels without that seam

    void cutSeam() {
        try {
            List<Position> seam = bestSeam().getFirst();
            int[] newPixels = new int[height * (width - 1)];
            for (int h = 0; h < height; h++) {
                int nw = 0;
                for (int w = 0; w < width; w++) {
                    if (seam.isEmpty()) {
                        newPixels[nw + h * (width - 1)] = pixels[w + h * width];
                    }
                    else {
                        Position p = seam.getFirst();
                        if (p.getFirst() == h && p.getSecond() == w) {
                            seam = seam.getRest();
                            nw--;
                        } else {
                            newPixels[nw + h * (width - 1)] = pixels[w + h * width];
                        }
                    }
                    nw++;
                }
            }
            width = width - 1;
            pixels = newPixels;
        } catch (EmptyListE e) {
            throw new Error("Bug");
        }
>>>>>>> 970c8099d69d25b9cdca7ab52f7a0f74023ffac9
    }
>>>>>>> 084c7665ad224a9f37a5393124d43ad4adba85b8
}
